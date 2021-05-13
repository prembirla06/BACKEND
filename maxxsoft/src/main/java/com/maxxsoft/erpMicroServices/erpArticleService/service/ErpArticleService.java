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
package com.maxxsoft.erpMicroServices.erpArticleService.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.maxxsoft.erpMicroServices.erpArticleService.model.Artikel;
import com.maxxsoft.microServices.articleService.model.Article;
import com.maxxsoft.microServices.articleService.model.ArticleSet;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public interface ErpArticleService {

	public void pushArticle(Article article);

	public void updateArticle(Article article);

	public void pushArticleSet(ArticleSet articleSet);

	public void updateArticleSet(ArticleSet articleSet);

	public Integer getStock(String articleNumber);

	public Integer getPreOrder(String articleNumber);

	public String getDimension(String articleNumber);

	public List<Object[]> findAllArtikelEkbestels(String articleNumber);
	
	public List<Object[]> findAllArtikelEkbestelsStrict(String articleNumber);

	public Set<Artikel> getAllArtikelByType(String articleType);

	public Optional<Artikel> getArtikelByNumber(String articleNumber);

	public Integer getOrderLastxDays(int recId, int day);

	public String getKasName(String articleNumber);

	public void deleteErpArticle(String artNum);

}