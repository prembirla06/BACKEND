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
@JsonPropertyOrder({ "option_id", "option_qty", "option_selections", "extension_attributes" })
public class BundleOption_ {

	@JsonProperty("option_id")
	private Integer optionId;
	@JsonProperty("option_qty")
	private Integer optionQty;
	@JsonProperty("option_selections")
	private List<Integer> optionSelections = null;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes________________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("option_id")
	public Integer getOptionId() {
		return optionId;
	}

	@JsonProperty("option_id")
	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	@JsonProperty("option_qty")
	public Integer getOptionQty() {
		return optionQty;
	}

	@JsonProperty("option_qty")
	public void setOptionQty(Integer optionQty) {
		this.optionQty = optionQty;
	}

	@JsonProperty("option_selections")
	public List<Integer> getOptionSelections() {
		return optionSelections;
	}

	@JsonProperty("option_selections")
	public void setOptionSelections(List<Integer> optionSelections) {
		this.optionSelections = optionSelections;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes________________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes________________ extensionAttributes) {
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
