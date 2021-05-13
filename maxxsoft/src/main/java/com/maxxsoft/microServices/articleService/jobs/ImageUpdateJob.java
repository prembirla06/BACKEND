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

import com.maxxsoft.microServices.articleService.model.request.ImageRequest;
import com.maxxsoft.microServices.articleService.repository.ArticleRepository;
import com.maxxsoft.microServices.articleService.repository.ArticleSetRepository;
import com.maxxsoft.microServices.articleService.service.ImageService;
import com.maxxsoft.microServices.magentoService.repository.MagentoMediaRepository;
import com.maxxsoft.microServices.magentoService.service.MagentoService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Slf4j
@Component
public class ImageUpdateJob {

	@Autowired
	private ArticleSetRepository articleSetRepository;

	@Autowired
	private MagentoMediaRepository magentoMediaRepository;

	@Autowired
	private ArticleRepository articleRepository;

	@Autowired
	private ImageService imageService;

	@Autowired
	private MagentoService magentoService;

	// Job is not important now. because at the time of migration, we use
	// shortname as image name.
	// @Scheduled(fixedRate = 10000000)
	public void runImageUpdateJob() {
		System.out.println("start...ImageUpdateJob..");
		articleRepository.findAll().forEach(article -> {
			System.out.println(article.getArticleId());
			if (article.isStandalone()) {
				imageService.findArticleImageByArticleId(article.getArticleId()).forEach(image -> {
					int position = image.getOrderNumber();

					ImageRequest imageRequest = new ImageRequest();
					imageRequest.setName(image.getName() + "_" + position);
					imageRequest.setOrder(position);
					imageService.updateArticleImage(image.getArticleImageId(), imageRequest, article.getNumber());

				});
			}
		});
		articleSetRepository.findAll().forEach(articleSet -> {
			System.out.println(articleSet.getArticleSetId());
			imageService.findArticleSetImageByArticleSetId(articleSet.getArticleSetId()).forEach(image -> {
				int position = image.getOrderNumber();

				ImageRequest imageRequest = new ImageRequest();
				imageRequest.setName(image.getName() + "_" + position);
				imageRequest.setOrder(position);
				imageService.updateArticleSetImage(image.getArticleSetImageId(), imageRequest, articleSet.getNumber());

			});

		});
		System.out.println("end.....");
	}

}