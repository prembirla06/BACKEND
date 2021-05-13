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

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.ArticleService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class UpdateTotalWeightJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleService articleService;

	// once a day
	@Scheduled(cron ="${cronexpression.UpdateTotalWeightJob}")
	public boolean runUpdateTotalWeightJob() {
		log.info("start..UpdateTotalWeightJob..." + LocalDateTime.now());
		try {
			articleRepository.findAll().forEach(article -> {
				Double totalWeight = articleService.getTotalArticleWeight(article.getArticleId());
				article.setTotalWeight(totalWeight);
				articleRepository.save(article);
			});

			articleSetRepository.findAll().forEach(articleSet -> {
				Double totalWeight = articleService.getTotalArticleSetWeight(articleSet.getArticleSetId());
				articleSet.setTotalWeight(totalWeight);
				articleSetRepository.save(articleSet);
			});

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..UpdateTotalWeightJob..." + LocalDateTime.now());
		return true;
	}
}
