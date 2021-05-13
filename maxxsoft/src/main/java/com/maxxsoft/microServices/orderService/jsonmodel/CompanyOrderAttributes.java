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
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "order_id", "company_id", "company_name", "extension_attributes" })
public class CompanyOrderAttributes {

	@JsonProperty("order_id")
	private Integer orderId;
	@JsonProperty("company_id")
	private Integer companyId;
	@JsonProperty("company_name")
	private String companyName;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes______________________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("order_id")
	public Integer getOrderId() {
		return orderId;
	}

	@JsonProperty("order_id")
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@JsonProperty("company_id")
	public Integer getCompanyId() {
		return companyId;
	}

	@JsonProperty("company_id")
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	@JsonProperty("company_name")
	public String getCompanyName() {
		return companyName;
	}

	@JsonProperty("company_name")
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes______________________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes______________________ extensionAttributes) {
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
