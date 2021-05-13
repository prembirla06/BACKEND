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
package com.maxxsoft.microServices.articleService.service;

import java.util.Optional;
import java.util.Set;

import com.maxxsoft.microServices.articleService.model.ArticleImage;
import com.maxxsoft.microServices.articleService.model.ArticleSetImage;
import com.maxxsoft.microServices.articleService.model.request.ImageRequest;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public interface ImageService {

	void addArticleImage(Long articleId, ImageRequest imageRequest);

	void addArticleSetImage(Long articleSetId, ImageRequest imageRequest);

	void updateArticleImage(Long articleImageId, ImageRequest imageRequest, String articleNumber);

	void updateArticleSetImage(Long articleSetImageId, ImageRequest imageRequest, String articleNumber);

	void deleteArticleSetImage(ArticleSetImage articleSetImage, String articleNumber);

	Optional<ArticleImage> findArticleImageById(Long articleImageId);

	Optional<ArticleSetImage> findArticleSetImageById(Long articleSetImageId);

	Set<ArticleImage> findArticleImageByArticleId(Long articleId);

	Set<ArticleSetImage> findArticleSetImageByArticleSetId(Long articleSetId);

	void deleteArticleImage(ArticleImage articleImage, String articleNumber);

	Boolean isOrderNumberExists(Long articleId, int order);

	Boolean isOrderNumberExistsSet(Long articleSetId, int order);

	public void pushImageToMagento(ArticleImage image, String articleShortName, String sku);

	public void pushImageToMagentoForSet(ArticleSetImage image, String articleShortName, String sku);

}