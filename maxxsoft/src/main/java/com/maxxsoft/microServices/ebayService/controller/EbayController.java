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
package com.maxxsoft.microServices.ebayService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
import com.maxxsoft.microServices.ebayService.service.EbayService;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/ebay")
@Transactional
public class EbayController {

	@Autowired
	private EbayService ebayService;

	@GetMapping(value = "/getOrder/{orderDate}")
	public EbayOrder getToken(@PathVariable String orderDate) {
		return ebayService.getEbayOrders(orderDate);
	}

	@GetMapping(value = "/getToken")
	public OAuthResponse getToken() {
		return ebayService.getToken();
	}

	@GetMapping(value = "/getInventoryItem/{sku}")
	public String getInventoryItem(@PathVariable String sku) {
		return ebayService.getInventoryItem(sku).getBody();
	}

	@GetMapping(value = "/isEbayArticleSkuPresent/{sku}")
	public boolean isEbayArticleSkuPresent(@PathVariable String sku) {
		return ebayService.isEbayArticleSkuPresent(sku);
	}

	@GetMapping(value = "/isEbayArticleSetSkuPresent/{sku}")
	public boolean isEbayArticleSetSkuPresent(@PathVariable String sku) {
		return ebayService.isEbayArticleSetSkuPresent(sku);
	}

	@GetMapping(value = "/getEbayListedArticle/{articleId}")
	public List<ResponseEntity<String>> getArticle(@PathVariable Long articleId) {
		return ebayService.getEbayArticle(articleId);
	}

	@GetMapping(value = "/getEbayListedArticleSet/{articleSetId}")
	public List<ResponseEntity<String>> getArticleSet(@PathVariable Long articleSetId) {
		return ebayService.getEbayArticleSet(articleSetId);
	}

	@DeleteMapping(value = "/deleteInventoryItem/{sku}")
	public ResponseEntity<String> deleteInventoryItem(@PathVariable String sku) {
		return ebayService.deleteInventoryItem(sku);
	}

	@GetMapping(value = "/geItemByItemId/{itemId}")
	public ResponseEntity<String> geItemByItemId(@PathVariable String itemId) {
		return ebayService.browseItemByItemId(itemId);
	}

	@GetMapping(value = "/getAllInventoryItem")
	public ResponseEntity<String> getAllInventoryItem() {
		return ebayService.getAllInventoryItem();
	}

	@PutMapping(value = "/createOrReplaceInventoryItem/{sku}")
	public String createOrReplaceInventoryItem(@PathVariable String sku, @RequestBody EbayItem ebayItem) {
		return ebayService.createOrReplaceInventoryItem(ebayItem, sku);
	}

	@PostMapping(value = "/saveAndPublishEbayArticleOffer/{offerId}/{articleId}/{sku}")
	public EbayArticle saveAndPublishEbayArticleOffer(@PathVariable Long articleId, @PathVariable String sku,
			@PathVariable String offerId) {
		return ebayService.saveAndPublishEbayArticleOffer(offerId, articleId, sku);
	}

	@PostMapping(value = "/saveAndPublishEbayArticleSetOffer/{offerId}/{articleSetId}/{sku}")
	public EbayArticleSet saveAndPublishEbayArticleSetOffer(@PathVariable Long articleSetId, @PathVariable String sku,
			@PathVariable String offerId) {
		return ebayService.saveAndPublishEbayArticleSetOffer(offerId, articleSetId, sku);
	}

	@PostMapping(value = "/createOffer")
	public OfferResponse createOffer(@RequestBody EbayOffer ebayOffer) {
		return ebayService.createOffer(ebayOffer);
	}

	@PostMapping(value = "/createBulkOffer")
	public String createOffer(@RequestBody EbayBulkOffer ebayBulkOffer) {
		return ebayService.createBulkOffer(ebayBulkOffer);
	}

	@PostMapping(value = "/publishOffer/{offerId}")
	public String publishOffer(@PathVariable String offerId) {
		return ebayService.publishOffer(offerId).getBody();
	}

	@PostMapping(value = "/publishBulkOffer")
	public String publishBulkOffer(@RequestBody PublishBulkOffer publishBulkOffer) {
		return ebayService.publishBulkOffer(publishBulkOffer);
	}

	@GetMapping(value = "/getCategoryAspects/{category}")
	public AspectResponse getCategoryAspects(@PathVariable String category) {
		return ebayService.getCategoryAspects(category);
	}

	@GetMapping(value = "/getOffersBySku/{sku}")
	public String getOffersBySku(@PathVariable String sku) {
		return ebayService.getOffers(sku);
	}

	@GetMapping(value = "/getImageUrlsForArticle/{articleId}")
	public String[] getImageUrlsForArticle(@PathVariable Long articleId) {
		return ebayService.getImageUrlsForArticle(articleId);
	}

	@GetMapping(value = "/getImageUrlsForArticleSet/{articleSetId}")
	public String[] getImageUrlsForArticleSet(@PathVariable Long articleSetId) {
		return ebayService.getImageUrlsForArticleSet(articleSetId);
	}

	@GetMapping(value = "/getListingPolicies")
	public ListingPolicies getPolicies() {
		return ebayService.getListingPolicies();
	}

	@GetMapping(value = "/getMerchantLocationKey")
	public String getMerchantLocationKey() {
		return ebayService.getMerchantLocationKey();
	}

}