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
@JsonPropertyOrder({ "additional_data", "amount_refunded", "applied_rule_ids", "base_amount_refunded", "base_cost",
		"base_discount_amount", "base_discount_invoiced", "base_discount_refunded",
		"base_discount_tax_compensation_amount", "base_discount_tax_compensation_invoiced",
		"base_discount_tax_compensation_refunded", "base_original_price", "base_price", "base_price_incl_tax",
		"base_row_invoiced", "base_row_total", "base_row_total_incl_tax", "base_tax_amount", "base_tax_before_discount",
		"base_tax_invoiced", "base_tax_refunded", "base_weee_tax_applied_amount", "base_weee_tax_applied_row_amnt",
		"base_weee_tax_disposition", "base_weee_tax_row_disposition", "created_at", "description", "discount_amount",
		"discount_invoiced", "discount_percent", "discount_refunded", "event_id", "ext_order_item_id", "free_shipping",
		"gw_base_price", "gw_base_price_invoiced", "gw_base_price_refunded", "gw_base_tax_amount",
		"gw_base_tax_amount_invoiced", "gw_base_tax_amount_refunded", "gw_id", "gw_price", "gw_price_invoiced",
		"gw_price_refunded", "gw_tax_amount", "gw_tax_amount_invoiced", "gw_tax_amount_refunded",
		"discount_tax_compensation_amount", "discount_tax_compensation_canceled", "discount_tax_compensation_invoiced",
		"discount_tax_compensation_refunded", "is_qty_decimal", "is_virtual", "item_id", "locked_do_invoice",
		"locked_do_ship", "name", "no_discount", "order_id", "original_price", "parent_item_id", "price",
		"price_incl_tax", "product_id", "product_type", "qty_backordered", "qty_canceled", "qty_invoiced",
		"qty_ordered", "qty_refunded", "qty_returned", "qty_shipped", "quote_item_id", "row_invoiced", "row_total",
		"row_total_incl_tax", "row_weight", "sku", "store_id", "tax_amount", "tax_before_discount", "tax_canceled",
		"tax_invoiced", "tax_percent", "tax_refunded", "updated_at", "weee_tax_applied", "weee_tax_applied_amount",
		"weee_tax_applied_row_amount", "weee_tax_disposition", "weee_tax_row_disposition", "weight", "parent_item",
		"product_option", "extension_attributes" })
public class Item_ {

