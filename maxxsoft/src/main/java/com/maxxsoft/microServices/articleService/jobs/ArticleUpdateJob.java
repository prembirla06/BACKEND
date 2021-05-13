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

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.erpMicroServices.erpArticleService.repository.ErpArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ArticleUpdateJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private ErpArticleRepository erpArticleRepository;

	@Autowired
	private ArticleRepository articleRepository;

	// @Scheduled(fixedRate = 100000000)
	// @Scheduled(cron = "0 0 0 * * *")
	public void runArticleUpdatenJob() {
		System.out.println("start..runArticleUpdateJob...");
		articleRepository.findAll().forEach(article -> {
			if (article.isStandalone()) {
				// System.out.println(
				// "---Article Number OR SKU---" + article.getNumber() +
				// "article Id--" + article.getArticleId());
				Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(article.getNumber());
				if (artikelOptional.isPresent()) {

					Artikel artikel = artikelOptional.get();
					String userfield08 = artikel.getUserfeld08();

					if (StringUtils.isEmpty(userfield08)) {
						System.out.println("ArticleSet with empty userfield08---" + article.getNumber());
					} else {
						String[] customAttributes = userfield08.split(",");
						int length = customAttributes.length;
						if (length == 12) {
							String width = customAttributes[8];
							String height = customAttributes[9];
							String depth = customAttributes[10];
							if (!width.equalsIgnoreCase("x")) {
								article.setWidth(Double.valueOf(width));
							}
							if (!height.equalsIgnoreCase("x")) {
								article.setHeight(Double.valueOf(height));
							}
							if (!depth.equalsIgnoreCase("x")) {
								article.setDepth(Double.valueOf(depth));
							}
							articleRepository.saveAndFlush(article);
						} else {
							System.out.println("Article with length problem userfield08---" + article.getNumber());
						}
					}
				}
			}
		});
		System.out.println("end..runArticleUpdateJob...");
	}

	// @Scheduled(fixedRate = 100000000)
	// @Scheduled(cron = "0 0 0 * * *")
	public void runArticleSetUpdatenJob() {
		System.out.println("start..runArticleSetUpdateJob...");
		articleSetRepository.findAll().forEach(articleSet -> {
			// System.out.println("ArticleSet number---" +
			// articleSet.getNumber() + "article Set Id--"
			// + articleSet.getArticleSetId());
			Optional<Artikel> artikelOptional = erpArticleRepository.findByArtnum(articleSet.getNumber());
			if (artikelOptional.isPresent()) {

				Artikel artikel = artikelOptional.get();
				String userfield08 = artikel.getUserfeld08();

				if (StringUtils.isEmpty(userfield08)) {
					System.out.println("ArticleSet with empty userfield08---" + articleSet.getNumber());
				} else {
					String[] customAttributes = userfield08.split(",");
					int length = customAttributes.length;
					if (length == 12) {
						String width = customAttributes[8];
						String height = customAttributes[9];
						String depth = customAttributes[10];
						if (!width.equalsIgnoreCase("x")) {
							articleSet.setWidth(Double.valueOf(width));
						}
						if (!height.equalsIgnoreCase("x")) {
							articleSet.setHeight(Double.valueOf(height));
						}
						if (!depth.equalsIgnoreCase("x")) {
							articleSet.setDepth(Double.valueOf(depth));
						}
						articleSetRepository.saveAndFlush(articleSet);
					} else {
						System.out.println("ArticleSet with length problem userfield08---" + articleSet.getNumber());
					}
				}
			}
		});
		System.out.println("end..runArticleUpdateJob...");
	}
}
