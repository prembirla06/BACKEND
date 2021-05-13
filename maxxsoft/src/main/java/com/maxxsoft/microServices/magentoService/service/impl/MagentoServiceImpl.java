/*******************************************************
* Copyright (C) 2020, TecMaXX GmbH
* All Rights Reserved.
* 
* NOTICE: All information contained herein is, and remains
* the property of TecMaXX GmbH and its suppliers,
* if any. The intellectual and technical concepts contained
* herein are proprietary to TecMaXX GmbH
* and its suppliers and are protected by trade secret or copyright law.
* Dissemination of this information or reproduction of this material
* is strictly forbidden unless prior written permission is obtained
* from TecMaXX GmbH.
* 
* TecMaXX GmbH
* Auf der Suend 18, DE-91757 Treuchtlingen
*******************************************************/
package com.maxxsoft.microServices.magentoService.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.model.request.AttributeResult;
import com.maxxsoft.microServices.articleService.model.request.Categories;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.magentoService.model.MagentoCategory;
import com.maxxsoft.microServices.magentoService.model.MagentoCustomAttribute;
import com.maxxsoft.microServices.magentoService.model.MagentoCustomAttributeValue;
import com.maxxsoft.microServices.magentoService.model.MagentoProduct;
import com.maxxsoft.microServices.magentoService.model.MagentoUser;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoMediaResponse;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoMediaRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoPriceRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.magentoService.repository.MagentoCustomAttributeRepository;
import com.maxxsoft.microServices.magentoService.repository.MagentoProductRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;
import com.maxxsoft.microServices.orderService.jsonmodel.MagentoOrderResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Service
@Slf4j
public class MagentoServiceImpl implements MagentoService {

	@Value("${app.magento.url}")
	private String magentoUrl;
	@Value("${app.magento.password}")
	private String password;
	@Value("${app.magento.user}")
	private String user;
	@Value("${spring.profiles.active}")
	private String activeProfile;

	private static final String PRODUCT = "/rest/V1/products";
	private static final String ALL_PRODUCT = "/rest/all/V1/products";
	private static final String LOGIN = "/rest/V1/integration/admin/token";
	private static final String CATEGORIES = "/rest/V1/categories";
	private static final String ATTRIBUTES = "/rest/V1/products/attributes";
	private static final String SPECIAL_PRICE = "/rest/V1/products/special-price-delete";
	private static final String RELATIVE_ORDER_PATH = "rest/V1/orders";
	String categoryName = null;
	MagentoCategory magentoCategory = null;

