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
@JsonPropertyOrder({ "entity_id", "entity_type", "wrapping_id", "wrapping_allow_gift_receipt",
		"wrapping_add_printed_card" })
public class ExtensionAttributes______ {

	@JsonProperty("entity_id")
	private String entityId;
	@JsonProperty("entity_type")
	private String entityType;
	@JsonProperty("wrapping_id")
	private Integer wrappingId;
	@JsonProperty("wrapping_allow_gift_receipt")
	private Boolean wrappingAllowGiftReceipt;
	@JsonProperty("wrapping_add_printed_card")
	private Boolean wrappingAddPrintedCard;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("entity_id")
	public String getEntityId() {
		return entityId;
	}

	@JsonProperty("entity_id")
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	@JsonProperty("entity_type")
	public String getEntityType() {
		return entityType;
	}

	@JsonProperty("entity_type")
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	@JsonProperty("wrapping_id")
	public Integer getWrappingId() {
		return wrappingId;
	}

	@JsonProperty("wrapping_id")
	public void setWrappingId(Integer wrappingId) {
		this.wrappingId = wrappingId;
	}

	@JsonProperty("wrapping_allow_gift_receipt")
	public Boolean getWrappingAllowGiftReceipt() {
		return wrappingAllowGiftReceipt;
	}

	@JsonProperty("wrapping_allow_gift_receipt")
	public void setWrappingAllowGiftReceipt(Boolean wrappingAllowGiftReceipt) {
		this.wrappingAllowGiftReceipt = wrappingAllowGiftReceipt;
	}

	@JsonProperty("wrapping_add_printed_card")
	public Boolean getWrappingAddPrintedCard() {
		return wrappingAddPrintedCard;
	}

	@JsonProperty("wrapping_add_printed_card")
	public void setWrappingAddPrintedCard(Boolean wrappingAddPrintedCard) {
		this.wrappingAddPrintedCard = wrappingAddPrintedCard;
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
