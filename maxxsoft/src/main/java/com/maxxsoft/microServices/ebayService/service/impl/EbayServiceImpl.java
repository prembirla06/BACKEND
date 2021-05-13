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
package com.maxxsoft.microServices.ebayService.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.ebay.api.client.auth.oauth2.CredentialUtil;
import com.ebay.api.client.auth.oauth2.OAuth2Api;
import com.ebay.api.client.auth.oauth2.model.Environment;
import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.ebayService.model.EbayArticle;
import com.maxxsoft.microServices.ebayService.model.EbayArticleSet;
import com.maxxsoft.microServices.ebayService.model.EbayConstants;
import com.maxxsoft.microServices.ebayService.model.EbayOrder;
import com.maxxsoft.microServices.ebayService.model.Request.EbayBulkOffer;
import com.maxxsoft.microServices.ebayService.model.Request.EbayItem;
import com.maxxsoft.microServices.ebayService.model.Request.EbayOffer;
import com.maxxsoft.microServices.ebayService.model.Request.ListingPolicies;
import com.maxxsoft.microServices.ebayService.model.Request.PublishBulkOffer;
import com.maxxsoft.microServices.ebayService.model.Response.AspectResponse;
import com.maxxsoft.microServices.ebayService.model.Response.OfferResponse;
import com.maxxsoft.microServices.ebayService.repository.EbayArticleRepository;
import com.maxxsoft.microServices.ebayService.repository.EbayArticleSetRepository;
import com.maxxsoft.microServices.ebayService.service.EbayService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Service
@Slf4j
public class EbayServiceImpl implements EbayService {

	@Autowired
	private ImageService imageService;

	@Autowired
	private EbayArticleRepository ebayArticleRepository;

	@Autowired
	private EbayArticleSetRepository ebayArticleSetRepository;

	@Value("${app.ebay.sell.url}")
	private String ebaySellUrl;

	@Value("${app.ebay.buy.url}")
	private String ebayBuyUrl;

	@Value("${app.ebay.taxonomy.url}")
	private String ebayTaxonomyUrl;

	@Value("${sftp.image.access.url.articleSet}")
	private String articleSetImagePath;

	@Value("${sftp.image.access.url.article}")
	private String articleImagePath;

	@Value("${ebay.fulfillmentPolicyId}")
	private String fulfillmentPolicyId;

	@Value("${ebay.paymentPolicyId}")
	private String paymentPolicyId;

	@Value("${ebay.returnPolicyId}")
	private String returnPolicyId;

	@Value("${ebay.merchantLocationKey}")
	private String merchantLocationKey;

	private static final String INVENTORY_ITEM = "/inventory/v1/inventory_item";
	private static final String INVENTORY_OFFER = "/inventory/v1/offer";
	private static final String INVENTORY_BULK_OFFER = "/inventory/v1/bulk_create_offer";
	private static final String INVENTORY_PUBLISH_BULK_OFFER = "/inventory/v1/bulk_publish_offer";
	private static final String ALL_PRODUCT = "/rest/all/V1/products";
	private static final String LOGIN = "/rest/V1/integration/admin/token";
	private static final String CATEGORIES = "/rest/V1/categories";
	private static final String ATTRIBUTES = "/rest/V1/products/attributes";
	private static final String BROWSE_ITEM = "/browse/v1/item";
	private static final String CATEGORY_ASPECTS = "/v1/category_tree/77/get_item_aspects_for_category";

