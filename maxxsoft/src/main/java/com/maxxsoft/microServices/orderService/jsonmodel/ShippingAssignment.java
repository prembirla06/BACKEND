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
package com.maxxsoft.microServices.orderService.jsonmodel;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "shipping", "items", "stock_id", "extension_attributes" })
public class ShippingAssignment {

	@JsonProperty("shipping")
	private Shipping shipping;
	@JsonProperty("items")
	private List<Item_> items = null;
	@JsonProperty("stock_id")
	private Integer stockId;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes_____________________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("shipping")
	public Shipping getShipping() {
		return shipping;
	}

	@JsonProperty("shipping")
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}

	@JsonProperty("items")
	public List<Item_> getItems() {
		return items;
	}

	@JsonProperty("items")
	public void setItems(List<Item_> items) {
		this.items = items;
	}

	@JsonProperty("stock_id")
	public Integer getStockId() {
		return stockId;
	}

	@JsonProperty("stock_id")
	public void setStockId(Integer stockId) {
		this.stockId = stockId;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes_____________________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes_____________________ extensionAttributes) {
		this.extensionAttributes = extensionAttributes;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
