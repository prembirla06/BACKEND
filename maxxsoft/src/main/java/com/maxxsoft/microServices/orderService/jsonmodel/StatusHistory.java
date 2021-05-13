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
@JsonPropertyOrder({ "comment", "created_at", "entity_id", "entity_name", "is_customer_notified", "is_visible_on_front",
		"parent_id", "status", "extension_attributes" })
public class StatusHistory {

	@JsonProperty("comment")
	private String comment;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("entity_id")
	private Integer entityId;
	@JsonProperty("entity_name")
	private String entityName;
	@JsonProperty("is_customer_notified")
	private Integer isCustomerNotified;
	@JsonProperty("is_visible_on_front")
	private Integer isVisibleOnFront;
	@JsonProperty("parent_id")
	private Integer parentId;
	@JsonProperty("status")
	private String status;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes_________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("comment")
	public String getComment() {
		return comment;
	}

	@JsonProperty("comment")
	public void setComment(String comment) {
		this.comment = comment;
	}

	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("entity_id")
	public Integer getEntityId() {
		return entityId;
	}

	@JsonProperty("entity_id")
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@JsonProperty("entity_name")
	public String getEntityName() {
		return entityName;
	}

	@JsonProperty("entity_name")
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	@JsonProperty("is_customer_notified")
	public Integer getIsCustomerNotified() {
		return isCustomerNotified;
	}

	@JsonProperty("is_customer_notified")
	public void setIsCustomerNotified(Integer isCustomerNotified) {
		this.isCustomerNotified = isCustomerNotified;
	}

	@JsonProperty("is_visible_on_front")
	public Integer getIsVisibleOnFront() {
		return isVisibleOnFront;
	}

	@JsonProperty("is_visible_on_front")
	public void setIsVisibleOnFront(Integer isVisibleOnFront) {
		this.isVisibleOnFront = isVisibleOnFront;
	}

	@JsonProperty("parent_id")
	public Integer getParentId() {
		return parentId;
	}

	@JsonProperty("parent_id")
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	@JsonProperty("status")
	public void setStatus(String status) {
		this.status = status;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes_________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes_________ extensionAttributes) {
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
