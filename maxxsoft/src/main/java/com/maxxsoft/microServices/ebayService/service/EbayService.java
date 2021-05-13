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
package com.maxxsoft.microServices.ebayService.service;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.ebay.api.client.auth.oauth2.model.OAuthResponse;
import com.maxxsoft.microServices.ebayService.model.EbayArticle;
import com.maxxsoft.microServices.ebayService.model.EbayArticleSet;
import com.maxxsoft.microServices.ebayService.model.EbayOrder;
import com.maxxsoft.microServices.ebayService.model.Request.EbayBulkOffer;
import com.maxxsoft.microServices.ebayService.model.Request.EbayItem;
import com.maxxsoft.microServices.ebayService.model.Request.EbayOffer;
import com.maxxsoft.microServices.ebayService.model.Request.ListingPolicies;
import com.maxxsoft.microServices.ebayService.model.Request.PublishBulkOffer;
import com.maxxsoft.microServices.ebayService.model.Response.AspectResponse;
import com.maxxsoft.microServices.ebayService.model.Response.OfferResponse;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public interface EbayService {

	public EbayOrder getEbayOrders(String orderDate);

	public OAuthResponse getToken();

	public ResponseEntity<String> getInventoryItem(String sku);

	public ResponseEntity<String> deleteInventoryItem(String sku);

	public ResponseEntity<String> getAllInventoryItem();

	public String createOrReplaceInventoryItem(EbayItem ebayItem, String sku);

	public HttpHeaders getHeader();

	public OfferResponse createOffer(EbayOffer ebayOffer);

	public ResponseEntity<String> publishOffer(String offerId);

	public String publishBulkOffer(PublishBulkOffer publishBulkOffer);

	public String createBulkOffer(EbayBulkOffer ebayBulkOffer);

	public ResponseEntity<String> browseItemByItemId(String itemId);

	public AspectResponse getCategoryAspects(String category);

	public String getOffers(String sku);

	public String[] getImageUrlsForArticle(Long articleId);

	public String[] getImageUrlsForArticleSet(Long articleSetId);

	public ListingPolicies getListingPolicies();

	public String getMerchantLocationKey();

	public EbayArticleSet saveAndPublishEbayArticleSetOffer(String offerId, Long articleSetId, String sku);

	public EbayArticle saveAndPublishEbayArticleOffer(String offerId, Long articleId, String sku);

	public List<ResponseEntity<String>> getEbayArticle(Long articleId);

	public List<ResponseEntity<String>> getEbayArticleSet(Long articleSetId);

	public boolean isEbayArticleSkuPresent(String sku);

	public boolean isEbayArticleSetSkuPresent(String sku);

}