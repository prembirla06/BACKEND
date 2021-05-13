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
@JsonPropertyOrder({ "type", "item_id", "associated_item_id", "applied_taxes", "extension_attributes" })
public class ItemAppliedTax {

	@JsonProperty("type")
	private String type;
	@JsonProperty("item_id")
	private Integer itemId;
	@JsonProperty("associated_item_id")
	private Integer associatedItemId;
	@JsonProperty("applied_taxes")
	private List<AppliedTax_> appliedTaxes = null;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes___________________________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("item_id")
	public Integer getItemId() {
		return itemId;
	}

	@JsonProperty("item_id")
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@JsonProperty("associated_item_id")
	public Integer getAssociatedItemId() {
		return associatedItemId;
	}

	@JsonProperty("associated_item_id")
	public void setAssociatedItemId(Integer associatedItemId) {
		this.associatedItemId = associatedItemId;
	}

	@JsonProperty("applied_taxes")
	public List<AppliedTax_> getAppliedTaxes() {
		return appliedTaxes;
	}

	@JsonProperty("applied_taxes")
	public void setAppliedTaxes(List<AppliedTax_> appliedTaxes) {
		this.appliedTaxes = appliedTaxes;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes___________________________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes___________________________ extensionAttributes) {
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