	@Bean
	public RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(60000);
		httpRequestFactory.setConnectTimeout(60000);
		httpRequestFactory.setReadTimeout(60000);
		return new RestTemplate(httpRequestFactory);
	}

	@Autowired
	MagentoProductRepository magentoProductRepository;

	@Autowired
	ImageService imageService;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	ArticleSetRepository articleSetRepository;

	@Autowired
	MagentoCustomAttributeRepository magentoCustomAttributeRepository;

	@Override
	public String getMogentoAccessToken(MagentoUser magentoUser) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		// if (!activeProfile.equals("prod")) {
		// headers.add("Authorization", "Basic dGVzdDpzaG9w");
		// }
		HttpEntity<MagentoUser> entity = new HttpEntity<MagentoUser>(magentoUser, headers);
		String bearer = getRestTemplate().exchange(magentoUrl + LOGIN, HttpMethod.POST, entity, String.class).getBody();
		return bearer;
	}

	@Override
	public String getProductList() {
		HttpHeaders headers = getHeader();
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return getRestTemplate()
				.exchange(magentoUrl + PRODUCT + "?searchCriteria[page_size]=20", HttpMethod.GET, entity, String.class)
				.getBody();
	}

	@Override
	public String getCategories() {
		HttpHeaders headers = getHeader();
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return getRestTemplate().exchange(magentoUrl + CATEGORIES, HttpMethod.GET, entity, String.class).getBody();
	}

	private String getBearer() {
		MagentoUser magentoUser = new MagentoUser(user, password);
		return getMogentoAccessToken(magentoUser);
	}

	@Override
	public String saveProduct(MagentoProductList magentoProductList, HttpHeaders headers) {

		Set<MagentoCategory> magentoCategories = new HashSet();
		Set<MagentoCustomAttributeValue> magentoCustomAttributes = new HashSet();

		MagentoProduct magentoProduct = new MagentoProduct();

		magentoProduct.setSku(magentoProductList.getProduct().getSku());
		magentoProductList.getProduct().getCustom_attributes().forEach(customAttribure -> {
			// System.out.println(customAttribure.getAttribute_code() + " ---- "
			// + customAttribure.getValue());
			MagentoCustomAttributeValue magentoCustomAttributeValue;

			AttributeResult[] result = getAttributeData1(customAttribure.getAttribute_code(), headers);

			if (result != null && result.length > 0) {
				ArrayList<String> names = new ArrayList<String>();
				Arrays.stream(result).forEach(ar -> {
					String[] ids = customAttribure.getValue().split(",");
					Arrays.stream(ids).forEach(id -> {
						if (ar.getValue().equals(id)) {
							names.add(ar.getLabel());
						}
					});
				});
				String csnames = String.join(",", names);
				magentoCustomAttributeValue = new MagentoCustomAttributeValue(customAttribure.getAttribute_code(),
						customAttribure.getValue(), csnames, magentoProduct);
			} else {

				magentoCustomAttributeValue = new MagentoCustomAttributeValue(customAttribure.getAttribute_code(),
						customAttribure.getValue(), null, magentoProduct);
			}
			magentoCustomAttributes.add(magentoCustomAttributeValue);
		});

		Categories categories = getCategories1(getHeader());
		magentoProductList.getProduct().getExtension_attributes().getCategory_links().forEach(category -> {
			int categoryId = Integer.valueOf(category.getCategory_id());
			MagentoCategory magentoCategory = new MagentoCategory(category.getPosition(), category.getCategory_id(),
					getMagentoCategoryName(categoryId, categories), magentoProduct);
			magentoCategories.add(magentoCategory);
		});

		magentoProduct.setSku(magentoProductList.getProduct().getSku());
		magentoProduct.setMagentoCategories(magentoCategories);
		magentoProduct.setMagentoCustomAttributes(magentoCustomAttributes);
		magentoProductRepository.save(magentoProduct);

		HttpEntity<MagentoProductList> entity = new HttpEntity<MagentoProductList>(magentoProductList, headers);

		return getRestTemplate().exchange(magentoUrl + PRODUCT, HttpMethod.POST, entity, String.class).getBody();

	}

	@Override
	public void saveProductMedia(MagentoProductList magentoProductList, HttpHeaders headers) {
		log.info("Pushing media of this article to magento");
		String number = magentoProductList.getProduct().getSku();
		Optional<Article> optionalArticle = articleRepository.findByNumber(number);
		if (optionalArticle.isPresent()) {
			optionalArticle.get().getImages().forEach(image -> {
				imageService.pushImageToMagento(image, optionalArticle.get().getShortName(), number);
			});

		} else {
			Optional<ArticleSet> optionalArticleSet = articleSetRepository.findByNumber(number);
			if (optionalArticleSet.isPresent()) {
				optionalArticleSet.get().getImages().forEach(image -> {
					imageService.pushImageToMagentoForSet(image, optionalArticleSet.get().getShortName(), number);
				});

			}
		}
	}

	@Override
	public String getProductBySKU(String sku, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8"));

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.GET, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MagentoProductResponse getProductBySKU1(String sku, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8"));

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.GET, entity, MagentoProductResponse.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String updateProductBySku(String sku, MagentoProductList magentoProductList, HttpHeaders headers) {

		HttpEntity<MagentoProductList> entity = new HttpEntity<MagentoProductList>(magentoProductList, headers);

		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8"));

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.PUT, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public String updateProductBySkuFormUI(String sku, MagentoProductList magentoProductList, HttpHeaders headers) {

		Optional<MagentoProduct> optionalMagentoProduct = magentoProductRepository.findBySku(sku);
		if (optionalMagentoProduct.isPresent()) {
			magentoProductRepository.delete(optionalMagentoProduct.get());

			Set<MagentoCategory> magentoCategories = new HashSet();
			Set<MagentoCustomAttributeValue> magentoCustomAttributes = new HashSet();
			MagentoProduct magentoProduct = new MagentoProduct();
			magentoProduct.setSku(magentoProductList.getProduct().getSku());
			magentoProductList.getProduct().getCustom_attributes().forEach(customAttribure -> {
				MagentoCustomAttributeValue magentoCustomAttributeValue;

				AttributeResult[] result = getAttributeData1(customAttribure.getAttribute_code(), headers);

				if (result != null && result.length > 0) {
					ArrayList<String> names = new ArrayList<String>();
					Arrays.stream(result).forEach(ar -> {
						String[] ids = customAttribure.getValue().split(",");
						Arrays.stream(ids).forEach(id -> {
							if (ar.getValue().equals(id)) {
								names.add(ar.getLabel());
							}
						});
					});
					String csnames = String.join(",", names);
					magentoCustomAttributeValue = new MagentoCustomAttributeValue(customAttribure.getAttribute_code(),
							customAttribure.getValue(), csnames, magentoProduct);
				} else {

					magentoCustomAttributeValue = new MagentoCustomAttributeValue(customAttribure.getAttribute_code(),
							customAttribure.getValue(), null, magentoProduct);
				}
				magentoCustomAttributes.add(magentoCustomAttributeValue);
			});
			Categories categories = getCategories1(getHeader());
			magentoProductList.getProduct().getExtension_attributes().getCategory_links().forEach(category -> {
				int categoryId = Integer.valueOf(category.getCategory_id());
				MagentoCategory magentoCategory = new MagentoCategory(category.getPosition(), category.getCategory_id(),
						getMagentoCategoryName(categoryId, categories), magentoProduct);
				magentoCategories.add(magentoCategory);
			});

			magentoProduct.setSku(magentoProductList.getProduct().getSku());
			magentoProduct.setMagentoCategories(magentoCategories);
			magentoProduct.setMagentoCustomAttributes(magentoCustomAttributes);
			magentoProductRepository.saveAndFlush(magentoProduct);
			HttpEntity<MagentoProductList> entity = new HttpEntity<MagentoProductList>(magentoProductList, headers);

			UriComponentsBuilder builder = null;
			try {
				builder = UriComponentsBuilder
						.fromUriString(magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8"));

				URI uri = builder.build(true).toUri();
				return getRestTemplate().exchange(uri, HttpMethod.PUT, entity, String.class).getBody();
			} catch (RestClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public String deleteProductBySku(String sku) {
		HttpHeaders headers = getHeader();
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8"));

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.DELETE, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MagentoCustomAttribute createCustomAttribute(MagentoCustomAttribute magentoCustomAttribute) {
		return magentoCustomAttributeRepository.save(magentoCustomAttribute);
	}

	@Override
	public List<MagentoCustomAttribute> getCustomAttributes() {
		return magentoCustomAttributeRepository.findAll();
	}

	@Override
	public void deleteCustomAttribute(Long magentoCustomAttributeId) {
		magentoCustomAttributeRepository.deleteById(magentoCustomAttributeId);
	}

	@Override
	public String createMedia(String sku, MagentoMediaRequest magentoMediaRequest, HttpHeaders headers) {

		HttpEntity<MagentoMediaRequest> entity = new HttpEntity<MagentoMediaRequest>(magentoMediaRequest, headers);
		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder
					.fromUriString(magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8") + "/media/");

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.POST, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String deleteMedia(String sku, Long entryId, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(
					magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8") + "/media/" + entryId + "/");

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.DELETE, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public String updateMedia(String sku, Long entryId, MagentoMediaRequest magentoMediaRequest, HttpHeaders headers) {
		HttpEntity<MagentoMediaRequest> entity = new HttpEntity<MagentoMediaRequest>(magentoMediaRequest, headers);
		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(
					magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8") + "/media/" + entryId + "/");

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.PUT, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MagentoMediaResponse[] getMedia(String sku, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder
					.fromUriString(magentoUrl + PRODUCT + "/" + URLEncoder.encode(sku, "UTF-8") + "/media/");

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.GET, entity, MagentoMediaResponse[].class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public HttpHeaders getHeader() {
		HttpHeaders headers = new HttpHeaders();
		// headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		// headers.add("User-Agent", "Spring's RestTemplate" ); // value can be
		// whatever
		headers.add("Authorization", "Bearer " + getBearer().replaceAll("[^a-zA-Z0-9]", ""));
		// headers.setBearerAuth(getBearer().replaceAll("[^a-zA-Z0-9]", ""));
		return headers;
	}

	@Override
	public String getAttributeData(String attributeCode, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try {
			return getRestTemplate().exchange(magentoUrl + PRODUCT + "/attributes/" + attributeCode + "/options",
					HttpMethod.GET, entity, String.class).getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public AttributeResult[] getAttributeData1(String attributeCode, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		try {
			return getRestTemplate().exchange(magentoUrl + PRODUCT + "/attributes/" + attributeCode + "/options",
					HttpMethod.GET, entity, AttributeResult[].class).getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Categories getCategories1(HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return getRestTemplate().exchange(magentoUrl + CATEGORIES, HttpMethod.GET, entity, Categories.class).getBody();
	}

	@Override
	public String deleteSpecialPrice(MagentoPriceRequest magentoPriceRequest, HttpHeaders headers) {

		HttpEntity<MagentoPriceRequest> entity = new HttpEntity<MagentoPriceRequest>(magentoPriceRequest, headers);
		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(magentoUrl + SPECIAL_PRICE);

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.POST, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getMagentoCategoryName(int categoryId, Categories categories) {
		// System.out.println(categoryId);
		try {
			if (categories != null) {
				if (categoryId == categories.getId()) {
					categoryName = categories.getName();
				} else if (categories.getChildren_data() != null) {
					Categories[] categories1 = categories.getChildren_data();
					Arrays.stream(categories1).forEach(level2 -> {
						if (categoryId == level2.getId()) {
							categoryName = level2.getName();
						} else if (level2.getChildren_data() != null) {
							Arrays.stream(level2.getChildren_data()).forEach(level3 -> {
								if (categoryId == level3.getId()) {
									categoryName = level3.getName();
								} else if (level3.getChildren_data() != null) {
									Arrays.stream(level3.getChildren_data()).forEach(level4 -> {
										if (categoryId == level4.getId()) {
											categoryName = level4.getName();
										} else if (level4.getChildren_data() != null) {
											Arrays.stream(level4.getChildren_data()).forEach(level5 -> {
												if (categoryId == level5.getId()) {
													categoryName = level5.getName();
												}
											});
										}
									});
								}
							});
						}
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println(categoryName);
		return categoryName;
	}

	@Override
	public void deleteCategoryFromLocalMagentoProduct(String sku, int categoryId) {
		Optional<MagentoProduct> magentoProductOptional = magentoProductRepository.findBySku(sku);
		if (magentoProductOptional.isPresent()) {
			MagentoProduct magentoProduct = magentoProductOptional.get();
			magentoProduct.getMagentoCategories().forEach(category -> {
				if (categoryId == Integer.valueOf(category.getCategory())) {
					magentoCategory = category;
				}
			});
			if (magentoCategory != null) {
				magentoProduct.getMagentoCategories().remove(magentoCategory);
			}
			magentoProductRepository.saveAndFlush(magentoProduct);
		}
	}

	@Override
	public void addCategoryToLocalMagentoProduct(String sku, int categoryPosition, String categoryId,
			String categoryName) {
		Optional<MagentoProduct> magentoProductOptional = magentoProductRepository.findBySku(sku);
		if (magentoProductOptional.isPresent()) {
			MagentoProduct magentoProduct = magentoProductOptional.get();
			MagentoCategory magentoCategory = new MagentoCategory(categoryPosition, categoryId, categoryName,
					magentoProduct);
			magentoProduct.getMagentoCategories().add(magentoCategory);
			magentoProductRepository.saveAndFlush(magentoProduct);
		}
	}

	@Override
	public String deleteMagentoProductFromCategory(String sku, int categoryId, HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = null;
		try {
			builder = UriComponentsBuilder.fromUriString(
					magentoUrl + CATEGORIES + "/" + categoryId + "/" + "/products/" + URLEncoder.encode(sku, "UTF-8"));

			URI uri = builder.build(true).toUri();
			return getRestTemplate().exchange(uri, HttpMethod.DELETE, entity, String.class).getBody();
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public MagentoOrderResponse getNewOrders(HttpHeaders headers, String lastOrderIdFetched) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		// String searchCriteria =
		// "searchCriteria[filter_groups][0][filters][0][field]=created_at&"
		// + "searchCriteria[filter_groups][0][filters][0][value]=2020-10-01
		// 07:19:29&"
		// + "searchCriteria[filter_groups][0][filters][0][condition_type]=gt";

		String searchCriteria = "searchCriteria[filter_groups][0][filters][0][field]=increment_id&"
				+ "searchCriteria[filter_groups][0][filters][0][value]=" + lastOrderIdFetched + "&"
				+ "searchCriteria[filter_groups][0][filters][0][condition_type]=gt";

		return getRestTemplate().exchange(magentoUrl + "/" + RELATIVE_ORDER_PATH + "?" + searchCriteria, HttpMethod.GET,
				entity, MagentoOrderResponse.class).getBody();
	}

	@Override
	public MagentoOrderResponse getAllOrders(HttpHeaders headers) {
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return getRestTemplate().exchange(magentoUrl + "/" + RELATIVE_ORDER_PATH + "?searchCriteria=all",
				HttpMethod.GET, entity, MagentoOrderResponse.class).getBody();
	}

	@Override
	public String urlKeyBuilder(String url_key) {
		url_key = url_key.replaceAll("[\"/\\:,+.*() ]", "-");
		url_key = url_key.replace("ü", "ue");
		url_key = url_key.replace("Ü", "Ue");
		url_key = url_key.replace("ö", "oe");
		url_key = url_key.replace("Ö", "Oe");
		url_key = url_key.replace("ä", "ae");
		url_key = url_key.replace("Ä", "Ae");
		url_key = url_key.replace("ß", "ss");
		url_key = url_key.replace("é", "e");
		url_key = url_key.toLowerCase();
		url_key = url_key.replaceAll("-----", "-");
		url_key = url_key.replaceAll("----", "-");
		url_key = url_key.replaceAll("---", "-");
		url_key = url_key.replaceAll("--", "-");
		if (url_key.charAt(url_key.length() - 1) == '-') {
			url_key = url_key.substring(0, url_key.length() - 1);
		}
		return url_key;
	}
}