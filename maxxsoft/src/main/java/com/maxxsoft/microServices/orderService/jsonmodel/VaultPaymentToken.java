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
@JsonPropertyOrder({ "entity_id", "customer_id", "public_hash", "payment_method_code", "type", "created_at",
		"expires_at", "gateway_token", "token_details", "is_active", "is_visible" })
public class VaultPaymentToken {

	@JsonProperty("entity_id")
	private Integer entityId;
	@JsonProperty("customer_id")
	private Integer customerId;
	@JsonProperty("public_hash")
	private String publicHash;
	@JsonProperty("payment_method_code")
	private String paymentMethodCode;
	@JsonProperty("type")
	private String type;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("expires_at")
	private String expiresAt;
	@JsonProperty("gateway_token")
	private String gatewayToken;
	@JsonProperty("token_details")
	private String tokenDetails;
	@JsonProperty("is_active")
	private Boolean isActive;
	@JsonProperty("is_visible")
	private Boolean isVisible;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("entity_id")
	public Integer getEntityId() {
		return entityId;
	}

	@JsonProperty("entity_id")
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@JsonProperty("customer_id")
	public Integer getCustomerId() {
		return customerId;
	}

	@JsonProperty("customer_id")
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@JsonProperty("public_hash")
	public String getPublicHash() {
		return publicHash;
	}

	@JsonProperty("public_hash")
	public void setPublicHash(String publicHash) {
		this.publicHash = publicHash;
	}

	@JsonProperty("payment_method_code")
	public String getPaymentMethodCode() {
		return paymentMethodCode;
	}

	@JsonProperty("payment_method_code")
	public void setPaymentMethodCode(String paymentMethodCode) {
		this.paymentMethodCode = paymentMethodCode;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("expires_at")
	public String getExpiresAt() {
		return expiresAt;
	}

	@JsonProperty("expires_at")
	public void setExpiresAt(String expiresAt) {
		this.expiresAt = expiresAt;
	}

	@JsonProperty("gateway_token")
	public String getGatewayToken() {
		return gatewayToken;
	}

	@JsonProperty("gateway_token")
	public void setGatewayToken(String gatewayToken) {
		this.gatewayToken = gatewayToken;
	}

	@JsonProperty("token_details")
	public String getTokenDetails() {
		return tokenDetails;
	}

	@JsonProperty("token_details")
	public void setTokenDetails(String tokenDetails) {
		this.tokenDetails = tokenDetails;
	}

	@JsonProperty("is_active")
	public Boolean getIsActive() {
		return isActive;
	}

	@JsonProperty("is_active")
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	@JsonProperty("is_visible")
	public Boolean getIsVisible() {
		return isVisible;
	}

	@JsonProperty("is_visible")
	public void setIsVisible(Boolean isVisible) {
		this.isVisible = isVisible;
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
