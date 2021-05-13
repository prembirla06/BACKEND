
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
@JsonPropertyOrder({ "gift_message", "gw_id", "gw_base_price", "gw_price", "gw_base_tax_amount", "gw_tax_amount",
		"gw_base_price_invoiced", "gw_price_invoiced", "gw_base_tax_amount_invoiced", "gw_tax_amount_invoiced",
		"gw_base_price_refunded", "gw_price_refunded", "gw_base_tax_amount_refunded", "gw_tax_amount_refunded" })
public class ExtensionAttributes_____ {

	@JsonProperty("gift_message")
	private GiftMessage giftMessage;
	@JsonProperty("gw_id")
	private String gwId;
	@JsonProperty("gw_base_price")
	private String gwBasePrice;
	@JsonProperty("gw_price")
	private String gwPrice;
	@JsonProperty("gw_base_tax_amount")
	private String gwBaseTaxAmount;
	@JsonProperty("gw_tax_amount")
	private String gwTaxAmount;
	@JsonProperty("gw_base_price_invoiced")
	private String gwBasePriceInvoiced;
	@JsonProperty("gw_price_invoiced")
	private String gwPriceInvoiced;
	@JsonProperty("gw_base_tax_amount_invoiced")
	private String gwBaseTaxAmountInvoiced;
	@JsonProperty("gw_tax_amount_invoiced")
	private String gwTaxAmountInvoiced;
	@JsonProperty("gw_base_price_refunded")
	private String gwBasePriceRefunded;
	@JsonProperty("gw_price_refunded")
	private String gwPriceRefunded;
	@JsonProperty("gw_base_tax_amount_refunded")
	private String gwBaseTaxAmountRefunded;
	@JsonProperty("gw_tax_amount_refunded")
	private String gwTaxAmountRefunded;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("gift_message")
	public GiftMessage getGiftMessage() {
		return giftMessage;
	}

	@JsonProperty("gift_message")
	public void setGiftMessage(GiftMessage giftMessage) {
		this.giftMessage = giftMessage;
	}

	@JsonProperty("gw_id")
	public String getGwId() {
		return gwId;
	}

	@JsonProperty("gw_id")
	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	@JsonProperty("gw_base_price")
	public String getGwBasePrice() {
		return gwBasePrice;
	}

	@JsonProperty("gw_base_price")
	public void setGwBasePrice(String gwBasePrice) {
		this.gwBasePrice = gwBasePrice;
	}

	@JsonProperty("gw_price")
	public String getGwPrice() {
		return gwPrice;
	}

	@JsonProperty("gw_price")
	public void setGwPrice(String gwPrice) {
		this.gwPrice = gwPrice;
	}

	@JsonProperty("gw_base_tax_amount")
	public String getGwBaseTaxAmount() {
		return gwBaseTaxAmount;
	}

	@JsonProperty("gw_base_tax_amount")
	public void setGwBaseTaxAmount(String gwBaseTaxAmount) {
		this.gwBaseTaxAmount = gwBaseTaxAmount;
	}

	@JsonProperty("gw_tax_amount")
	public String getGwTaxAmount() {
		return gwTaxAmount;
	}

	@JsonProperty("gw_tax_amount")
	public void setGwTaxAmount(String gwTaxAmount) {
		this.gwTaxAmount = gwTaxAmount;
	}

	@JsonProperty("gw_base_price_invoiced")
	public String getGwBasePriceInvoiced() {
		return gwBasePriceInvoiced;
	}

	@JsonProperty("gw_base_price_invoiced")
	public void setGwBasePriceInvoiced(String gwBasePriceInvoiced) {
		this.gwBasePriceInvoiced = gwBasePriceInvoiced;
	}

	@JsonProperty("gw_price_invoiced")
	public String getGwPriceInvoiced() {
		return gwPriceInvoiced;
	}

	@JsonProperty("gw_price_invoiced")
	public void setGwPriceInvoiced(String gwPriceInvoiced) {
		this.gwPriceInvoiced = gwPriceInvoiced;
	}

	@JsonProperty("gw_base_tax_amount_invoiced")
	public String getGwBaseTaxAmountInvoiced() {
		return gwBaseTaxAmountInvoiced;
	}

	@JsonProperty("gw_base_tax_amount_invoiced")
	public void setGwBaseTaxAmountInvoiced(String gwBaseTaxAmountInvoiced) {
		this.gwBaseTaxAmountInvoiced = gwBaseTaxAmountInvoiced;
	}

	@JsonProperty("gw_tax_amount_invoiced")
	public String getGwTaxAmountInvoiced() {
		return gwTaxAmountInvoiced;
	}

	@JsonProperty("gw_tax_amount_invoiced")
	public void setGwTaxAmountInvoiced(String gwTaxAmountInvoiced) {
		this.gwTaxAmountInvoiced = gwTaxAmountInvoiced;
	}

	@JsonProperty("gw_base_price_refunded")
	public String getGwBasePriceRefunded() {
		return gwBasePriceRefunded;
	}

	@JsonProperty("gw_base_price_refunded")
	public void setGwBasePriceRefunded(String gwBasePriceRefunded) {
		this.gwBasePriceRefunded = gwBasePriceRefunded;
	}

	@JsonProperty("gw_price_refunded")
	public String getGwPriceRefunded() {
		return gwPriceRefunded;
	}

	@JsonProperty("gw_price_refunded")
	public void setGwPriceRefunded(String gwPriceRefunded) {
		this.gwPriceRefunded = gwPriceRefunded;
	}

	@JsonProperty("gw_base_tax_amount_refunded")
	public String getGwBaseTaxAmountRefunded() {
		return gwBaseTaxAmountRefunded;
	}

	@JsonProperty("gw_base_tax_amount_refunded")
	public void setGwBaseTaxAmountRefunded(String gwBaseTaxAmountRefunded) {
		this.gwBaseTaxAmountRefunded = gwBaseTaxAmountRefunded;
	}

	@JsonProperty("gw_tax_amount_refunded")
	public String getGwTaxAmountRefunded() {
		return gwTaxAmountRefunded;
	}

	@JsonProperty("gw_tax_amount_refunded")
	public void setGwTaxAmountRefunded(String gwTaxAmountRefunded) {
		this.gwTaxAmountRefunded = gwTaxAmountRefunded;
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
