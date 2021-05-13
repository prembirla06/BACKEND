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
package com.maxxsoft.microServices.ebayService.jobs;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.CommonConfigRepository;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.ebayService.model.Request.Aspect;
import com.maxxsoft.microServices.ebayService.model.Request.Dimensions;
import com.maxxsoft.microServices.ebayService.model.Request.EbayItem;
import com.maxxsoft.microServices.ebayService.model.Request.EbayOffer;
import com.maxxsoft.microServices.ebayService.model.Request.ListingPolicies;
import com.maxxsoft.microServices.ebayService.model.Request.PackageWeightAndSize;
import com.maxxsoft.microServices.ebayService.model.Request.Price;
import com.maxxsoft.microServices.ebayService.model.Request.PricingSummary;
import com.maxxsoft.microServices.ebayService.model.Request.Product;
import com.maxxsoft.microServices.ebayService.model.Request.Tax;
import com.maxxsoft.microServices.ebayService.model.Request.Weight;
import com.maxxsoft.microServices.ebayService.model.Response.AspectResponse;
import com.maxxsoft.microServices.ebayService.model.Response.OfferResponse;
import com.maxxsoft.microServices.ebayService.service.EbayService;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoCategoryRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class EbayProductMigrationJob {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private EbayService ebayService;

	@Autowired
	private PriceService priceService;
	@Autowired
	CommonConfigRepository commonConfigRepository;

	MagentoCategoryRequest magentoCategoryRequest;

	@Value("${sftp.image.access.url.article}")
	private String articleImagePath;

	@Value("${sftp.image.access.url.articleSet}")
	private String articleSetImagePath;

	@Value("${ebay.fulfillmentPolicyId}")
	private String fulfillmentPolicyId;
	@Value("${ebay.paymentPolicyId}")
	private String paymentPolicyId;
	@Value("${ebay.returnPolicyId}")
	private String returnPolicyId;
	@Value("${ebay.merchantLocationKey}")
	private String merchantLocationKey;

	// private String[] getImageUrls(Long articleId) {
	// List<String> imageUrlList = new ArrayList<>();
	// imageService.findArticleImageByArticleId(articleId).forEach(image -> {
	// try {
	// String url = articleImagePath + articleId + "/" + image.getOrderNumber()
	// + "_" + image.getName();
	// System.out.println(url);
	// imageUrlList.add(url);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// });
	//
	// return imageUrlList.stream().toArray(String[]::new);
	//
	// }

	// @Scheduled(fixedDelay = 100000000)
	public void runEbayCreateInventoryArticle() {
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone() && article.getArticleId() < 2) {
				System.out.println(
						"---Article Number OR SKU---" + article.getNumber() + "article Id--" + article.getArticleId());
				EbayItem ebayItem = new EbayItem();
				Product product = new Product();
				product.setTitle(article.getShortName());
				product.setDescription(article.getShortDescription()); // long
																		// desc
				product.setImageUrls(ebayService.getImageUrlsForArticle(article.getArticleId()));
				ebayItem.setLocale("de_DE");
				ebayItem.setCondition("NEW");
				PackageWeightAndSize packageWeightAndSize = new PackageWeightAndSize();
				Dimensions dimensions = new Dimensions();
				dimensions.setHeight(article.getHeight());
				dimensions.setLength(article.getDepth());
				dimensions.setWidth(article.getWidth());
				dimensions.setUnit("CENTIMETER");
				packageWeightAndSize.setDimensions(dimensions);
				Weight weight = new Weight();
				weight.setValue(20.10);
				weight.setUnit("KILOGRAM");
				packageWeightAndSize.setWeight(weight);
				ebayItem.setPackageWeightAndSize(packageWeightAndSize);
				// Availability availability = new Availability();
				// ShipToLocationAvailability shipToLocationAvailability = new
				// ShipToLocationAvailability();
				// shipToLocationAvailability.setQuantity(1);
				// availability.setShipToLocationAvailability(shipToLocationAvailability);
				// ebayItem.setAvailability(availability);
				// product.setBrand("von moebel-guenstig24.de");
				String[] testValue = { "testValue" };
				HashMap<String, String[]> aspectsMap = new HashMap<String, String[]>();
				AspectResponse aspectResponse = ebayService.getCategoryAspects("88057");
				aspectResponse.getAspects().forEach(aspect -> {
					if (aspect.getAspectConstraint().isAspectRequired()) {
						Aspect newAspect = new Aspect();
						aspectsMap.put(aspect.getLocalizedAspectName(), testValue);
					}
				});
				// String[] brand = { "MaxxSoft-New" };
				// aspects.setMarke(brand);
				// String[] type = { "Furniture" };
				// aspects.setProduktart(type);
				// String[] material = { "Wood" };
				// aspects.setMaterial(material);
				// String[] Farbe = { "Green" };
				// aspects.setFarbe(Farbe);
				// String[] Breite = { "Breite" };
				// aspects.setBreite(Breite);
				// aspects.setHöhe(höhe);
				product.setAspects(aspectsMap);
				ebayItem.setProduct(product);
				String response = ebayService.createOrReplaceInventoryItem(ebayItem, article.getNumber());
				System.out.println(response);
				BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(), 2L);
				String offerId = runEbayCreateOffer(article.getNumber(), article.getShortName(), sellingPrice);
				System.out.println("offerId----------" + offerId);
				String offerpublishResponse = ebayService.publishOffer(offerId).getBody();
				System.out.println(offerpublishResponse);
				System.out.println("offer publish");
			}
		});
		System.out.println("---------finished--------------");
	}

	public void runEbayCreateInventoryArticleSet() {
		articleSetRepository.findAll().forEach(articleSet -> {
			if (articleSet.getArticleSetId() > 5) {
				System.out.println("---Article Number OR SKU---" + articleSet.getNumber() + "article Id--"
						+ articleSet.getArticleSetId());
				EbayItem ebayItem = new EbayItem();
				Product product = new Product();
				product.setTitle(articleSet.getShortName());
				product.setDescription(articleSet.getShortDescription()); // long
																			// desc
				product.setBrand("TechMaxx");
				product.setImageUrls(ebayService.getImageUrlsForArticleSet(articleSet.getArticleSetId()));
				ebayItem.setProduct(product);
				ebayItem.setLocale("de_DE");
				ebayItem.setCondition("NEW");
				PackageWeightAndSize packageWeightAndSize = new PackageWeightAndSize();
				Dimensions dimensions = new Dimensions();
				dimensions.setHeight(articleSet.getHeight());
				dimensions.setLength(articleSet.getDepth());
				dimensions.setWidth(articleSet.getWidth());
				dimensions.setUnit("CENTIMETER");
				packageWeightAndSize.setDimensions(dimensions);
				Weight weight = new Weight();
				weight.setValue(articleSet.getTotalWeight());
				weight.setUnit("KILOGRAM");
				packageWeightAndSize.setWeight(weight);
				ebayItem.setPackageWeightAndSize(packageWeightAndSize);
				// Availability availability = new Availability();
				// ShipToLocationAvailability shipToLocationAvailability = new
				// ShipToLocationAvailability();
				// shipToLocationAvailability.setQuantity(1);
				// availability.setShipToLocationAvailability(shipToLocationAvailability);
				// ebayItem.setAvailability(availability);
				String[] testValue = { "testValue" };
				HashMap<String, String[]> aspectsMap = new HashMap<String, String[]>();
				AspectResponse aspectResponse = ebayService.getCategoryAspects("88057");
				aspectResponse.getAspects().forEach(aspect -> {
					if (aspect.getAspectConstraint().isAspectRequired()) {
						Aspect newAspect = new Aspect();
						aspectsMap.put(aspect.getLocalizedAspectName(), testValue);
					}
				});
				product.setAspects(aspectsMap);
				String response = ebayService.createOrReplaceInventoryItem(ebayItem, articleSet.getNumber());
				System.out.println(response);
				BigDecimal buyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
				BigDecimal sellingPrice = priceService.getSellingPrice(articleSet.getNumber(), buyPrice, 2L);
				String offerId = runEbayCreateOffer(articleSet.getNumber(), articleSet.getShortName(), sellingPrice);
				System.out.println("offerId----------" + offerId);
				String offerpublishResponse = ebayService.publishOffer(offerId).getBody();
				System.out.println(offerpublishResponse);
				System.out.println("offer publish for Set");
			}
		});
		System.out.println("---------finished--------------");
	}

	private String runEbayCreateOffer(String sku, String articleShortName, BigDecimal sellingPrice) {

		ebayService.getInventoryItem(sku);
		EbayOffer ebayOffer = new EbayOffer();
		ebayOffer.setSku(sku);
		ebayOffer.setMarketplaceId("EBAY_DE");
		ebayOffer.setMerchantLocationKey(merchantLocationKey);
		ebayOffer.setFormat("FIXED_PRICE");
		ebayOffer.setAvailableQuantity(1);
		ebayOffer.setCategoryId("88057"); // TBD
		ebayOffer.setListingDescription("TechMaxx test listingg desc");// long
		ListingPolicies listingPolicies = new ListingPolicies();
		listingPolicies.setFulfillmentPolicyId(fulfillmentPolicyId);
		listingPolicies.setPaymentPolicyId(paymentPolicyId);
		listingPolicies.setReturnPolicyId(returnPolicyId);
		ebayOffer.setListingPolicies(listingPolicies);
		PricingSummary pricingSummary = new PricingSummary();
		Price price = new Price();
		price.setCurrency("EUR");
		price.setValue(sellingPrice.toString());
		pricingSummary.setPrice(price);
		ebayOffer.setPricingSummary(pricingSummary);
		Tax tax = new Tax();
		tax.setVatPercentage(Double.parseDouble(commonConfigRepository.findByConfigKey("VAT_Percentage").getValue()));
		tax.setThirdPartyTaxCategory("Furniture");
		tax.setApplyTax(true);
		ebayOffer.setTax(tax);
		System.out.println(ebayOffer);
		OfferResponse offerResponse = ebayService.createOffer(ebayOffer);
		return offerResponse.getOfferId();
	}

}
