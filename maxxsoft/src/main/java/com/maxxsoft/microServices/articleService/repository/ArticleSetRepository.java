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

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maxxsoft.microServices.articleService.model.ArticleSet;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Repository
public interface ArticleSetRepository extends JpaRepository<ArticleSet, Long> {

	@Override
	@Query(value = "SELECT DISTINCT b FROM ArticleSet b JOIN FETCH b.articleSetRelations asr where b.active=true")
	List<ArticleSet> findAll();

	@Override
	@Query(value = "SELECT a FROM ArticleSet a where a.active=true and a.articleSetId = ?1")
	Optional<ArticleSet> findById(Long articleSetId);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM ArticleSetRelation asr where asr.articleSet= ?1")
	public void deleteAssociations(ArticleSet articleSet);

	Optional<ArticleSet> findByNumber(String number);
}
