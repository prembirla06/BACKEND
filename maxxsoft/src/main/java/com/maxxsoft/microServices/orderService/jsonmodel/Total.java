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
@JsonPropertyOrder({ "base_shipping_amount", "base_shipping_canceled", "base_shipping_discount_amount",
		"base_shipping_discount_tax_compensation_amnt", "base_shipping_incl_tax", "base_shipping_invoiced",
		"base_shipping_refunded", "base_shipping_tax_amount", "base_shipping_tax_refunded", "shipping_amount",
		"shipping_canceled", "shipping_discount_amount", "shipping_discount_tax_compensation_amount",
		"shipping_incl_tax", "shipping_invoiced", "shipping_refunded", "shipping_tax_amount", "shipping_tax_refunded",
		"extension_attributes" })
public class Total {

	@JsonProperty("base_shipping_amount")
	private Integer baseShippingAmount;
	@JsonProperty("base_shipping_canceled")
	private Integer baseShippingCanceled;
	@JsonProperty("base_shipping_discount_amount")
	private Integer baseShippingDiscountAmount;
	@JsonProperty("base_shipping_discount_tax_compensation_amnt")
	private Integer baseShippingDiscountTaxCompensationAmnt;
	@JsonProperty("base_shipping_incl_tax")
	private Integer baseShippingInclTax;
	@JsonProperty("base_shipping_invoiced")
	private Integer baseShippingInvoiced;
	@JsonProperty("base_shipping_refunded")
	private Integer baseShippingRefunded;
	@JsonProperty("base_shipping_tax_amount")
	private Integer baseShippingTaxAmount;
	@JsonProperty("base_shipping_tax_refunded")
	private Integer baseShippingTaxRefunded;
	@JsonProperty("shipping_amount")
	private Integer shippingAmount;
	@JsonProperty("shipping_canceled")
	private Integer shippingCanceled;
	@JsonProperty("shipping_discount_amount")
	private Integer shippingDiscountAmount;
	@JsonProperty("shipping_discount_tax_compensation_amount")
	private Integer shippingDiscountTaxCompensationAmount;
	@JsonProperty("shipping_incl_tax")
	private Integer shippingInclTax;
	@JsonProperty("shipping_invoiced")
	private Integer shippingInvoiced;
	@JsonProperty("shipping_refunded")
	private Integer shippingRefunded;
	@JsonProperty("shipping_tax_amount")
	private Integer shippingTaxAmount;
	@JsonProperty("shipping_tax_refunded")
	private Integer shippingTaxRefunded;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes____________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("base_shipping_amount")
	public Integer getBaseShippingAmount() {
		return baseShippingAmount;
	}

	@JsonProperty("base_shipping_amount")
	public void setBaseShippingAmount(Integer baseShippingAmount) {
		this.baseShippingAmount = baseShippingAmount;
	}

	@JsonProperty("base_shipping_canceled")
	public Integer getBaseShippingCanceled() {
		return baseShippingCanceled;
	}

	@JsonProperty("base_shipping_canceled")
	public void setBaseShippingCanceled(Integer baseShippingCanceled) {
		this.baseShippingCanceled = baseShippingCanceled;
	}

	@JsonProperty("base_shipping_discount_amount")
	public Integer getBaseShippingDiscountAmount() {
		return baseShippingDiscountAmount;
	}

	@JsonProperty("base_shipping_discount_amount")
	public void setBaseShippingDiscountAmount(Integer baseShippingDiscountAmount) {
		this.baseShippingDiscountAmount = baseShippingDiscountAmount;
	}

	@JsonProperty("base_shipping_discount_tax_compensation_amnt")
	public Integer getBaseShippingDiscountTaxCompensationAmnt() {
		return baseShippingDiscountTaxCompensationAmnt;
	}

	@JsonProperty("base_shipping_discount_tax_compensation_amnt")
	public void setBaseShippingDiscountTaxCompensationAmnt(Integer baseShippingDiscountTaxCompensationAmnt) {
		this.baseShippingDiscountTaxCompensationAmnt = baseShippingDiscountTaxCompensationAmnt;
	}

	@JsonProperty("base_shipping_incl_tax")
	public Integer getBaseShippingInclTax() {
		return baseShippingInclTax;
	}

	@JsonProperty("base_shipping_incl_tax")
	public void setBaseShippingInclTax(Integer baseShippingInclTax) {
		this.baseShippingInclTax = baseShippingInclTax;
	}

