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
package com.maxxsoft.microServices.ebayService.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Getter
@Setter
public class EbayOrder {

	@JsonProperty("href")
	private String href;

	@JsonProperty("limit")
	private long limit;

	@JsonProperty("next")
	private String next;

	@JsonProperty("offset")
	private long offset;

	// @JsonProperty("orders")
	// private List<MagentoOrder> orders;

	@JsonProperty("prev")
	private String prev;

	@JsonProperty("total")
	private long total;

	// @JsonProperty("warnings")
	// private List<Warning> warnings;

}