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
package com.maxxsoft.microServices.articleService.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maxxsoft.microServices.articleService.model.ArticleImage;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Repository
public interface ArticleImageRepository extends JpaRepository<ArticleImage, Long> {
	@Query(nativeQuery = true, value = "SELECT * FROM maxxsoft.articleImage where articleId = ?1")
	public Set<ArticleImage> findByArticleArticleId(Long articleId);

}