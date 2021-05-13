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
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.repository.CommonConfigRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticleStockAndDeliveryTimeAndPreOrderUpdateJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	@Autowired
	CommonConfigRepository commonConfigRepository;

	@Autowired
	private MagentoDeliveryTimeUpdateJob magentoDeliveryTimeUpdateJob;

	// @Scheduled(fixedRate = 10000000)
	// Every hour
	@Scheduled(cron = "${cronexpression.ArticleStockAndDeliveryTimeAndPreOrderUpdateJob}")
	public boolean runStockAndDeliveryTimeAndPreOrderUpdate() {
		log.info(
				"Job started to update Stock, Delivery Time and Preorder Information for all article and article Sets at --"
						+ LocalDateTime.now());
		try {
			int defaultDeliveryTime = Integer
					.valueOf(commonConfigRepository.findByConfigKey("Default_Delivery_Time").getValue());
			articleRepository.findAll().forEach(article -> {
				int preOrder = 0;
				Optional<Artikel> erpArtikel = erpArticleService.getArtikelByNumber(article.getNumber());
				if (erpArtikel.isPresent()) {
					BigDecimal erpBuyPrice = erpArtikel.get().getEkPreis();
					preOrder = erpArtikel.get().getShopSort();
					article.setBuyPrice(erpBuyPrice);
				}
				int stock = erpArticleService.getStock(article.getNumber());
				int actualStock = stock - preOrder;

				article.setStock(stock);
				article.setPreOrder(preOrder);
				if (actualStock > 0) {
					article.setDeliveryTime(defaultDeliveryTime);
				} else {
					List<Object[]> artikelEkbestelList = erpArticleService.findAllArtikelEkbestels(article.getNumber());
					// sorting of list by next delivery asc needed?
					int[] actualStockAfterNextDelivery = new int[1];
					if (artikelEkbestelList.size() > 0) {
						artikelEkbestelList.forEach(artikelEkbestel -> {
							actualStockAfterNextDelivery[0] = actualStock
									+ ((BigDecimal) artikelEkbestel[0]).intValue();
							if (actualStockAfterNextDelivery[0] <= 0) {
								article.setDeliveryTime(63);
							} else {
								long nextStockDelivery = ChronoUnit.DAYS.between(LocalDate.now(),
										((Date) artikelEkbestel[1]).toLocalDate());
								if (nextStockDelivery <= 0) {
									article.setDeliveryTime(defaultDeliveryTime);
								} else if (nextStockDelivery + defaultDeliveryTime <= 70) {
									article.setDeliveryTime((int) nextStockDelivery + defaultDeliveryTime);
								} else {
									article.setDeliveryTime(70);
								}
							}
						});
					} else {
						article.setDeliveryTime(63);
					}
				}
				articleRepository.save(article);
			});
			updateStockAndArticleSetDeliveryTimeAndPreOrder();
			log.info(
					"Job ended to update Stock, Delivery Time and Preorder Information for all article and article Sets at---"
							+ LocalDateTime.now());
			log.info("Pushing Delivery time to Magento");
			magentoDeliveryTimeUpdateJob.runMagentoDeliveryTimeUpdateForArticle();
			magentoDeliveryTimeUpdateJob.runMagentoDeliveryTimeUpdateForArticleSet();
			log.info("Delivery time has been pushed to Magento at--" + LocalDateTime.now());

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void updateStockAndArticleSetDeliveryTimeAndPreOrder() {
		articleSetRepository.findAll().forEach(articleSet -> {
			List<Integer> stocks = new ArrayList<Integer>();
			List<Integer> deliveryTimeList = new ArrayList<Integer>();
			int preOrder = erpArticleService.getPreOrder(articleSet.getNumber());
			articleSet.setPreOrder(preOrder);
			articleSet.getArticleSetRelations().forEach(asr -> {
				deliveryTimeList.add(asr.getArticle().getDeliveryTime());
				stocks.add(asr.getArticle().getStock() - asr.getArticle().getPreOrder());
			});
			articleSet.setDeliveryTime(Collections.max(deliveryTimeList));
			articleSet.setStock(Collections.min(stocks));
			articleSetRepository.save(articleSet);
		});
	}
}
