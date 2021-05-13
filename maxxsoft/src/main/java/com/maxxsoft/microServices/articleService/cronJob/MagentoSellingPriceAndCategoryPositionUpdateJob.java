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
package com.maxxsoft.microServices.articleService.cronJob;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoCategoryRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoCustomAttributeValueRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoPriceRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductList;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoProductRequest;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoSpecialPriceRequest;
import com.maxxsoft.microServices.magentoService.repository.MagentoCustomAttributeRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class MagentoSellingPriceAndCategoryPositionUpdateJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private PriceService priceService;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	MagentoCustomAttributeRepository magentoCustomAttributeRepository;

	@Autowired
	private MagentoService magentoService;

	int categoryPosition = 0;
	boolean saleCat;

	// @Scheduled(fixedRate = 10000000)
	// Every hour
	@Scheduled(cron = "${cronexpression.MagentoSellingPriceAndCategoryPositionUpdateJob}")
	public boolean runMagentoSellingPriceUpdateJobForArticles() {
		log.info("start..MagentoSellingPriceAndCategoryPositionUpdateJob...for article..." + LocalDateTime.now());
		HttpHeaders headers = magentoService.getHeader();
		try {
			articleRepository.findAll().forEach(article -> {
				if (article.isStandalone()) {
					saleCat = false;
					// System.out.println(article.getNumber());

					BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(),
							1L);
					int actualStock = article.getStock() - article.getPreOrder();
					BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(article.getNumber(),
							article.getBuyPrice(), 1L);
					BigDecimal salePrice = priceService.getDirectSalePrice(article.getNumber(), actualStock,
							minimumSellingPrice);
					Optional<Artikel> artikelOptional = erpArticleService.getArtikelByNumber(article.getNumber());
					if (artikelOptional.isPresent()) {

						Artikel artikel = artikelOptional.get();
						categoryPosition = -(erpArticleService.getOrderLastxDays(artikel.getRecId(), 60) + actualStock);

					}
					MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(article.getNumber(),
							headers);
					MagentoProductList magentoProductList = new MagentoProductList();
					MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
					mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
					mogentoProductRequest.getExtension_attributes().getCategory_links().forEach(cat -> {
						cat.setPosition(categoryPosition);
						if (cat.getCategory_id().equals("107")) {
							saleCat = true;
						}
					});
					if (salePrice != null) {
						mogentoProductRequest.setCustom_attributes(getMagentoSpecialRequestSet(salePrice));
						if (!saleCat) {
							magentoProductResponse.getExtension_attributes().getCategory_links()
									.add(new MagentoCategoryRequest(categoryPosition, "107"));
							magentoService.addCategoryToLocalMagentoProduct(article.getNumber(), categoryPosition,
									"107", "% SALE %");
						}
					} else if (saleCat) {
						mogentoProductRequest.getExtension_attributes().getCategory_links()
								.remove(new MagentoCategoryRequest(categoryPosition, "107"));
						magentoService.deleteMagentoProductFromCategory(article.getNumber(), 107, headers);
						magentoService.deleteCategoryFromLocalMagentoProduct(article.getNumber(), 107);
						deleteSpecialPrice(article.getNumber(), headers);
					}
					mogentoProductRequest.setSku(magentoProductResponse.getSku());
					mogentoProductRequest.setName(article.getLongName().replaceAll("[\"/\\:]", ""));
					mogentoProductRequest.setPrice(sellingPrice);
					mogentoProductRequest.setVisibility(4);
					magentoProductList.setProduct(mogentoProductRequest);

					String response = magentoService.updateProductBySku(magentoProductResponse.getSku(),
							magentoProductList, headers);
					if (response == null) {
						log.error("No product found for Sku --" + article.getNumber());

					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..MagentoSellingPriceAndCategoryPositionUpdateJob...for article..." + LocalDateTime.now());
		return true;
	}

	// @Scheduled(fixedRate = 10000000)
	// Every hour
	// @Scheduled(cron = "0 0 */1 ? * *")
	@Scheduled(cron = "${cronexpression.MagentoSellingPriceAndCategoryPositionUpdateJob}")
	public boolean runMagentoSellingPriceForArticleSet() {
		log.info("start..MagentoSellingPriceAndCategoryPositionUpdateJob...for articleSet..." + LocalDateTime.now());
		HttpHeaders headers = magentoService.getHeader();
		try {
			articleSetRepository.findAll().forEach(articleSet -> {
				// if (articleSet.getArticleSetId() == 10) {
				saleCat = false;
				BigDecimal buyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());

				BigDecimal sellingPrice = priceService.getSellingPrice(articleSet.getNumber(), buyPrice, 1L);
				// Saving actual stock in stock. This value is equal to the
				// mimimum
				// actual stock among all the partlist
				int actualStock = articleSet.getStock();
				BigDecimal minimumSellingPrice = priceService.getMinimumSellingPrice(articleSet.getNumber(), buyPrice,
						1L);
				BigDecimal salePrice = priceService.getDirectSalePrice(articleSet.getNumber(), actualStock,
						minimumSellingPrice);
				Optional<Artikel> artikelOptional = erpArticleService.getArtikelByNumber(articleSet.getNumber());
				if (artikelOptional.isPresent()) {

					Artikel artikel = artikelOptional.get();
					categoryPosition = -(erpArticleService.getOrderLastxDays(artikel.getRecId(), 60) + actualStock);

				}
				MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(articleSet.getNumber(),
						headers);
				if (magentoProductResponse != null) {
					MagentoProductList magentoProductList = new MagentoProductList();
					MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
					mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
					mogentoProductRequest.getExtension_attributes().getCategory_links().forEach(cat -> {
						cat.setPosition(categoryPosition);
						if (cat.getCategory_id().equals("107")) {
							saleCat = true;
						}
					});
					if (salePrice != null) {
						mogentoProductRequest.setCustom_attributes(getMagentoSpecialRequestSet(salePrice));
						if (!saleCat) {
							magentoProductResponse.getExtension_attributes().getCategory_links()
									.add(new MagentoCategoryRequest(categoryPosition, "107"));
							magentoService.addCategoryToLocalMagentoProduct(articleSet.getNumber(), categoryPosition,
									"107", "% SALE %");
						}
					} else if (saleCat) {
						mogentoProductRequest.getExtension_attributes().getCategory_links()
								.remove(new MagentoCategoryRequest(categoryPosition, "107"));
						magentoService.deleteMagentoProductFromCategory(articleSet.getNumber(), 107, headers);
						magentoService.deleteCategoryFromLocalMagentoProduct(articleSet.getNumber(), 107);
						deleteSpecialPrice(articleSet.getNumber(), headers);
					}

					mogentoProductRequest.setSku(magentoProductResponse.getSku());
					mogentoProductRequest.setName(articleSet.getLongName().replaceAll("[\"/\\:]", ""));
					mogentoProductRequest.setPrice(sellingPrice);
					mogentoProductRequest.setVisibility(4);
					magentoProductList.setProduct(mogentoProductRequest);

					String response = magentoService.updateProductBySku(magentoProductResponse.getSku(),
							magentoProductList, headers);
					if (response == null) {
						log.error("No product found for Sku --" + articleSet.getNumber());

					}
				}
				// }
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..MagentoSellingPriceAndCategoryPositionUpdateJob...for articleSet..." + LocalDateTime.now());
		return true;
	}

	private Set<MagentoCustomAttributeValueRequest> getMagentoSpecialRequestSet(BigDecimal salePrice) {
		Set<MagentoCustomAttributeValueRequest> magentoCustomAttributeValueRequestSet = new HashSet<>();
		MagentoCustomAttributeValueRequest specialPriceAttribute = new MagentoCustomAttributeValueRequest(
				"special_price", salePrice.toString());
		MagentoCustomAttributeValueRequest specialPriceTypeAttribute = new MagentoCustomAttributeValueRequest(
				"ms_special_price_type", "Sale Price");
		magentoCustomAttributeValueRequestSet.add(specialPriceAttribute);
		magentoCustomAttributeValueRequestSet.add(specialPriceTypeAttribute);
		return magentoCustomAttributeValueRequestSet;

	}

	private void deleteSpecialPrice(String sku, HttpHeaders headers) {
		List<MagentoSpecialPriceRequest> magentoSpecialPriceRequestList = new ArrayList<>();
		MagentoSpecialPriceRequest magentoSpecialPriceRequest = new MagentoSpecialPriceRequest(0, 0, sku);
		magentoSpecialPriceRequestList.add(magentoSpecialPriceRequest);
		MagentoPriceRequest magentoPriceRequest = new MagentoPriceRequest();
		magentoPriceRequest.setPrices(magentoSpecialPriceRequestList);
		magentoService.deleteSpecialPrice(magentoPriceRequest, headers);

	}
}