	@JsonProperty("additional_data")
	private String additionalData;
	@JsonProperty("amount_refunded")
	private Integer amountRefunded;
	@JsonProperty("applied_rule_ids")
	private String appliedRuleIds;
	@JsonProperty("base_amount_refunded")
	private Integer baseAmountRefunded;
	@JsonProperty("base_cost")
	private Integer baseCost;
	@JsonProperty("base_discount_amount")
	private Integer baseDiscountAmount;
	@JsonProperty("base_discount_invoiced")
	private Integer baseDiscountInvoiced;
	@JsonProperty("base_discount_refunded")
	private Integer baseDiscountRefunded;
	@JsonProperty("base_discount_tax_compensation_amount")
	private Integer baseDiscountTaxCompensationAmount;
	@JsonProperty("base_discount_tax_compensation_invoiced")
	private Integer baseDiscountTaxCompensationInvoiced;
	@JsonProperty("base_discount_tax_compensation_refunded")
	private Integer baseDiscountTaxCompensationRefunded;
	@JsonProperty("base_original_price")
	private Integer baseOriginalPrice;
	@JsonProperty("base_price")
	private Integer basePrice;
	@JsonProperty("base_price_incl_tax")
	private Integer basePriceInclTax;
	@JsonProperty("base_row_invoiced")
	private Integer baseRowInvoiced;
	@JsonProperty("base_row_total")
	private Integer baseRowTotal;
	@JsonProperty("base_row_total_incl_tax")
	private Integer baseRowTotalInclTax;
	@JsonProperty("base_tax_amount")
	private Integer baseTaxAmount;
	@JsonProperty("base_tax_before_discount")
	private Integer baseTaxBeforeDiscount;
	@JsonProperty("base_tax_invoiced")
	private Integer baseTaxInvoiced;
	@JsonProperty("base_tax_refunded")
	private Integer baseTaxRefunded;
	@JsonProperty("base_weee_tax_applied_amount")
	private Integer baseWeeeTaxAppliedAmount;
	@JsonProperty("base_weee_tax_applied_row_amnt")
	private Integer baseWeeeTaxAppliedRowAmnt;
	@JsonProperty("base_weee_tax_disposition")
	private Integer baseWeeeTaxDisposition;
	@JsonProperty("base_weee_tax_row_disposition")
	private Integer baseWeeeTaxRowDisposition;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("description")
	private String description;
	@JsonProperty("discount_amount")
	private Integer discountAmount;
	@JsonProperty("discount_invoiced")
	private Integer discountInvoiced;
	@JsonProperty("discount_percent")
	private Integer discountPercent;
	@JsonProperty("discount_refunded")
	private Integer discountRefunded;
	@JsonProperty("event_id")
	private Integer eventId;
	@JsonProperty("ext_order_item_id")
	private String extOrderItemId;
	@JsonProperty("free_shipping")
	private Integer freeShipping;
	@JsonProperty("gw_base_price")
	private Integer gwBasePrice;
	@JsonProperty("gw_base_price_invoiced")
	private Integer gwBasePriceInvoiced;
	@JsonProperty("gw_base_price_refunded")
	private Integer gwBasePriceRefunded;
	@JsonProperty("gw_base_tax_amount")
	private Integer gwBaseTaxAmount;
	@JsonProperty("gw_base_tax_amount_invoiced")
	private Integer gwBaseTaxAmountInvoiced;
	@JsonProperty("gw_base_tax_amount_refunded")
	private Integer gwBaseTaxAmountRefunded;
	@JsonProperty("gw_id")
	private Integer gwId;
	@JsonProperty("gw_price")
	private Integer gwPrice;
	@JsonProperty("gw_price_invoiced")
	private Integer gwPriceInvoiced;
	@JsonProperty("gw_price_refunded")
	private Integer gwPriceRefunded;
	@JsonProperty("gw_tax_amount")
	private Integer gwTaxAmount;
	@JsonProperty("gw_tax_amount_invoiced")
	private Integer gwTaxAmountInvoiced;
	@JsonProperty("gw_tax_amount_refunded")
	private Integer gwTaxAmountRefunded;
	@JsonProperty("discount_tax_compensation_amount")
	private Integer discountTaxCompensationAmount;
	@JsonProperty("discount_tax_compensation_canceled")
	private Integer discountTaxCompensationCanceled;
	@JsonProperty("discount_tax_compensation_invoiced")
	private Integer discountTaxCompensationInvoiced;
	@JsonProperty("discount_tax_compensation_refunded")
	private Integer discountTaxCompensationRefunded;
	@JsonProperty("is_qty_decimal")
	private Integer isQtyDecimal;
	@JsonProperty("is_virtual")
	private Integer isVirtual;
	@JsonProperty("item_id")
	private Integer itemId;
	@JsonProperty("locked_do_invoice")
	private Integer lockedDoInvoice;
	@JsonProperty("locked_do_ship")
	private Integer lockedDoShip;
	@JsonProperty("name")
	private String name;
	@JsonProperty("no_discount")
	private Integer noDiscount;
	@JsonProperty("order_id")
	private Integer orderId;
	@JsonProperty("original_price")
	private Integer originalPrice;
	@JsonProperty("parent_item_id")
	private Integer parentItemId;
	@JsonProperty("price")
	private Integer price;
	@JsonProperty("price_incl_tax")
	private Integer priceInclTax;
	@JsonProperty("product_id")
	private Integer productId;
	@JsonProperty("product_type")
	private String productType;
	@JsonProperty("qty_backordered")
	private Integer qtyBackordered;
	@JsonProperty("qty_canceled")
	private Integer qtyCanceled;
	@JsonProperty("qty_invoiced")
	private Integer qtyInvoiced;
	@JsonProperty("qty_ordered")
	private Integer qtyOrdered;
	@JsonProperty("qty_refunded")
	private Integer qtyRefunded;
	@JsonProperty("qty_returned")
	private Integer qtyReturned;
	@JsonProperty("qty_shipped")
	private Integer qtyShipped;
	@JsonProperty("quote_item_id")
	private Integer quoteItemId;
	@JsonProperty("row_invoiced")
	private Integer rowInvoiced;
	@JsonProperty("row_total")
	private Integer rowTotal;
	@JsonProperty("row_total_incl_tax")
	private Integer rowTotalInclTax;
	@JsonProperty("row_weight")
	private Integer rowWeight;
	@JsonProperty("sku")
	private String sku;
	@JsonProperty("store_id")
	private Integer storeId;
	@JsonProperty("tax_amount")
	private Integer taxAmount;
	@JsonProperty("tax_before_discount")
	private Integer taxBeforeDiscount;
	@JsonProperty("tax_canceled")
	private Integer taxCanceled;
	@JsonProperty("tax_invoiced")
	private Integer taxInvoiced;
	@JsonProperty("tax_percent")
	private Integer taxPercent;
	@JsonProperty("tax_refunded")
	private Integer taxRefunded;
	@JsonProperty("updated_at")
	private String updatedAt;
	@JsonProperty("weee_tax_applied")
	private String weeeTaxApplied;
	@JsonProperty("weee_tax_applied_amount")
	private Integer weeeTaxAppliedAmount;
	@JsonProperty("weee_tax_applied_row_amount")
	private Integer weeeTaxAppliedRowAmount;
	@JsonProperty("weee_tax_disposition")
	private Integer weeeTaxDisposition;
	@JsonProperty("weee_tax_row_disposition")
	private Integer weeeTaxRowDisposition;
	@JsonProperty("weight")
	private Integer weight;
	@JsonProperty("parent_item")
	private ParentItem_ parentItem;
	@JsonProperty("product_option")
	private ProductOption_ productOption;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes___________________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("additional_data")
	public String getAdditionalData() {
		return additionalData;
	}