	@JsonProperty("base_shipping_invoiced")
	public Integer getBaseShippingInvoiced() {
		return baseShippingInvoiced;
	}

	@JsonProperty("base_shipping_invoiced")
	public void setBaseShippingInvoiced(Integer baseShippingInvoiced) {
		this.baseShippingInvoiced = baseShippingInvoiced;
	}

	@JsonProperty("base_shipping_refunded")
	public Integer getBaseShippingRefunded() {
		return baseShippingRefunded;
	}

	@JsonProperty("base_shipping_refunded")
	public void setBaseShippingRefunded(Integer baseShippingRefunded) {
		this.baseShippingRefunded = baseShippingRefunded;
	}

	@JsonProperty("base_shipping_tax_amount")
	public Integer getBaseShippingTaxAmount() {
		return baseShippingTaxAmount;
	}

	@JsonProperty("base_shipping_tax_amount")
	public void setBaseShippingTaxAmount(Integer baseShippingTaxAmount) {
		this.baseShippingTaxAmount = baseShippingTaxAmount;
	}

	@JsonProperty("base_shipping_tax_refunded")
	public Integer getBaseShippingTaxRefunded() {
		return baseShippingTaxRefunded;
	}

	@JsonProperty("base_shipping_tax_refunded")
	public void setBaseShippingTaxRefunded(Integer baseShippingTaxRefunded) {
		this.baseShippingTaxRefunded = baseShippingTaxRefunded;
	}

	@JsonProperty("shipping_amount")
	public Integer getShippingAmount() {
		return shippingAmount;
	}

	@JsonProperty("shipping_amount")
	public void setShippingAmount(Integer shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	@JsonProperty("shipping_canceled")
	public Integer getShippingCanceled() {
		return shippingCanceled;
	}

	@JsonProperty("shipping_canceled")
	public void setShippingCanceled(Integer shippingCanceled) {
		this.shippingCanceled = shippingCanceled;
	}

	@JsonProperty("shipping_discount_amount")
	public Integer getShippingDiscountAmount() {
		return shippingDiscountAmount;
	}

	@JsonProperty("shipping_discount_amount")
	public void setShippingDiscountAmount(Integer shippingDiscountAmount) {
		this.shippingDiscountAmount = shippingDiscountAmount;
	}

	@JsonProperty("shipping_discount_tax_compensation_amount")
	public Integer getShippingDiscountTaxCompensationAmount() {
		return shippingDiscountTaxCompensationAmount;
	}

	@JsonProperty("shipping_discount_tax_compensation_amount")
	public void setShippingDiscountTaxCompensationAmount(Integer shippingDiscountTaxCompensationAmount) {
		this.shippingDiscountTaxCompensationAmount = shippingDiscountTaxCompensationAmount;
	}

	@JsonProperty("shipping_incl_tax")
	public Integer getShippingInclTax() {
		return shippingInclTax;
	}

	@JsonProperty("shipping_incl_tax")
	public void setShippingInclTax(Integer shippingInclTax) {
		this.shippingInclTax = shippingInclTax;
	}

	@JsonProperty("shipping_invoiced")
	public Integer getShippingInvoiced() {
		return shippingInvoiced;
	}

	@JsonProperty("shipping_invoiced")
	public void setShippingInvoiced(Integer shippingInvoiced) {
		this.shippingInvoiced = shippingInvoiced;
	}

	@JsonProperty("shipping_refunded")
	public Integer getShippingRefunded() {
		return shippingRefunded;
	}

	@JsonProperty("shipping_refunded")
	public void setShippingRefunded(Integer shippingRefunded) {
		this.shippingRefunded = shippingRefunded;
	}

	@JsonProperty("shipping_tax_amount")
	public Integer getShippingTaxAmount() {
		return shippingTaxAmount;
	}

	@JsonProperty("shipping_tax_amount")
	public void setShippingTaxAmount(Integer shippingTaxAmount) {
		this.shippingTaxAmount = shippingTaxAmount;
	}

	@JsonProperty("shipping_tax_refunded")
	public Integer getShippingTaxRefunded() {
		return shippingTaxRefunded;
	}

	@JsonProperty("shipping_tax_refunded")
	public void setShippingTaxRefunded(Integer shippingTaxRefunded) {
		this.shippingTaxRefunded = shippingTaxRefunded;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes____________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes____________ extensionAttributes) {
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
