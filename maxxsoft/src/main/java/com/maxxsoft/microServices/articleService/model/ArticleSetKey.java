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
package com.maxxsoft.microServices.articleService.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
// @Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ArticleSetKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public Long getArticleSetId() {
		return articleSetId;
	}

	public void setArticleSetId(Long articleSetId) {
		this.articleSetId = articleSetId;
	}

	@Column(name = "articleId")
	Long articleId;

	@Column(name = "articleSetId")
	Long articleSetId;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		ArticleSetKey that = (ArticleSetKey) o;
		return Objects.equals(articleSetId, that.articleSetId) && Objects.equals(articleId, that.articleId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(articleSetId, articleId);
	}

}