	@JsonProperty("additional_data")
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	@JsonProperty("amount_refunded")
	public Integer getAmountRefunded() {
		return amountRefunded;
	}

	@JsonProperty("amount_refunded")
	public void setAmountRefunded(Integer amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	@JsonProperty("applied_rule_ids")
	public String getAppliedRuleIds() {
		return appliedRuleIds;
	}

	@JsonProperty("applied_rule_ids")
	public void setAppliedRuleIds(String appliedRuleIds) {
		this.appliedRuleIds = appliedRuleIds;
	}

	@JsonProperty("base_amount_refunded")
	public Integer getBaseAmountRefunded() {
		return baseAmountRefunded;
	}

	@JsonProperty("base_amount_refunded")
	public void setBaseAmountRefunded(Integer baseAmountRefunded) {
		this.baseAmountRefunded = baseAmountRefunded;
	}

	@JsonProperty("base_cost")
	public Integer getBaseCost() {
		return baseCost;
	}

	@JsonProperty("base_cost")
	public void setBaseCost(Integer baseCost) {
		this.baseCost = baseCost;
	}

	@JsonProperty("base_discount_amount")
	public Integer getBaseDiscountAmount() {
		return baseDiscountAmount;
	}

	@JsonProperty("base_discount_amount")
	public void setBaseDiscountAmount(Integer baseDiscountAmount) {
		this.baseDiscountAmount = baseDiscountAmount;
	}

	@JsonProperty("base_discount_invoiced")
	public Integer getBaseDiscountInvoiced() {
		return baseDiscountInvoiced;
	}

	@JsonProperty("base_discount_invoiced")
	public void setBaseDiscountInvoiced(Integer baseDiscountInvoiced) {
		this.baseDiscountInvoiced = baseDiscountInvoiced;
	}

	@JsonProperty("base_discount_refunded")
	public Integer getBaseDiscountRefunded() {
		return baseDiscountRefunded;
	}

	@JsonProperty("base_discount_refunded")
	public void setBaseDiscountRefunded(Integer baseDiscountRefunded) {
		this.baseDiscountRefunded = baseDiscountRefunded;
	}

	@JsonProperty("base_discount_tax_compensation_amount")
	public Integer getBaseDiscountTaxCompensationAmount() {
		return baseDiscountTaxCompensationAmount;
	}

	@JsonProperty("base_discount_tax_compensation_amount")
	public void setBaseDiscountTaxCompensationAmount(Integer baseDiscountTaxCompensationAmount) {
		this.baseDiscountTaxCompensationAmount = baseDiscountTaxCompensationAmount;
	}

	@JsonProperty("base_discount_tax_compensation_invoiced")
	public Integer getBaseDiscountTaxCompensationInvoiced() {
		return baseDiscountTaxCompensationInvoiced;
	}

	@JsonProperty("base_discount_tax_compensation_invoiced")
	public void setBaseDiscountTaxCompensationInvoiced(Integer baseDiscountTaxCompensationInvoiced) {
		this.baseDiscountTaxCompensationInvoiced = baseDiscountTaxCompensationInvoiced;
	}

	@JsonProperty("base_discount_tax_compensation_refunded")
	public Integer getBaseDiscountTaxCompensationRefunded() {
		return baseDiscountTaxCompensationRefunded;
	}

	@JsonProperty("base_discount_tax_compensation_refunded")
	public void setBaseDiscountTaxCompensationRefunded(Integer baseDiscountTaxCompensationRefunded) {
		this.baseDiscountTaxCompensationRefunded = baseDiscountTaxCompensationRefunded;
	}

	@JsonProperty("base_original_price")
	public Integer getBaseOriginalPrice() {
		return baseOriginalPrice;
	}

	@JsonProperty("base_original_price")
	public void setBaseOriginalPrice(Integer baseOriginalPrice) {
		this.baseOriginalPrice = baseOriginalPrice;
	}

	@JsonProperty("base_price")
	public Integer getBasePrice() {
		return basePrice;
	}

	@JsonProperty("base_price")
	public void setBasePrice(Integer basePrice) {
		this.basePrice = basePrice;
	}

	@JsonProperty("base_price_incl_tax")
	public Integer getBasePriceInclTax() {
		return basePriceInclTax;
	}

	@JsonProperty("base_price_incl_tax")
	public void setBasePriceInclTax(Integer basePriceInclTax) {
		this.basePriceInclTax = basePriceInclTax;
	}

	@JsonProperty("base_row_invoiced")
	public Integer getBaseRowInvoiced() {
		return baseRowInvoiced;
	}

	@JsonProperty("base_row_invoiced")
	public void setBaseRowInvoiced(Integer baseRowInvoiced) {
		this.baseRowInvoiced = baseRowInvoiced;
	}

	@JsonProperty("base_row_total")
	public Integer getBaseRowTotal() {
		return baseRowTotal;
	}

	@JsonProperty("base_row_total")
	public void setBaseRowTotal(Integer baseRowTotal) {
		this.baseRowTotal = baseRowTotal;
	}

	@JsonProperty("base_row_total_incl_tax")
	public Integer getBaseRowTotalInclTax() {
		return baseRowTotalInclTax;
	}

	@JsonProperty("base_row_total_incl_tax")
	public void setBaseRowTotalInclTax(Integer baseRowTotalInclTax) {
		this.baseRowTotalInclTax = baseRowTotalInclTax;
	}

	@JsonProperty("base_tax_amount")
	public Integer getBaseTaxAmount() {
		return baseTaxAmount;
	}

	@JsonProperty("base_tax_amount")
	public void setBaseTaxAmount(Integer baseTaxAmount) {
		this.baseTaxAmount = baseTaxAmount;
	}

	@JsonProperty("base_tax_before_discount")
	public Integer getBaseTaxBeforeDiscount() {
		return baseTaxBeforeDiscount;
	}

	@JsonProperty("base_tax_before_discount")
	public void setBaseTaxBeforeDiscount(Integer baseTaxBeforeDiscount) {
		this.baseTaxBeforeDiscount = baseTaxBeforeDiscount;
	}

	@JsonProperty("base_tax_invoiced")
	public Integer getBaseTaxInvoiced() {
		return baseTaxInvoiced;
	}

	@JsonProperty("base_tax_invoiced")
	public void setBaseTaxInvoiced(Integer baseTaxInvoiced) {
		this.baseTaxInvoiced = baseTaxInvoiced;
	}

	@JsonProperty("base_tax_refunded")
	public Integer getBaseTaxRefunded() {
		return baseTaxRefunded;
	}

	@JsonProperty("base_tax_refunded")
	public void setBaseTaxRefunded(Integer baseTaxRefunded) {
		this.baseTaxRefunded = baseTaxRefunded;
	}

	@JsonProperty("base_weee_tax_applied_amount")
	public Integer getBaseWeeeTaxAppliedAmount() {
		return baseWeeeTaxAppliedAmount;
	}

	@JsonProperty("base_weee_tax_applied_amount")
	public void setBaseWeeeTaxAppliedAmount(Integer baseWeeeTaxAppliedAmount) {
		this.baseWeeeTaxAppliedAmount = baseWeeeTaxAppliedAmount;
	}

	@JsonProperty("base_weee_tax_applied_row_amnt")
	public Integer getBaseWeeeTaxAppliedRowAmnt() {
		return baseWeeeTaxAppliedRowAmnt;
	}

	@JsonProperty("base_weee_tax_applied_row_amnt")
	public void setBaseWeeeTaxAppliedRowAmnt(Integer baseWeeeTaxAppliedRowAmnt) {
		this.baseWeeeTaxAppliedRowAmnt = baseWeeeTaxAppliedRowAmnt;
	}

	@JsonProperty("base_weee_tax_disposition")
	public Integer getBaseWeeeTaxDisposition() {
		return baseWeeeTaxDisposition;
	}

	@JsonProperty("base_weee_tax_disposition")
	public void setBaseWeeeTaxDisposition(Integer baseWeeeTaxDisposition) {
		this.baseWeeeTaxDisposition = baseWeeeTaxDisposition;
	}

	@JsonProperty("base_weee_tax_row_disposition")
	public Integer getBaseWeeeTaxRowDisposition() {
		return baseWeeeTaxRowDisposition;
	}

	@JsonProperty("base_weee_tax_row_disposition")
	public void setBaseWeeeTaxRowDisposition(Integer baseWeeeTaxRowDisposition) {
		this.baseWeeeTaxRowDisposition = baseWeeeTaxRowDisposition;
	}

	@JsonProperty("created_at")
	public String getCreatedAt() {
		return createdAt;
	}

	@JsonProperty("created_at")
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("discount_amount")
	public Integer getDiscountAmount() {
		return discountAmount;
	}

	@JsonProperty("discount_amount")
	public void setDiscountAmount(Integer discountAmount) {
		this.discountAmount = discountAmount;
	}

	@JsonProperty("discount_invoiced")
	public Integer getDiscountInvoiced() {
		return discountInvoiced;
	}

	@JsonProperty("discount_invoiced")
	public void setDiscountInvoiced(Integer discountInvoiced) {
		this.discountInvoiced = discountInvoiced;
	}

	@JsonProperty("discount_percent")
	public Integer getDiscountPercent() {
		return discountPercent;
	}

	@JsonProperty("discount_percent")
	public void setDiscountPercent(Integer discountPercent) {
		this.discountPercent = discountPercent;
	}

	@JsonProperty("discount_refunded")
	public Integer getDiscountRefunded() {
		return discountRefunded;
	}

	@JsonProperty("discount_refunded")
	public void setDiscountRefunded(Integer discountRefunded) {
		this.discountRefunded = discountRefunded;
	}

	@JsonProperty("event_id")
	public Integer getEventId() {
		return eventId;
	}

	@JsonProperty("event_id")
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	@JsonProperty("ext_order_item_id")
	public String getExtOrderItemId() {
		return extOrderItemId;
	}

	@JsonProperty("ext_order_item_id")
	public void setExtOrderItemId(String extOrderItemId) {
		this.extOrderItemId = extOrderItemId;
	}

	@JsonProperty("free_shipping")
	public Integer getFreeShipping() {
		return freeShipping;
	}

	@JsonProperty("free_shipping")
	public void setFreeShipping(Integer freeShipping) {
		this.freeShipping = freeShipping;
	}

	@JsonProperty("gw_base_price")
	public Integer getGwBasePrice() {
		return gwBasePrice;
	}

	@JsonProperty("gw_base_price")
	public void setGwBasePrice(Integer gwBasePrice) {
		this.gwBasePrice = gwBasePrice;
	}

	@JsonProperty("gw_base_price_invoiced")
	public Integer getGwBasePriceInvoiced() {
		return gwBasePriceInvoiced;
	}

	@JsonProperty("gw_base_price_invoiced")
	public void setGwBasePriceInvoiced(Integer gwBasePriceInvoiced) {
		this.gwBasePriceInvoiced = gwBasePriceInvoiced;
	}

	@JsonProperty("gw_base_price_refunded")
	public Integer getGwBasePriceRefunded() {
		return gwBasePriceRefunded;
	}

	@JsonProperty("gw_base_price_refunded")
	public void setGwBasePriceRefunded(Integer gwBasePriceRefunded) {
		this.gwBasePriceRefunded = gwBasePriceRefunded;
	}

	@JsonProperty("gw_base_tax_amount")
	public Integer getGwBaseTaxAmount() {
		return gwBaseTaxAmount;
	}

	@JsonProperty("gw_base_tax_amount")
	public void setGwBaseTaxAmount(Integer gwBaseTaxAmount) {
		this.gwBaseTaxAmount = gwBaseTaxAmount;
	}

	@JsonProperty("gw_base_tax_amount_invoiced")
	public Integer getGwBaseTaxAmountInvoiced() {
		return gwBaseTaxAmountInvoiced;
	}

	@JsonProperty("gw_base_tax_amount_invoiced")
	public void setGwBaseTaxAmountInvoiced(Integer gwBaseTaxAmountInvoiced) {
		this.gwBaseTaxAmountInvoiced = gwBaseTaxAmountInvoiced;
	}

	@JsonProperty("gw_base_tax_amount_refunded")
	public Integer getGwBaseTaxAmountRefunded() {
		return gwBaseTaxAmountRefunded;
	}

	@JsonProperty("gw_base_tax_amount_refunded")
	public void setGwBaseTaxAmountRefunded(Integer gwBaseTaxAmountRefunded) {
		this.gwBaseTaxAmountRefunded = gwBaseTaxAmountRefunded;
	}

	@JsonProperty("gw_id")
	public Integer getGwId() {
		return gwId;
	}

	@JsonProperty("gw_id")
	public void setGwId(Integer gwId) {
		this.gwId = gwId;
	}

	@JsonProperty("gw_price")
	public Integer getGwPrice() {
		return gwPrice;
	}

	@JsonProperty("gw_price")
	public void setGwPrice(Integer gwPrice) {
		this.gwPrice = gwPrice;
	}

	@JsonProperty("gw_price_invoiced")
	public Integer getGwPriceInvoiced() {
		return gwPriceInvoiced;
	}

	@JsonProperty("gw_price_invoiced")
	public void setGwPriceInvoiced(Integer gwPriceInvoiced) {
		this.gwPriceInvoiced = gwPriceInvoiced;
	}

	@JsonProperty("gw_price_refunded")
	public Integer getGwPriceRefunded() {
		return gwPriceRefunded;
	}

	@JsonProperty("gw_price_refunded")
	public void setGwPriceRefunded(Integer gwPriceRefunded) {
		this.gwPriceRefunded = gwPriceRefunded;
	}

	@JsonProperty("gw_tax_amount")
	public Integer getGwTaxAmount() {
		return gwTaxAmount;
	}

	@JsonProperty("gw_tax_amount")
	public void setGwTaxAmount(Integer gwTaxAmount) {
		this.gwTaxAmount = gwTaxAmount;
	}

	@JsonProperty("gw_tax_amount_invoiced")
	public Integer getGwTaxAmountInvoiced() {
		return gwTaxAmountInvoiced;
	}

	@JsonProperty("gw_tax_amount_invoiced")
	public void setGwTaxAmountInvoiced(Integer gwTaxAmountInvoiced) {
		this.gwTaxAmountInvoiced = gwTaxAmountInvoiced;
	}

	@JsonProperty("gw_tax_amount_refunded")
	public Integer getGwTaxAmountRefunded() {
		return gwTaxAmountRefunded;
	}

	@JsonProperty("gw_tax_amount_refunded")
	public void setGwTaxAmountRefunded(Integer gwTaxAmountRefunded) {
		this.gwTaxAmountRefunded = gwTaxAmountRefunded;
	}

	@JsonProperty("discount_tax_compensation_amount")
	public Integer getDiscountTaxCompensationAmount() {
		return discountTaxCompensationAmount;
	}

	@JsonProperty("discount_tax_compensation_amount")
	public void setDiscountTaxCompensationAmount(Integer discountTaxCompensationAmount) {
		this.discountTaxCompensationAmount = discountTaxCompensationAmount;
	}

	@JsonProperty("discount_tax_compensation_canceled")
	public Integer getDiscountTaxCompensationCanceled() {
		return discountTaxCompensationCanceled;
	}

	@JsonProperty("discount_tax_compensation_canceled")
	public void setDiscountTaxCompensationCanceled(Integer discountTaxCompensationCanceled) {
		this.discountTaxCompensationCanceled = discountTaxCompensationCanceled;
	}

	@JsonProperty("discount_tax_compensation_invoiced")
	public Integer getDiscountTaxCompensationInvoiced() {
		return discountTaxCompensationInvoiced;
	}

	@JsonProperty("discount_tax_compensation_invoiced")
	public void setDiscountTaxCompensationInvoiced(Integer discountTaxCompensationInvoiced) {
		this.discountTaxCompensationInvoiced = discountTaxCompensationInvoiced;
	}

	@JsonProperty("discount_tax_compensation_refunded")
	public Integer getDiscountTaxCompensationRefunded() {
		return discountTaxCompensationRefunded;
	}

	@JsonProperty("discount_tax_compensation_refunded")
	public void setDiscountTaxCompensationRefunded(Integer discountTaxCompensationRefunded) {
		this.discountTaxCompensationRefunded = discountTaxCompensationRefunded;
	}

	@JsonProperty("is_qty_decimal")
	public Integer getIsQtyDecimal() {
		return isQtyDecimal;
	}

	@JsonProperty("is_qty_decimal")
	public void setIsQtyDecimal(Integer isQtyDecimal) {
		this.isQtyDecimal = isQtyDecimal;
	}

	@JsonProperty("is_virtual")
	public Integer getIsVirtual() {
		return isVirtual;
	}

	@JsonProperty("is_virtual")
	public void setIsVirtual(Integer isVirtual) {
		this.isVirtual = isVirtual;
	}

	@JsonProperty("item_id")
	public Integer getItemId() {
		return itemId;
	}

	@JsonProperty("item_id")
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	@JsonProperty("locked_do_invoice")
	public Integer getLockedDoInvoice() {
		return lockedDoInvoice;
	}

	@JsonProperty("locked_do_invoice")
	public void setLockedDoInvoice(Integer lockedDoInvoice) {
		this.lockedDoInvoice = lockedDoInvoice;
	}

	@JsonProperty("locked_do_ship")
	public Integer getLockedDoShip() {
		return lockedDoShip;
	}

	@JsonProperty("locked_do_ship")
	public void setLockedDoShip(Integer lockedDoShip) {
		this.lockedDoShip = lockedDoShip;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("no_discount")
	public Integer getNoDiscount() {
		return noDiscount;
	}

	@JsonProperty("no_discount")
	public void setNoDiscount(Integer noDiscount) {
		this.noDiscount = noDiscount;
	}

	@JsonProperty("order_id")
	public Integer getOrderId() {
		return orderId;
	}

	@JsonProperty("order_id")
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	@JsonProperty("original_price")
	public Integer getOriginalPrice() {
		return originalPrice;
	}

	@JsonProperty("original_price")
	public void setOriginalPrice(Integer originalPrice) {
		this.originalPrice = originalPrice;
	}

	@JsonProperty("parent_item_id")
	public Integer getParentItemId() {
		return parentItemId;
	}

	@JsonProperty("parent_item_id")
	public void setParentItemId(Integer parentItemId) {
		this.parentItemId = parentItemId;
	}

	@JsonProperty("price")
	public Integer getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(Integer price) {
		this.price = price;
	}

	@JsonProperty("price_incl_tax")
	public Integer getPriceInclTax() {
		return priceInclTax;
	}

	@JsonProperty("price_incl_tax")
	public void setPriceInclTax(Integer priceInclTax) {
		this.priceInclTax = priceInclTax;
	}

	@JsonProperty("product_id")
	public Integer getProductId() {
		return productId;
	}

	@JsonProperty("product_id")
	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	@JsonProperty("product_type")
	public String getProductType() {
		return productType;
	}

	@JsonProperty("product_type")
	public void setProductType(String productType) {
		this.productType = productType;
	}

	@JsonProperty("qty_backordered")
	public Integer getQtyBackordered() {
		return qtyBackordered;
	}

	@JsonProperty("qty_backordered")
	public void setQtyBackordered(Integer qtyBackordered) {
		this.qtyBackordered = qtyBackordered;
	}

	@JsonProperty("qty_canceled")
	public Integer getQtyCanceled() {
		return qtyCanceled;
	}

	@JsonProperty("qty_canceled")
	public void setQtyCanceled(Integer qtyCanceled) {
		this.qtyCanceled = qtyCanceled;
	}

	@JsonProperty("qty_invoiced")
	public Integer getQtyInvoiced() {
		return qtyInvoiced;
	}

	@JsonProperty("qty_invoiced")
	public void setQtyInvoiced(Integer qtyInvoiced) {
		this.qtyInvoiced = qtyInvoiced;
	}

	@JsonProperty("qty_ordered")
	public Integer getQtyOrdered() {
		return qtyOrdered;
	}

	@JsonProperty("qty_ordered")
	public void setQtyOrdered(Integer qtyOrdered) {
		this.qtyOrdered = qtyOrdered;
	}

	@JsonProperty("qty_refunded")
	public Integer getQtyRefunded() {
		return qtyRefunded;
	}

	@JsonProperty("qty_refunded")
	public void setQtyRefunded(Integer qtyRefunded) {
		this.qtyRefunded = qtyRefunded;
	}

	@JsonProperty("qty_returned")
	public Integer getQtyReturned() {
		return qtyReturned;
	}

	@JsonProperty("qty_returned")
	public void setQtyReturned(Integer qtyReturned) {
		this.qtyReturned = qtyReturned;
	}

	@JsonProperty("qty_shipped")
	public Integer getQtyShipped() {
		return qtyShipped;
	}

	@JsonProperty("qty_shipped")
	public void setQtyShipped(Integer qtyShipped) {
		this.qtyShipped = qtyShipped;
	}

	@JsonProperty("quote_item_id")
	public Integer getQuoteItemId() {
		return quoteItemId;
	}

	@JsonProperty("quote_item_id")
	public void setQuoteItemId(Integer quoteItemId) {
		this.quoteItemId = quoteItemId;
	}

	@JsonProperty("row_invoiced")
	public Integer getRowInvoiced() {
		return rowInvoiced;
	}

	@JsonProperty("row_invoiced")
	public void setRowInvoiced(Integer rowInvoiced) {
		this.rowInvoiced = rowInvoiced;
	}

	@JsonProperty("row_total")
	public Integer getRowTotal() {
		return rowTotal;
	}

	@JsonProperty("row_total")
	public void setRowTotal(Integer rowTotal) {
		this.rowTotal = rowTotal;
	}

	@JsonProperty("row_total_incl_tax")
	public Integer getRowTotalInclTax() {
		return rowTotalInclTax;
	}

	@JsonProperty("row_total_incl_tax")
	public void setRowTotalInclTax(Integer rowTotalInclTax) {
		this.rowTotalInclTax = rowTotalInclTax;
	}

	@JsonProperty("row_weight")
	public Integer getRowWeight() {
		return rowWeight;
	}

	@JsonProperty("row_weight")
	public void setRowWeight(Integer rowWeight) {
		this.rowWeight = rowWeight;
	}

	@JsonProperty("sku")
	public String getSku() {
		return sku;
	}

	@JsonProperty("sku")
	public void setSku(String sku) {
		this.sku = sku;
	}

	@JsonProperty("store_id")
	public Integer getStoreId() {
		return storeId;
	}

	@JsonProperty("store_id")
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	@JsonProperty("tax_amount")
	public Integer getTaxAmount() {
		return taxAmount;
	}

	@JsonProperty("tax_amount")
	public void setTaxAmount(Integer taxAmount) {
		this.taxAmount = taxAmount;
	}

	@JsonProperty("tax_before_discount")
	public Integer getTaxBeforeDiscount() {
		return taxBeforeDiscount;
	}

	@JsonProperty("tax_before_discount")
	public void setTaxBeforeDiscount(Integer taxBeforeDiscount) {
		this.taxBeforeDiscount = taxBeforeDiscount;
	}

	@JsonProperty("tax_canceled")
	public Integer getTaxCanceled() {
		return taxCanceled;
	}

	@JsonProperty("tax_canceled")
	public void setTaxCanceled(Integer taxCanceled) {
		this.taxCanceled = taxCanceled;
	}

	@JsonProperty("tax_invoiced")
	public Integer getTaxInvoiced() {
		return taxInvoiced;
	}

	@JsonProperty("tax_invoiced")
	public void setTaxInvoiced(Integer taxInvoiced) {
		this.taxInvoiced = taxInvoiced;
	}

	@JsonProperty("tax_percent")
	public Integer getTaxPercent() {
		return taxPercent;
	}

	@JsonProperty("tax_percent")
	public void setTaxPercent(Integer taxPercent) {
		this.taxPercent = taxPercent;
	}

	@JsonProperty("tax_refunded")
	public Integer getTaxRefunded() {
		return taxRefunded;
	}

	@JsonProperty("tax_refunded")
	public void setTaxRefunded(Integer taxRefunded) {
		this.taxRefunded = taxRefunded;
	}

	@JsonProperty("updated_at")
	public String getUpdatedAt() {
		return updatedAt;
	}

	@JsonProperty("updated_at")
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@JsonProperty("weee_tax_applied")
	public String getWeeeTaxApplied() {
		return weeeTaxApplied;
	}

	@JsonProperty("weee_tax_applied")
	public void setWeeeTaxApplied(String weeeTaxApplied) {
		this.weeeTaxApplied = weeeTaxApplied;
	}

	@JsonProperty("weee_tax_applied_amount")
	public Integer getWeeeTaxAppliedAmount() {
		return weeeTaxAppliedAmount;
	}

	@JsonProperty("weee_tax_applied_amount")
	public void setWeeeTaxAppliedAmount(Integer weeeTaxAppliedAmount) {
		this.weeeTaxAppliedAmount = weeeTaxAppliedAmount;
	}

	@JsonProperty("weee_tax_applied_row_amount")
	public Integer getWeeeTaxAppliedRowAmount() {
		return weeeTaxAppliedRowAmount;
	}

	@JsonProperty("weee_tax_applied_row_amount")
	public void setWeeeTaxAppliedRowAmount(Integer weeeTaxAppliedRowAmount) {
		this.weeeTaxAppliedRowAmount = weeeTaxAppliedRowAmount;
	}

	@JsonProperty("weee_tax_disposition")
	public Integer getWeeeTaxDisposition() {
		return weeeTaxDisposition;
	}

	@JsonProperty("weee_tax_disposition")
	public void setWeeeTaxDisposition(Integer weeeTaxDisposition) {
		this.weeeTaxDisposition = weeeTaxDisposition;
	}

	@JsonProperty("weee_tax_row_disposition")
	public Integer getWeeeTaxRowDisposition() {
		return weeeTaxRowDisposition;
	}

	@JsonProperty("weee_tax_row_disposition")
	public void setWeeeTaxRowDisposition(Integer weeeTaxRowDisposition) {
		this.weeeTaxRowDisposition = weeeTaxRowDisposition;
	}

	@JsonProperty("weight")
	public Integer getWeight() {
		return weight;
	}

	@JsonProperty("weight")
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	@JsonProperty("parent_item")
	public ParentItem_ getParentItem() {
		return parentItem;
	}

	@JsonProperty("parent_item")
	public void setParentItem(ParentItem_ parentItem) {
		this.parentItem = parentItem;
	}

	@JsonProperty("product_option")
	public ProductOption_ getProductOption() {
		return productOption;
	}

	@JsonProperty("product_option")
	public void setProductOption(ProductOption_ productOption) {
		this.productOption = productOption;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes___________________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes___________________ extensionAttributes) {
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
