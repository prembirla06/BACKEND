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
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticleMarkOrphanJob {

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ArticleSetRepository articleSetRepository;

	Boolean associatedWithActiveSet = false;

	// @Scheduled(fixedRate = 1000000)
	// needs to run once a week
	@Scheduled(cron ="${cronexpression.ArticleMarkOrphanJob}")
	public boolean runArticleMarkOrphanJob() {
		log.info("end..runArticleMarkOrphanJob.." + LocalDateTime.now());
		try {
			Set<Article> articleHashSet = new HashSet<>();
			articleRepository.findAll().forEach(article -> {
				if (!article.isStandalone()) {
					associatedWithActiveSet = false;
					article.getArticleSetRelations().forEach(asr -> {
						ArticleSet as = asr.getArticleSet();
						if (as.getActive()) {
							associatedWithActiveSet = true;
						}
					});
					if (!associatedWithActiveSet) {
						log.info("making orphan.....");
						if (!article.getSubstituteNumber().contains("_orphan")) {
							article.setSubstituteNumber(article.getSubstituteNumber() + "_orphan");
							articleHashSet.add(article);
						}
					}
				}
				articleRepository.saveAll(articleHashSet);
			});
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		log.info("end..runArticleMarkOrphanJob.." + LocalDateTime.now());
		return true;
	}

}
