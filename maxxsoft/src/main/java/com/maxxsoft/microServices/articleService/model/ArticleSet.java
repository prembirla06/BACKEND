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

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.maxxsoft.common.model.AuditModel;
import com.maxxsoft.microServices.articleService.model.request.ImageRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "articleSet", schema = "public")
public class ArticleSet extends AuditModel {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long articleSetId;

	private String shortName;
	private String longName;

	@Column(unique = true)
	private String number;

	private String substituteNumber;

	@Column(unique = true)
	private String ean;

	@Column(length = 1000000)
	@Lob
	private String shortDescription;

	@Column(length = 1000000)
	@Lob
	private String longDescription;

	private int totalArticles;

	private double totalWeight;

	private int stock;

	private int preOrder;

	private int deliveryTime;

	private int dimension;

	private double height;

	private double depth;

	private double width;

	@Column(columnDefinition = "boolean default true")
	private boolean active = true;

	// @JsonBackReference
	@JsonIgnore
	@JsonManagedReference
	@OneToMany(mappedBy = "articleSet", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
	Set<ArticleSetRelation> articleSetRelations = new HashSet();

	public void addArticle(Article article, int quantity) {
		ArticleSetRelation articleSetRelation = new ArticleSetRelation(article, this, quantity);
		articleSetRelations.add(articleSetRelation);
		article.getArticleSetRelations().add(articleSetRelation);
	}

	// Hack to avoid Null Article Set ID AND Single Article ArticleSet
	public void addArticle1(Article article, int quantity) {
		ArticleSetRelation articleSetRelation = new ArticleSetRelation(article, this, quantity);
	}
	// Hack ends

	// public void removeArticle(Article article) {
	//
	// articleSetRelations.forEach(articleSetRelation -> {
	// if (articleSetRelation.getArticleSet().equals(this) &&
	// articleSetRelation.getArticle().equals(article)) {
	// articleSetRelation.getArticle().getArticleSetRelations().remove(articleSetRelation);
	// articleSetRelation.setArticleSet(null);
	// articleSetRelation.setArticle(null);
	// }
	// });
	//
	// }
	//
	// public void removeArticleSetRelation() {
	// System.out.println(articleSetRelations.size());
	// this.articleSetRelations.forEach(articleSetRelation -> {
	// articleSetRelation.getArticle().getArticleSetRelations().remove(articleSetRelation);
	// articleSetRelation.setArticleSet(null);
	// articleSetRelation.setArticle(null);
	// });
	//
	// }

	@JsonIgnore
	// @JsonManagedReference
	@OneToMany(mappedBy = "articleSet", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	Set<ArticleSetImage> images = new HashSet();

	public void addImage(ImageRequest image) {
		ArticleSetImage imageObject = new ArticleSetImage(image.getName(), image.getOrder(), image.getPicByte(), this);
		images.add(imageObject);
	}

	public void removeImage(ArticleSetImage image) {
		images.remove(image);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		ArticleSet that = (ArticleSet) o;
		return Objects.equals(shortName, that.shortName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(shortName);
	}

	public boolean getActive() {
		// TODO Auto-generated method stub
		return this.active;
	}

}