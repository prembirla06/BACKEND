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
import java.math.BigDecimal;
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
@Table(name = "article", schema = "public")
public class Article extends AuditModel implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long articleId;

	private String shortName;

	private String longName;

	@Column(unique = true)
	private String number;

	private String substituteNumber;
	// europianArticleNumber (barcode)
	@Column(unique = true)
	private String ean;
	// No of units of articles in one package at the time of buy
	private Integer packageUnit;

	private double totalWeight;

	private int stock;

	private int preOrder;

	private int deliveryTime;

	private int dimension;

	@Column(columnDefinition = "boolean default true")
	private boolean active = true;

	@Column(columnDefinition = "boolean default true")
	private boolean standalone = true;

	@Column(columnDefinition = "boolean default false")
	private boolean eol = false;

	private BigDecimal buyPrice;

	@Column(length = 1000000)
	@Lob
	private String shortDescription;

	@Column(length = 1000000)
	@Lob
	private String longDescription;

	private double height;

	private double depth;

	private double width;

	@JsonManagedReference
	// @JsonIgnoreProperties("articleSet")
	@JsonIgnore
	@OneToMany(mappedBy = "article", fetch = FetchType.EAGER, orphanRemoval = false, cascade = CascadeType.ALL)
	Set<ArticleSetRelation> articleSetRelations = new HashSet();

	@JsonIgnore
	// @JsonManagedReference
	@OneToMany(mappedBy = "article", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
	Set<ArticleImage> images = new HashSet();

	public void addImage(ImageRequest image) {
		ArticleImage imageObject = new ArticleImage(image.getName(), image.getOrder(), image.getPicByte(), this);
		images.add(imageObject);
	}

	public void removeImage(ArticleImage image) {
		images.remove(image);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Article article = (Article) o;
		return Objects.equals(shortName, article.shortName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(shortName);
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
