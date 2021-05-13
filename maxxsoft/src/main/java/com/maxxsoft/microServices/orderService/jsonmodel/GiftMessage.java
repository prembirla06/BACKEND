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
@JsonPropertyOrder({ "gift_message_id", "customer_id", "sender", "recipient", "message", "extension_attributes" })
public class GiftMessage {

	@JsonProperty("gift_message_id")
	private Integer giftMessageId;
	@JsonProperty("customer_id")
	private Integer customerId;
	@JsonProperty("sender")
	private String sender;
	@JsonProperty("recipient")
	private String recipient;
	@JsonProperty("message")
	private String message;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes______ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("gift_message_id")
	public Integer getGiftMessageId() {
		return giftMessageId;
	}

	@JsonProperty("gift_message_id")
	public void setGiftMessageId(Integer giftMessageId) {
		this.giftMessageId = giftMessageId;
	}

	@JsonProperty("customer_id")
	public Integer getCustomerId() {
		return customerId;
	}

	@JsonProperty("customer_id")
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@JsonProperty("sender")
	public String getSender() {
		return sender;
	}

	@JsonProperty("sender")
	public void setSender(String sender) {
		this.sender = sender;
	}

	@JsonProperty("recipient")
	public String getRecipient() {
		return recipient;
	}

	@JsonProperty("recipient")
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes______ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes______ extensionAttributes) {
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