	@Bean
	public RestTemplate getRestTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(60000);
		httpRequestFactory.setConnectTimeout(60000);
		httpRequestFactory.setReadTimeout(60000);
		return new RestTemplate(httpRequestFactory);
	}

	@Value("${ebay.config}")
	public String ebayConfig;

	@Value("${ebay.environment}")
	public String environment;

	/**
	 * Get New Orders At Ebay
	 * 
	 * @param orderDate
	 * @return
	 */

	@Override
	public EbayOrder getEbayOrders(String orderDate) {
		HttpEntity<String> request = new HttpEntity<>("", getHeader());
		ResponseEntity<EbayOrder> response = getRestTemplate().exchange(EbayConstants.ORDER_URL + orderDate + "]",
				HttpMethod.GET, request, EbayOrder.class);

		return response.getBody();
	}

	/**
	 * Get Access Token
	 * 
	 * @return
	 */
	// private String getEbayToken() {
	// MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
	//
	// String auth = EbayConstants.CLIENT_ID + ":" +
	// EbayConstants.CLIENT_SECRET;
	//
	// byte[] encodedAuth =
	// Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
	//
	// String authHeader = "Basic " + new String(encodedAuth);
	//
	// System.out.println(authHeader);
	//
	// bodyMap.add("grant_type", "refresh_token");
	// bodyMap.add("scope", EbayConstants.SCOPE);
	// bodyMap.add("refresh_token", EbayConstants.REFRESH_TOKEN);
	//
	// HttpHeaders headers = new HttpHeaders();
	// headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	// headers.set("Authorization", authHeader);
	//
	// HttpEntity<MultiValueMap<String, String>> request = new
	// HttpEntity<>(bodyMap, headers);
	//
	// ResponseEntity<EbayAccessToken> response =
	// getRestTemplate().exchange(EbayConstants.OAUTH_URL, HttpMethod.POST,
	// request, EbayAccessToken.class);
	//
	// return response.getBody().getAccessToken();
	// }

	@Override
	public OAuthResponse getToken() {
		try {
			InputStream is = getClass().getResourceAsStream(ebayConfig);
			CredentialUtil.load(is);
			List<String> allScopes = new ArrayList<>();
			String[] scopess = EbayConstants.SCOPE.split(" ");
			allScopes = Arrays.stream(scopess).collect(Collectors.toList());
			OAuth2Api oauth2Api = new OAuth2Api();
			if (environment.equalsIgnoreCase("SANDBOX")) {
				return oauth2Api.getAccessToken(Environment.SANDBOX, EbayConstants.REFRESH_TOKEN_SANDBOX, allScopes);
			}
			return oauth2Api.getAccessToken(Environment.PRODUCTION, EbayConstants.REFRESH_TOKEN_PROD, allScopes);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;

	}

	@Override
	public HttpHeaders getHeader() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setContentLanguage(Locale.GERMANY);
		headers.set("Authorization", "Bearer " + getToken().getAccessToken().get().getToken());
		return headers;
	}

	@Override
	public ResponseEntity<String> getInventoryItem(String sku) {

		HttpEntity<String> request = new HttpEntity<>(getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(ebaySellUrl + INVENTORY_ITEM + "/" + sku,
					HttpMethod.GET, request, String.class);
			return response;
		} catch (final HttpClientErrorException e) {
			return new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public ResponseEntity<String> deleteInventoryItem(String sku) {

		HttpEntity<String> request = new HttpEntity<>(getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(ebaySellUrl + INVENTORY_ITEM + "/" + sku,
					HttpMethod.DELETE, request, String.class);
			return response;
		} catch (final HttpClientErrorException e) {
			return new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public ResponseEntity<String> getAllInventoryItem() {

		HttpEntity<String> request = new HttpEntity<>(getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(
					ebaySellUrl + INVENTORY_ITEM + "?limit=2&offset=0", HttpMethod.GET, request, String.class);
			return response;
		} catch (final HttpClientErrorException e) {
			return new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String createOrReplaceInventoryItem(EbayItem ebayItem, String sku) {
		HttpEntity<EbayItem> entity = new HttpEntity<EbayItem>(ebayItem, getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(ebaySellUrl + INVENTORY_ITEM + "/" + sku,
					HttpMethod.PUT, entity, String.class);
			return response.getBody();
		} catch (final HttpClientErrorException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public OfferResponse createOffer(EbayOffer ebayOffer) {
		HttpEntity<EbayOffer> entity = new HttpEntity<EbayOffer>(ebayOffer, getHeader());
		try {
			ResponseEntity<OfferResponse> response = getRestTemplate().exchange(ebaySellUrl + INVENTORY_OFFER,
					HttpMethod.POST, entity, OfferResponse.class);
			return response.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String createBulkOffer(EbayBulkOffer ebayBulkOffer) {
		HttpEntity<EbayBulkOffer> entity = new HttpEntity<EbayBulkOffer>(ebayBulkOffer, getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(ebaySellUrl + INVENTORY_BULK_OFFER,
					HttpMethod.POST, entity, String.class);
			return response.getBody();
		} catch (final HttpClientErrorException e) {
			return e.getResponseBodyAsString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<String> publishOffer(String offerId) {
		HttpEntity<String> entity = new HttpEntity<>(getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(
					ebaySellUrl + INVENTORY_OFFER + "/" + offerId + "/publish", HttpMethod.POST, entity, String.class);
			return response;
		} catch (final HttpClientErrorException e) {
			e.getResponseBodyAsString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String publishBulkOffer(PublishBulkOffer publishBulkOffer) {
		HttpEntity<PublishBulkOffer> entity = new HttpEntity<PublishBulkOffer>(publishBulkOffer, getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(ebaySellUrl + INVENTORY_PUBLISH_BULK_OFFER,
					HttpMethod.POST, entity, String.class);
			return response.getBody();
		} catch (final HttpClientErrorException e) {
			return e.getResponseBodyAsString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public ResponseEntity<String> browseItemByItemId(String itemId) {
		HttpEntity<String> request = new HttpEntity<>(getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(ebayBuyUrl + BROWSE_ITEM + "/" + itemId,
					HttpMethod.GET, request, String.class);
			return response;
		} catch (final HttpClientErrorException e) {
			return new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public AspectResponse getCategoryAspects(String category) {

		HttpEntity<String> request = new HttpEntity<>(getHeader());
		try {
			ResponseEntity<AspectResponse> response = getRestTemplate().exchange(
					ebayTaxonomyUrl + CATEGORY_ASPECTS + "?category_id=" + category, HttpMethod.GET, request,
					AspectResponse.class);
			return response.getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public String getOffers(String sku) {

		HttpEntity<String> request = new HttpEntity<>(getHeader());
		try {
			ResponseEntity<String> response = getRestTemplate().exchange(ebaySellUrl + INVENTORY_OFFER + "?sku=" + sku,
					HttpMethod.GET, request, String.class);
			return response.getBody();
		} catch (final HttpClientErrorException e) {
			return new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode()).getBody();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	@Override
	public String[] getImageUrlsForArticle(Long articleId) {
		List<String> imageUrlList = new ArrayList<>();
		imageService.findArticleImageByArticleId(articleId).forEach(image -> {
			try {
				String url = articleImagePath + articleId + "/" + image.getOrderNumber() + "_" + image.getName();
				System.out.println(url);
				imageUrlList.add(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		return imageUrlList.stream().toArray(String[]::new);

	}

	@Override
	public String[] getImageUrlsForArticleSet(Long articleSetId) {
		List<String> imageUrlList = new ArrayList<>();
		imageService.findArticleSetImageByArticleSetId(articleSetId).forEach(image -> {
			try {
				String url = articleSetImagePath + articleSetId + "/" + image.getOrderNumber() + "_" + image.getName();
				System.out.println(url);
				imageUrlList.add(url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		return imageUrlList.stream().toArray(String[]::new);

	}

	@Override
	public ListingPolicies getListingPolicies() {
		ListingPolicies listingPolicies = new ListingPolicies();
		listingPolicies.setFulfillmentPolicyId(fulfillmentPolicyId);
		listingPolicies.setPaymentPolicyId(paymentPolicyId);
		listingPolicies.setReturnPolicyId(returnPolicyId);

		return listingPolicies;

	}

	@Override
	public String getMerchantLocationKey() {
		return merchantLocationKey;
	}

	@Override
	public EbayArticle saveAndPublishEbayArticleOffer(String offerId, Long articleId, String sku) {
		ResponseEntity<String> response = publishOffer(offerId);
		if (response.getStatusCode() == HttpStatus.OK) {
			EbayArticle ebayArticle = new EbayArticle();
			ebayArticle.setArticleId(articleId);
			ebayArticle.setSku(sku);
			return ebayArticleRepository.saveAndFlush(ebayArticle);
		}
		return null;
	}

	@Override
	public EbayArticleSet saveAndPublishEbayArticleSetOffer(String offerId, Long articleSetId, String sku) {
		ResponseEntity<String> response = publishOffer(offerId);
		if (response.getStatusCode() == HttpStatus.OK) {
			EbayArticleSet ebayArticleSet = new EbayArticleSet();
			ebayArticleSet.setArticleSetId(articleSetId);
			ebayArticleSet.setSku(sku);
			return ebayArticleSetRepository.saveAndFlush(ebayArticleSet);
		}
		return null;
	}

	@Override
	public List<ResponseEntity<String>> getEbayArticle(Long articleId) {

		List<EbayArticle> ebayArticleList = ebayArticleRepository.findByArticleId(articleId);
		List<ResponseEntity<String>> inventoryItemList = new ArrayList<>();
		ebayArticleList.forEach(ebayArticle -> {
			inventoryItemList.add(getInventoryItem(ebayArticle.getSku()));
		});
		return inventoryItemList;
	}

	@Override
	public List<ResponseEntity<String>> getEbayArticleSet(Long articleSetId) {

		List<EbayArticleSet> ebayArticleSetList = ebayArticleSetRepository.findByArticleSetId(articleSetId);
		List<ResponseEntity<String>> inventoryItemList = new ArrayList<>();
		ebayArticleSetList.forEach(ebayArticleSet -> {
			inventoryItemList.add(getInventoryItem(ebayArticleSet.getSku()));
		});
		return inventoryItemList;
	}

	@Override
	public boolean isEbayArticleSkuPresent(String sku) {

		return ebayArticleRepository.findBySku(sku).isPresent();

	}

	@Override
	public boolean isEbayArticleSetSkuPresent(String sku) {

		return ebayArticleSetRepository.findBySku(sku).isPresent();

	}
}