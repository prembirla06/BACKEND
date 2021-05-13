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

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Entity
@Data
@NoArgsConstructor
@Table(name = "articleSetRelation", schema = "public")
public class ArticleSetRelation {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long articleSetRelationId;

	@Column(name = "articleId")
	Long articleId;

	@Column(name = "articleSetId")
	Long articleSetId;

	// @JsonIgnore
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@MapsId("articleId")
	@JoinColumn(name = "articleId")
	Article article;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "articleSetId")
	@MapsId("articleSetId")
	ArticleSet articleSet;

	int quantity;

	public ArticleSetRelation(Article article, ArticleSet articleSet, int quantity) {
		this.article = article;
		this.articleSet = articleSet;
		this.quantity = quantity;
		this.articleId = article.getArticleId();
		this.articleSetId = articleSet.getArticleSetId();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		ArticleSetRelation that = (ArticleSetRelation) o;
		return Objects.equals(articleSet, that.articleSet) && Objects.equals(article, that.article)
				&& Objects.equals(quantity, that.quantity) && Objects.equals(articleId, that.articleId)
				&& Objects.equals(articleSetId, that.articleSetId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(articleSet, article, quantity, articleId, articleSetId);
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
