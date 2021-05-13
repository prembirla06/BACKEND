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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {

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
	private Float priceInclTax;
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
	private ParentItem parentItem;
	@JsonProperty("product_option")
	private ProductOption productOption;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes_____ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
