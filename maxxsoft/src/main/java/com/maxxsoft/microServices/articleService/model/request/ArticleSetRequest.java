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
package com.maxxsoft.microServices.articleService.model.request;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.maxxsoft.microServices.articleService.model.ArticleWithQuantity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Getter
@Setter
public class ArticleSetRequest {

	@NotBlank
	@Size(min = 3, max = 50)
	private String shortName;
	private String longName;
	private String number;

	private String substituteNumber;// one to many relation

	private String ean; // europianArticleNumber (barcode)

	private String shortDescription;

	private String longDescription;

	private int totalArticles;

	private double totalWeight;

	@NotBlank
	private Set<ArticleWithQuantity> articleWithQuantity;

	Set<ImageRequest> images = new HashSet();

	private int stock;

	private int preOrder;

	private int deliveryTime;

	private int dimension;

	private double height;

	private double depth;

	private double width;

}