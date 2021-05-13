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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Getter
@Setter
public class MagentoOrder {
	@JsonProperty("adjustment_negative")
	private Integer adjustmentNegative;
	@JsonProperty("adjustment_positive")
	private Integer adjustmentPositive;
	@JsonProperty("applied_rule_ids")
	private String appliedRuleIds;
	@JsonProperty("base_adjustment_negative")
	private Integer baseAdjustmentNegative;
	@JsonProperty("base_adjustment_positive")
	private Integer baseAdjustmentPositive;
	@JsonProperty("base_currency_code")
	private String baseCurrencyCode;
	@JsonProperty("base_discount_amount")
	private Integer baseDiscountAmount;
	@JsonProperty("base_discount_canceled")
	private Integer baseDiscountCanceled;
	@JsonProperty("base_discount_invoiced")
	private Integer baseDiscountInvoiced;
	@JsonProperty("base_discount_refunded")
	private Integer baseDiscountRefunded;
	@JsonProperty("base_grand_total")
	private Integer baseGrandTotal;
	@JsonProperty("base_discount_tax_compensation_amount")
	private Integer baseDiscountTaxCompensationAmount;
	@JsonProperty("base_discount_tax_compensation_invoiced")
	private Integer baseDiscountTaxCompensationInvoiced;
	@JsonProperty("base_discount_tax_compensation_refunded")
	private Integer baseDiscountTaxCompensationRefunded;
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
	@JsonProperty("base_subtotal")
	private Integer baseSubtotal;
	@JsonProperty("base_subtotal_canceled")
	private Integer baseSubtotalCanceled;
	@JsonProperty("base_subtotal_incl_tax")
	private Integer baseSubtotalInclTax;
	@JsonProperty("base_subtotal_invoiced")
	private Integer baseSubtotalInvoiced;
	@JsonProperty("base_subtotal_refunded")
	private Integer baseSubtotalRefunded;
	@JsonProperty("base_tax_amount")
	private Integer baseTaxAmount;
	@JsonProperty("base_tax_canceled")
	private Integer baseTaxCanceled;
	@JsonProperty("base_tax_invoiced")
	private Integer baseTaxInvoiced;
	@JsonProperty("base_tax_refunded")
	private Integer baseTaxRefunded;
	@JsonProperty("base_total_canceled")
	private Integer baseTotalCanceled;
	@JsonProperty("base_total_due")
	private Integer baseTotalDue;
	@JsonProperty("base_total_invoiced")
	private Integer baseTotalInvoiced;
	@JsonProperty("base_total_invoiced_cost")
	private Integer baseTotalInvoicedCost;
	@JsonProperty("base_total_offline_refunded")
	private Integer baseTotalOfflineRefunded;
	@JsonProperty("base_total_online_refunded")
	private Integer baseTotalOnlineRefunded;
	@JsonProperty("base_total_paid")
	private Integer baseTotalPaid;
	@JsonProperty("base_total_qty_ordered")
	private Integer baseTotalQtyOrdered;
	@JsonProperty("base_total_refunded")
	private Integer baseTotalRefunded;
	@JsonProperty("base_to_global_rate")
	private Integer baseToGlobalRate;
	@JsonProperty("base_to_order_rate")
	private Integer baseToOrderRate;
	@JsonProperty("billing_address_id")
	private Integer billingAddressId;
	@JsonProperty("can_ship_partially")
	private Integer canShipPartially;
	@JsonProperty("can_ship_partially_item")
	private Integer canShipPartiallyItem;
	@JsonProperty("coupon_code")
	private String couponCode;
	@JsonProperty("created_at")
	private String createdAt;
	@JsonProperty("customer_dob")
	private String customerDob;
	@JsonProperty("customer_email")
	private String customerEmail;
	@JsonProperty("customer_firstname")
	private String customerFirstname;
	@JsonProperty("customer_gender")
	private Integer customerGender;
	@JsonProperty("customer_group_id")
	private Integer customerGroupId;
	@JsonProperty("customer_id")
	private Integer customerId;
	@JsonProperty("customer_is_guest")
	private Integer customerIsGuest;
	@JsonProperty("customer_lastname")
	private String customerLastname;
	@JsonProperty("customer_middlename")
	private String customerMiddlename;
	@JsonProperty("customer_note")
	private String customerNote;
	@JsonProperty("customer_note_notify")
	private Integer customerNoteNotify;
	@JsonProperty("customer_prefix")
	private String customerPrefix;
	@JsonProperty("customer_suffix")
	private String customerSuffix;
	@JsonProperty("customer_taxvat")
	private String customerTaxvat;
	@JsonProperty("discount_amount")
	private Integer discountAmount;
	@JsonProperty("discount_canceled")
	private Integer discountCanceled;
	@JsonProperty("discount_description")
	private String discountDescription;
	@JsonProperty("discount_invoiced")
	private Integer discountInvoiced;
	@JsonProperty("discount_refunded")
	private Integer discountRefunded;
	@JsonProperty("edit_increment")
	private Integer editIncrement;
	@JsonProperty("email_sent")
	private Integer emailSent;
	@JsonProperty("entity_id")
	private Integer entityId;
	@JsonProperty("ext_customer_id")
	private String extCustomerId;
	@JsonProperty("ext_order_id")
	private String extOrderId;
	@JsonProperty("forced_shipment_with_invoice")
	private Integer forcedShipmentWithInvoice;
	@JsonProperty("global_currency_code")
	private String globalCurrencyCode;
	@JsonProperty("grand_total")
	private Integer grandTotal;
	@JsonProperty("discount_tax_compensation_amount")
	private Integer discountTaxCompensationAmount;
	@JsonProperty("discount_tax_compensation_invoiced")
	private Integer discountTaxCompensationInvoiced;
	@JsonProperty("discount_tax_compensation_refunded")
	private Integer discountTaxCompensationRefunded;
	@JsonProperty("hold_before_state")
	private String holdBeforeState;
	@JsonProperty("hold_before_status")
	private String holdBeforeStatus;
	@JsonProperty("increment_id")
	private String incrementId;
	@JsonProperty("is_virtual")
	private Integer isVirtual;
	@JsonProperty("order_currency_code")
	private String orderCurrencyCode;
	@JsonProperty("original_increment_id")
	private String originalIncrementId;
	@JsonProperty("payment_authorization_amount")
	private Integer paymentAuthorizationAmount;
	@JsonProperty("payment_auth_expiration")
	private Integer paymentAuthExpiration;
	@JsonProperty("protect_code")
	private String protectCode;
	@JsonProperty("quote_address_id")
	private Integer quoteAddressId;
	@JsonProperty("quote_id")
	private Integer quoteId;
	@JsonProperty("relation_child_id")
	private String relationChildId;
	@JsonProperty("relation_child_real_id")
	private String relationChildRealId;
	@JsonProperty("relation_parent_id")
	private String relationParentId;
	@JsonProperty("relation_parent_real_id")
	private String relationParentRealId;
	@JsonProperty("remote_ip")
	private String remoteIp;
	@JsonProperty("shipping_amount")
	private Integer shippingAmount;
	@JsonProperty("shipping_canceled")
	private Integer shippingCanceled;
	@JsonProperty("shipping_description")
	private String shippingDescription;
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
	@JsonProperty("state")
	private String state;
	@JsonProperty("status")
	private String status;
	@JsonProperty("store_currency_code")
	private String storeCurrencyCode;
	@JsonProperty("store_id")
	private Integer storeId;
	@JsonProperty("store_name")
	private String storeName;
	@JsonProperty("store_to_base_rate")
	private Integer storeToBaseRate;
	@JsonProperty("store_to_order_rate")
	private Integer storeToOrderRate;
	@JsonProperty("subtotal")
	private Integer subtotal;
	@JsonProperty("subtotal_canceled")
	private Integer subtotalCanceled;
	@JsonProperty("subtotal_incl_tax")
	private Integer subtotalInclTax;
	@JsonProperty("subtotal_invoiced")
	private Integer subtotalInvoiced;
	@JsonProperty("subtotal_refunded")
	private Integer subtotalRefunded;
	@JsonProperty("tax_amount")
	private Integer taxAmount;
	@JsonProperty("tax_canceled")
	private Integer taxCanceled;
	@JsonProperty("tax_invoiced")
	private Integer taxInvoiced;
	@JsonProperty("tax_refunded")
	private Integer taxRefunded;
	@JsonProperty("total_canceled")
	private Integer totalCanceled;
	@JsonProperty("total_due")
	private Integer totalDue;
	@JsonProperty("total_invoiced")
	private Integer totalInvoiced;
	@JsonProperty("total_item_count")
	private Integer totalItemCount;
	@JsonProperty("total_offline_refunded")
	private Integer totalOfflineRefunded;
	@JsonProperty("total_online_refunded")
	private Integer totalOnlineRefunded;
	@JsonProperty("total_paid")
	private Integer totalPaid;
	@JsonProperty("total_qty_ordered")
	private Integer totalQtyOrdered;
	@JsonProperty("total_refunded")
	private Integer totalRefunded;
	@JsonProperty("updated_at")
	private String updatedAt;
	@JsonProperty("weight")
	private Integer weight;
	@JsonProperty("x_forwarded_for")
	private String xForwardedFor;
	@JsonProperty("items")
	private List<Item> items = null;
	@JsonProperty("billing_address")
	private BillingAddress billingAddress;
	@JsonProperty("payment")
	private Payment payment;
	@JsonProperty("status_histories")
	private List<StatusHistory> statusHistories = null;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes__________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

}
