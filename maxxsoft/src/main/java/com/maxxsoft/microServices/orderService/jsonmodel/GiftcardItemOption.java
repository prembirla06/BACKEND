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
@JsonPropertyOrder({ "giftcard_amount", "custom_giftcard_amount", "giftcard_sender_name", "giftcard_recipient_name",
		"giftcard_sender_email", "giftcard_recipient_email", "giftcard_message", "extension_attributes" })
public class GiftcardItemOption {

	@JsonProperty("giftcard_amount")
	private String giftcardAmount;
	@JsonProperty("custom_giftcard_amount")
	private Integer customGiftcardAmount;
	@JsonProperty("giftcard_sender_name")
	private String giftcardSenderName;
	@JsonProperty("giftcard_recipient_name")
	private String giftcardRecipientName;
	@JsonProperty("giftcard_sender_email")
	private String giftcardSenderEmail;
	@JsonProperty("giftcard_recipient_email")
	private String giftcardRecipientEmail;
	@JsonProperty("giftcard_message")
	private String giftcardMessage;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes___ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("giftcard_amount")
	public String getGiftcardAmount() {
		return giftcardAmount;
	}

	@JsonProperty("giftcard_amount")
	public void setGiftcardAmount(String giftcardAmount) {
		this.giftcardAmount = giftcardAmount;
	}

	@JsonProperty("custom_giftcard_amount")
	public Integer getCustomGiftcardAmount() {
		return customGiftcardAmount;
	}

	@JsonProperty("custom_giftcard_amount")
	public void setCustomGiftcardAmount(Integer customGiftcardAmount) {
		this.customGiftcardAmount = customGiftcardAmount;
	}

	@JsonProperty("giftcard_sender_name")
	public String getGiftcardSenderName() {
		return giftcardSenderName;
	}

	@JsonProperty("giftcard_sender_name")
	public void setGiftcardSenderName(String giftcardSenderName) {
		this.giftcardSenderName = giftcardSenderName;
	}

	@JsonProperty("giftcard_recipient_name")
	public String getGiftcardRecipientName() {
		return giftcardRecipientName;
	}

	@JsonProperty("giftcard_recipient_name")
	public void setGiftcardRecipientName(String giftcardRecipientName) {
		this.giftcardRecipientName = giftcardRecipientName;
	}

	@JsonProperty("giftcard_sender_email")
	public String getGiftcardSenderEmail() {
		return giftcardSenderEmail;
	}

	@JsonProperty("giftcard_sender_email")
	public void setGiftcardSenderEmail(String giftcardSenderEmail) {
		this.giftcardSenderEmail = giftcardSenderEmail;
	}

	@JsonProperty("giftcard_recipient_email")
	public String getGiftcardRecipientEmail() {
		return giftcardRecipientEmail;
	}

	@JsonProperty("giftcard_recipient_email")
	public void setGiftcardRecipientEmail(String giftcardRecipientEmail) {
		this.giftcardRecipientEmail = giftcardRecipientEmail;
	}

	@JsonProperty("giftcard_message")
	public String getGiftcardMessage() {
		return giftcardMessage;
	}

	@JsonProperty("giftcard_message")
	public void setGiftcardMessage(String giftcardMessage) {
		this.giftcardMessage = giftcardMessage;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes___ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes___ extensionAttributes) {
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
