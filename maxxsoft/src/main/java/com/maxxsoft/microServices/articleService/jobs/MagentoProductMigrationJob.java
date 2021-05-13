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
package com.maxxsoft.microServices.articleService.jobs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleStueckRepository;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.model.request.AttributeResult;
import com.maxxsoft.microServices.articleService.model.request.Categories;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.ArticleService;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoCategoryRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoCustomAttributeValueRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoExtensionAttribute;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductRequest;
import com.maxxsoft.microServices.magentoService.repository.MagentoCustomAttributeRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class MagentoProductMigrationJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	private ErpArticleRepository erpArticleRepository;

	@Autowired
	private ErpArticleStueckRepository erpArticleStueckRepository;

	@Autowired
	MagentoCustomAttributeRepository magentoCustomAttributeRepository;

	@Autowired
	private MagentoService magentoService;

	@Autowired
	private ArticleService articleService;

	@Autowired
	private PriceService priceService;

	BigDecimal articleSetBuyPrice;

	MagentoCategoryRequest magentoCategoryRequest;

	// @Scheduled(fixedDelay = 100000000)
	public void runMagentoFieldUpdateArticle() {
		HttpHeaders headers = magentoService.getHeader();
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				System.out.println(
						"---Article Number OR SKU---" + article.getNumber() + "article Id--" + article.getArticleId());
				Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(article.getNumber());
				if (artikelOptional.isPresent()) {

					Artikel artikel = artikelOptional.get();
					String userfield01 = artikel.getUserfeld01();
					String userfield08 = artikel.getUserfeld08();

					if (StringUtils.isEmpty(userfield08)) {
						System.out.println("ArticleSet with empty userfield08---" + article.getNumber());
					} else {
						String[] customAttributes = userfield08.split(",");
						int length = customAttributes.length;
						if (length == 12) {

							MagentoProductList magentoProductList = new MagentoProductList();
							MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
							MagentoExtensionAttribute magentoExtensionAttribute = new MagentoExtensionAttribute();
							int actualStock = article.getStock() - article.getPreOrder();
							BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(),
									article.getBuyPrice(), 1L);
							BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(article.getNumber(),
									article.getBuyPrice(), 1L);
							BigDecimal salePrice = priceService.getDirectSalePrice(article.getNumber(), actualStock,
									minimumSellingPrice);
							int categoryPosition = -(erpArticleService.getOrderLastxDays(artikel.getRecId(), 60)
									+ actualStock);

							String shortNameWithoutSpecialChars = article.getShortName().replaceAll("[\"/\\:]", "");
							String longNameWithoutSpecialChars = article.getLongName().replaceAll("[\"/\\:,+.*() ]",
									"");

							mogentoProductRequest.setSku(article.getNumber());
							mogentoProductRequest.setName(longNameWithoutSpecialChars);
							mogentoProductRequest.setPrice(sellingPrice);
							mogentoProductRequest.setStatus(1);
							mogentoProductRequest.setAttribute_set_id(4L);
							mogentoProductRequest.setType_id("simple");
							mogentoProductRequest.setVisibility(4);
							mogentoProductRequest
									.setWeight(articleService.getTotalArticleWeight(article.getArticleId()));
							mogentoProductRequest.setCustom_attributes(getMagentoCustomAttributeValueRequestSet(
									userfield01, userfield08, article.getShortDescription(),
									article.getLongDescription(), shortNameWithoutSpecialChars, article.getLongName(),
									article.getDeliveryTime(), salePrice, headers));
							magentoExtensionAttribute.setCategory_links(
									getMagentoCategoryRequestSet(userfield08, salePrice, categoryPosition, headers));
							mogentoProductRequest.setExtension_attributes(magentoExtensionAttribute);

							magentoProductList.setProduct(mogentoProductRequest);
							magentoService.saveProduct(magentoProductList, headers);
						} else {
							System.out.println("Article with length problem userfield08---" + article.getNumber());
						}
					}
				}
			}
		});
		System.out.println("---------finished--------------");
	}

	// @Scheduled(fixedDelay = 100000000)
	public void runMagentoFieldUpdateArticleSet() {
		HttpHeaders headers = magentoService.getHeader();
		System.out.println("----start---");

		articleSetRepository.findAll().forEach(articleSet -> {
			System.out.println("ArticleSet number---" + articleSet.getNumber() + "article Set Id--"
					+ articleSet.getArticleSetId());
			Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(articleSet.getNumber());
			if (artikelOptional.isPresent()) {

				Artikel artikel = artikelOptional.get();
				String userfield01 = artikel.getUserfeld01();
				String userfield08 = artikel.getUserfeld08();

				if (StringUtils.isEmpty(userfield08)) {
					System.out.println("ArticleSet with empty userfield08---" + articleSet.getNumber());
				} else {
					String[] customAttributes = userfield08.split(",");
					int length = customAttributes.length;
					if (length == 12) {

						MagentoProductList magentoProductList = new MagentoProductList();
						MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
						MagentoExtensionAttribute magentoExtensionAttribute = new MagentoExtensionAttribute();
						String shortNameWithoutSpecialChars = articleSet.getShortName().replaceAll("[\"/\\:,+.*() ]",
								"");
						String longNameWithoutSpecialChars = articleSet.getLongName().replaceAll("[\"/\\:,+.*() ]", "");

						articleSetBuyPrice = new BigDecimal(0);
						articleSet.getArticleSetRelations().forEach(asr -> {
							articleSetBuyPrice = articleSetBuyPrice.add(asr.getArticle().getBuyPrice());
						});

						BigDecimal sellingPrice = priceService.getSellingPrice(articleSet.getNumber(),
								articleSetBuyPrice, 1L);
						// Saving actual stock in stock. This value is equal
						// to the mimimum actual stock among all the
						// partlist
						int actualStock = articleSet.getStock();
						BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(articleSet.getNumber(),
								articleSetBuyPrice, 1L);
						BigDecimal salePrice = priceService.getDirectSalePrice(articleSet.getNumber(), actualStock,
								minimumSellingPrice);
						int categoryPosition = erpArticleService.getOrderLastxDays(artikel.getRecId(), 60)
								+ actualStock;
						mogentoProductRequest.setSku(articleSet.getNumber());
						mogentoProductRequest.setName(longNameWithoutSpecialChars);
						mogentoProductRequest.setPrice(sellingPrice);
						mogentoProductRequest.setStatus(1);
						mogentoProductRequest.setAttribute_set_id(4L);
						mogentoProductRequest.setType_id("simple");
						mogentoProductRequest.setVisibility(4);
						mogentoProductRequest
								.setWeight(articleService.getTotalArticleSetWeight(articleSet.getArticleSetId()));
						mogentoProductRequest.setCustom_attributes(getMagentoCustomAttributeValueRequestSet(userfield01,
								userfield08, articleSet.getShortDescription(), articleSet.getLongDescription(),
								shortNameWithoutSpecialChars, articleSet.getLongName(), articleSet.getDeliveryTime(),
								salePrice, headers));
						magentoExtensionAttribute.setCategory_links(
								getMagentoCategoryRequestSet(userfield08, salePrice, categoryPosition, headers));
						mogentoProductRequest.setExtension_attributes(magentoExtensionAttribute);

						magentoProductList.setProduct(mogentoProductRequest);

						magentoService.saveProduct(magentoProductList, headers);
					} else {
						System.out.println("ArticleSet with length problem userfield08---" + articleSet.getNumber());
					}
				}
			}
		});
		System.out.println("----end---");
	}

	private Set<MagentoCustomAttributeValueRequest> getMagentoCustomAttributeValueRequestSet(String userfield01,
			String userfield08, String shortDesc, String LongDesc, String shortName, String longName, int deliveryTime,
			BigDecimal salePrice, HttpHeaders header) {
		Set<MagentoCustomAttributeValueRequest> magentoCustomAttributeValueRequestSet = new HashSet<>();
		String[] customAttributes = userfield08.split(",");
		// System.out.println(userfield08);
		magentoCustomAttributeRepository.findAll().forEach(magentoCustomAttribute -> {
			if (magentoCustomAttribute.getOrderNumber() < 12) {
				String customAttributeValue = customAttributes[magentoCustomAttribute.getOrderNumber()];
				if (!customAttributeValue.equalsIgnoreCase("x")) {
					AttributeResult[] result = magentoService
							.getAttributeData1(magentoCustomAttribute.getAttributeName(), header);
					String[] values = customAttributeValue.split("/");
					if (values.length > 1) {
						ArrayList<String> ids = new ArrayList<String>();
						Arrays.stream(values).forEach(value -> {
							Arrays.stream(result).forEach(ar -> {
								if (ar.getLabel().equals(value)) {
									ids.add(ar.getValue());
								}
							});
						});
						String csvalues = String.join(",", ids);
						MagentoCustomAttributeValueRequest magentoCustomAttributeValueRequest = new MagentoCustomAttributeValueRequest(
								magentoCustomAttribute.getAttributeName(), csvalues);
						magentoCustomAttributeValueRequestSet.add(magentoCustomAttributeValueRequest);

					} else {
						if (result.length > 0) {
							Arrays.stream(result).forEach(ar -> {
								if (ar.getLabel().equals(customAttributeValue)) {

									MagentoCustomAttributeValueRequest magentoCustomAttributeValueRequest = new MagentoCustomAttributeValueRequest(
											magentoCustomAttribute.getAttributeName(), ar.getValue());
									magentoCustomAttributeValueRequestSet.add(magentoCustomAttributeValueRequest);
								}
							});
						} else {
							MagentoCustomAttributeValueRequest magentoCustomAttributeValueRequest = new MagentoCustomAttributeValueRequest(
									magentoCustomAttribute.getAttributeName(), customAttributeValue);
							magentoCustomAttributeValueRequestSet.add(magentoCustomAttributeValueRequest);
						}
					}
				}
			}
		});
		MagentoCustomAttributeValueRequest shortDescAttribute = new MagentoCustomAttributeValueRequest(
				"short_description", shortDesc);
		MagentoCustomAttributeValueRequest longDescAttribute = new MagentoCustomAttributeValueRequest("description",
				LongDesc);
		MagentoCustomAttributeValueRequest urlKeyAttribute = new MagentoCustomAttributeValueRequest("url_key",
				urlKeyBuilder(longName));
		MagentoCustomAttributeValueRequest metaTitleAttribute = new MagentoCustomAttributeValueRequest("meta_title",
				shortName);
		MagentoCustomAttributeValueRequest metaDescAttribute = new MagentoCustomAttributeValueRequest(
				"meta_description", shortName);
		MagentoCustomAttributeValueRequest metaKeywordAttribute = new MagentoCustomAttributeValueRequest("meta_keyword",
				shortName);
		if (salePrice != null) {
			MagentoCustomAttributeValueRequest specialPriceAttribute = new MagentoCustomAttributeValueRequest(
					"special_price", salePrice.toString());
			MagentoCustomAttributeValueRequest specialPriceTypeAttribute = new MagentoCustomAttributeValueRequest(
					"ms_special_price_type", "Sale Price");
			magentoCustomAttributeValueRequestSet.add(specialPriceAttribute);
			magentoCustomAttributeValueRequestSet.add(specialPriceTypeAttribute);
		}
		if (userfield01 != null && !userfield01.equalsIgnoreCase("x")) {
			MagentoCustomAttributeValueRequest googleshoppingGtinAttribute = new MagentoCustomAttributeValueRequest(
					"ms_googleshopping_gtin", userfield01);
			MagentoCustomAttributeValueRequest googleshoppingGtinDateAttribute = new MagentoCustomAttributeValueRequest(
					"ms_googleshopping_gtin_date", LocalDate.now().toString());
			magentoCustomAttributeValueRequestSet.add(googleshoppingGtinAttribute);
			magentoCustomAttributeValueRequestSet.add(googleshoppingGtinDateAttribute);
		}
		magentoCustomAttributeValueRequestSet.add(shortDescAttribute);
		magentoCustomAttributeValueRequestSet.add(longDescAttribute);
		magentoCustomAttributeValueRequestSet.add(urlKeyAttribute);
		magentoCustomAttributeValueRequestSet.add(metaTitleAttribute);
		magentoCustomAttributeValueRequestSet.add(metaDescAttribute);
		magentoCustomAttributeValueRequestSet.add(metaKeywordAttribute);
		magentoCustomAttributeValueRequestSet.add(getMagentoDeliveyTimeCustomAttribute(deliveryTime));

		return magentoCustomAttributeValueRequestSet;

	}

	private Set<MagentoCategoryRequest> getMagentoCategoryRequestSet(String userfield08, BigDecimal salePrice,
			int categoryPosition, HttpHeaders header) {

		Set<MagentoCategoryRequest> magentoCategoryRequestSet = new HashSet<>();
		String[] customAttributes = userfield08.split(",");
		magentoCustomAttributeRepository.findAll().forEach(magentoCustomAttribute -> {
			if (magentoCustomAttribute.getOrderNumber() < 6) {
				String customAttributeValue = customAttributes[magentoCustomAttribute.getOrderNumber()];

				String[] values = customAttributeValue.split("/");
				if (values.length > 1) {

					Arrays.stream(values).forEach(value -> {
						magentoCategoryRequestSet.add(getMagentoCategoryRequest(value, categoryPosition, header));
					});

				} else {
					magentoCategoryRequestSet
							.add(getMagentoCategoryRequest(customAttributeValue, categoryPosition, header));
				}
			}
		});

		if (salePrice != null) {
			magentoCategoryRequestSet.add(new MagentoCategoryRequest(6, "107"));
		}

		return magentoCategoryRequestSet;

	}

	private MagentoCategoryRequest getMagentoCategoryRequest(String customAttributeValue, int categoryPosition,
			HttpHeaders header) {

		Categories level1 = magentoService.getCategories1(header);

		if (!customAttributeValue.equalsIgnoreCase("x")) {
			if (level1 != null) {
				if (customAttributeValue.equals(level1.getName())) {
					magentoCategoryRequest = new MagentoCategoryRequest(categoryPosition,
							String.valueOf(level1.getId()));
				} else if (level1.getChildren_data() != null) {
					Arrays.stream(level1.getChildren_data()).forEach(level2 -> {
						if (customAttributeValue.equals(level2.getName())) {
							magentoCategoryRequest = new MagentoCategoryRequest(categoryPosition,
									String.valueOf(level2.getId()));
						} else if (level2.getChildren_data() != null) {
							Arrays.stream(level2.getChildren_data()).forEach(level3 -> {
								if (customAttributeValue.equals(level3.getName())) {
									magentoCategoryRequest = new MagentoCategoryRequest(categoryPosition,
											String.valueOf(level3.getId()));
								} else if (level3.getChildren_data() != null) {
									Arrays.stream(level3.getChildren_data()).forEach(level4 -> {
										if (customAttributeValue.equals(level4.getName())) {
											magentoCategoryRequest = new MagentoCategoryRequest(categoryPosition,
													String.valueOf(level4.getId()));
										} else if (level4.getChildren_data() != null) {
											Arrays.stream(level4.getChildren_data()).forEach(level5 -> {
												if (customAttributeValue.equals(level5.getName())) {
													magentoCategoryRequest = new MagentoCategoryRequest(
															categoryPosition, String.valueOf(level5.getId()));
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
		}
		return magentoCategoryRequest;
	}

	private MagentoCustomAttributeValueRequest getMagentoDeliveyTimeCustomAttribute(int deliveryTime) {
		double dt = deliveryTime;
		double weekdays = dt / 7;
		double dtweek = weekdays * 5;

		String deliveryTimeAttributeValue;
		if (deliveryTime < 20) {
			deliveryTimeAttributeValue = "<font color=\"#009900\"><strong>ca. " + Math.round(dtweek)
					+ " Werktage Lieferzeit</strong></font>";
		} else if (deliveryTime < 40) {
			deliveryTimeAttributeValue = "<font color=\"#a3a300\"><strong>ca. " + Math.round(dtweek)
					+ " Werktage Lieferzeit</strong></font>";
		} else {
			deliveryTimeAttributeValue = "<font color=\"#990000\"><strong>ca. " + Math.round(weekdays)
					+ " Wochen Lieferzeit</strong></font>";
		}

		MagentoCustomAttributeValueRequest magentoCustomAttributeValueRequest = new MagentoCustomAttributeValueRequest(
				"ms_delivery_time", deliveryTimeAttributeValue);

		return magentoCustomAttributeValueRequest;

	}

	private String urlKeyBuilder(String url_key) {
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
