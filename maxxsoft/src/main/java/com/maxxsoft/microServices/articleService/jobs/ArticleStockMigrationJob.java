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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maxxsoft.erpMicroServices.erpArticleService.service.ErpArticleService;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticleStockMigrationJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	// @Scheduled(fixedRate = 1000000)
	// @Scheduled(cron = "0 0 0 * * *")
	public void runStockUpdate() {

		articleRepository.findAll().forEach(article -> {
			Integer stock = erpArticleService.getStock(article.getNumber());
			if (stock != null) {
				System.out.println(article.getArticleId() + "-------------------" + stock);
				article.setStock(stock);
				articleRepository.save(article);
			}
		});

		updateArticleSetStock();
	}

	private void updateArticleSetStock() {
		articleSetRepository.findAll().forEach(articleSet -> {
			System.out.println(articleSet.getArticleSetId());
			List<Integer> stocks = new ArrayList<Integer>();
			articleSet.getArticleSetRelations().forEach(asr -> {
				System.out.println(asr.getArticle().getArticleId());
				stocks.add(asr.getArticle().getStock());
			});
			articleSet.setStock(Collections.min(stocks));
			articleSetRepository.save(articleSet);
		});
	}
}
