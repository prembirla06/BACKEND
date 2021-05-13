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

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maxxsoft.microServices.articleService.model.Article;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	@Override
	@Query(value = "SELECT DISTINCT a FROM Article a where a.active=true")
	List<Article> findAll();

	@Query(value = "SELECT DISTINCT a FROM Article a  where a.active=true and a.standalone=true and a.articleId > 800")
	List<Article> findAllStandalone();

	Optional<Article> findByNumber(String number);

	@Override
	@Query(value = "SELECT a FROM Article a where a.active=true and a.articleId = ?1")
	Optional<Article> findById(Long articleId);

	@Query(value = "SELECT DISTINCT a FROM Article a JOIN FETCH a.articleSetRelations b JOIN FETCH b.articleSet arse where a.active=true and arse.articleSetId = ?1")
	List<Article> findAllAssociatedArticles(Long articleSetId);

	@Query(value = "SELECT DISTINCT a FROM Article a JOIN FETCH a.articleSetRelations b where a.active=true and a.stock=0")
	List<Article> findAllByStockIsZero();
}
