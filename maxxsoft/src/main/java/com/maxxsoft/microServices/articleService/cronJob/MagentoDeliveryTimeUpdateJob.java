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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.model.request.AttributeResult;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.PriceService;
import com.maxxsoft.microServices.magentoService.model.magentoResponse.MagentoProductResponse;
import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoCustomAttributeValueRequest;
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
public class MagentoDeliveryTimeUpdateJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	MagentoCustomAttributeRepository magentoCustomAttributeRepository;

	@Autowired
	private PriceService priceService;

	@Autowired
	private MagentoService magentoService;
	String AvailabilityValue = null;

	// @Scheduled(fixedRate = 10000000)
	// Automatically called from ArticleStockAndDeliveryTimeAndPreOrderUpdateJob
	public void runMagentoDeliveryTimeUpdateForArticle() {
		log.info("start..runMagentoDeliveryTimeUpdateForArticle..." + LocalDateTime.now());
		HttpHeaders headers = magentoService.getHeader();

		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				BigDecimal sellingPrice = priceService.getSellingPrice(article.getNumber(), article.getBuyPrice(), 1L);
				MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(article.getNumber(),
						headers);
				if (magentoProductResponse != null) {
					MagentoProductList magentoProductList = new MagentoProductList();
					MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
					mogentoProductRequest.setSku(magentoProductResponse.getSku());
					mogentoProductRequest.setName(article.getLongName().replaceAll("[\"/\\:]", ""));
					mogentoProductRequest.setPrice(sellingPrice);
					mogentoProductRequest.setVisibility(4);
					mogentoProductRequest.setCustom_attributes(
							getMagentoDeliveyTimeCustomAttributeValueRequestSet(article.getDeliveryTime(), headers));
					mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
					magentoProductList.setProduct(mogentoProductRequest);
					String response = magentoService.updateProductBySku(article.getNumber(), magentoProductList,
							headers);
					if (response == null) {
						log.info("No product found for Sku --" + article.getNumber());
					}
				}
			}

		});
		log.info("end..runMagentoDeliveryTimeUpdateForArticle..." + LocalDateTime.now());
	}

	public void runMagentoDeliveryTimeUpdateForArticleSet() {
		log.info("start..runMagentoDeliveryTimeUpdateForArticleSet..." + LocalDateTime.now());
		HttpHeaders headers = magentoService.getHeader();

		articleSetRepository.findAll().forEach(articleSet -> {
			BigDecimal buyPrice = priceService.getArticleSetBuyPrice(articleSet.getArticleSetId());
			BigDecimal sellingPrice = priceService.getSellingPrice(articleSet.getNumber(), buyPrice, 1L);
			MagentoProductResponse magentoProductResponse = magentoService.getProductBySKU1(articleSet.getNumber(),
					headers);
			if (magentoProductResponse != null) {
				MagentoProductList magentoProductList = new MagentoProductList();
				MagentoProductRequest mogentoProductRequest = new MagentoProductRequest();
				mogentoProductRequest.setSku(magentoProductResponse.getSku());
				mogentoProductRequest.setName(articleSet.getLongName().replaceAll("[\"/\\:]", ""));
				mogentoProductRequest.setPrice(sellingPrice);
				mogentoProductRequest.setVisibility(4);
				mogentoProductRequest.setCustom_attributes(
						getMagentoDeliveyTimeCustomAttributeValueRequestSet(articleSet.getDeliveryTime(), headers));
				mogentoProductRequest.setExtension_attributes(magentoProductResponse.getExtension_attributes());
				magentoProductList.setProduct(mogentoProductRequest);
				String response = magentoService.updateProductBySku(articleSet.getNumber(), magentoProductList,
						headers);
				if (response == null) {
					log.info("No product found for Sku --" + articleSet.getNumber());
				}
			}
		});
		log.info("end..runMagentoDeliveryTimeUpdateForArticleSet..." + LocalDateTime.now());
	}

	private Set<MagentoCustomAttributeValueRequest> getMagentoDeliveyTimeCustomAttributeValueRequestSet(
			int deliveryTime, HttpHeaders headers) {
		double dt = deliveryTime;
		double weekdays = dt / 7;
		double dtweek = weekdays * 5;
		Set<MagentoCustomAttributeValueRequest> magentoCustomAttributeValueRequestSet = new HashSet<>();
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
		magentoCustomAttributeValueRequestSet.add(magentoCustomAttributeValueRequest);

		AttributeResult[] result = magentoService.getAttributeData1("ms_availability", headers);

		if (deliveryTime <= 14) {
			Arrays.stream(result).forEach(ar -> {
				if (ar.getLabel().equals("Sofort lieferbar")) {
					AvailabilityValue = ar.getValue();
				}
			});
		} else if (deliveryTime > 14 && deliveryTime <= 30) {
			Arrays.stream(result).forEach(ar -> {
				if (ar.getLabel().equals("Bald lieferbar (2-4 Wochen)")) {
					AvailabilityValue = ar.getValue();
				}
			});
		} else {
			Arrays.stream(result).forEach(ar -> {
				if (ar.getLabel().equals("Lieferbar in mehr als 4 Wochen")) {
					AvailabilityValue = ar.getValue();
				}
			});
		}

		MagentoCustomAttributeValueRequest magentoCustomAttributeAvailablityValueRequest = new MagentoCustomAttributeValueRequest(
				"ms_availability", AvailabilityValue);
		magentoCustomAttributeValueRequestSet.add(magentoCustomAttributeAvailablityValueRequest);

		return magentoCustomAttributeValueRequestSet;

	}
}
