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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
public class ArticleShortNameMigrationJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ErpArticleService erpArticleService;

	// @Scheduled(fixedRate = 100000000)
	// @Scheduled(cron = "0 0 0 * * *")
	public void runArticleShortNameMigrationJob() {
		System.out.println("start..runArticleShortNameMigrationJob...");
		articleRepository.findAll().forEach(article -> {
			String kasName = erpArticleService.getKasName(article.getNumber());
			if (!StringUtils.isEmpty(kasName)) {
				article.setShortName(kasName);
				articleRepository.save(article);
			}
		});
		articleSetRepository.findAll().forEach(articleSet -> {
			String kasName = erpArticleService.getKasName(articleSet.getNumber());
			if (!StringUtils.isEmpty(kasName)) {
				articleSet.setShortName(kasName);
				articleSetRepository.save(articleSet);
			}
		});
		System.out.println("end..runArticleShortNameMigrationJob...");
	}

}
