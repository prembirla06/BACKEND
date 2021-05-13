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
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "shipping_assignments", "company_order_attributes", "base_customer_balance_amount",
		"customer_balance_amount", "base_customer_balance_invoiced", "customer_balance_invoiced",
		"base_customer_balance_refunded", "customer_balance_refunded", "base_customer_balance_total_refunded",
		"customer_balance_total_refunded", "gift_cards", "base_gift_cards_amount", "gift_cards_amount",
		"base_gift_cards_invoiced", "gift_cards_invoiced", "base_gift_cards_refunded", "gift_cards_refunded",
		"applied_taxes", "item_applied_taxes", "converting_from_quote", "gift_message", "gw_id",
		"gw_allow_gift_receipt", "gw_add_card", "gw_base_price", "gw_price", "gw_items_base_price", "gw_items_price",
		"gw_card_base_price", "gw_card_price", "gw_base_tax_amount", "gw_tax_amount", "gw_items_base_tax_amount",
		"gw_items_tax_amount", "gw_card_base_tax_amount", "gw_card_tax_amount", "gw_base_price_incl_tax",
		"gw_price_incl_tax", "gw_items_base_price_incl_tax", "gw_items_price_incl_tax", "gw_card_base_price_incl_tax",
		"gw_card_price_incl_tax", "gw_base_price_invoiced", "gw_price_invoiced", "gw_items_base_price_invoiced",
		"gw_items_price_invoiced", "gw_card_base_price_invoiced", "gw_card_price_invoiced",
		"gw_base_tax_amount_invoiced", "gw_tax_amount_invoiced", "gw_items_base_tax_invoiced", "gw_items_tax_invoiced",
		"gw_card_base_tax_invoiced", "gw_card_tax_invoiced", "gw_base_price_refunded", "gw_price_refunded",
		"gw_items_base_price_refunded", "gw_items_price_refunded", "gw_card_base_price_refunded",
		"gw_card_price_refunded", "gw_base_tax_amount_refunded", "gw_tax_amount_refunded", "gw_items_base_tax_refunded",
		"gw_items_tax_refunded", "gw_card_base_tax_refunded", "gw_card_tax_refunded" })
public class ExtensionAttributes__________ {

	@JsonProperty("shipping_assignments")
	private List<ShippingAssignment> shippingAssignments = null;
	@JsonProperty("company_order_attributes")
	private CompanyOrderAttributes companyOrderAttributes;
	@JsonProperty("base_customer_balance_amount")
	private Integer baseCustomerBalanceAmount;
	@JsonProperty("customer_balance_amount")
	private Integer customerBalanceAmount;
	@JsonProperty("base_customer_balance_invoiced")
	private Integer baseCustomerBalanceInvoiced;
	@JsonProperty("customer_balance_invoiced")
	private Integer customerBalanceInvoiced;
	@JsonProperty("base_customer_balance_refunded")
	private Integer baseCustomerBalanceRefunded;
	@JsonProperty("customer_balance_refunded")
	private Integer customerBalanceRefunded;
	@JsonProperty("base_customer_balance_total_refunded")
	private Integer baseCustomerBalanceTotalRefunded;
	@JsonProperty("customer_balance_total_refunded")
	private Integer customerBalanceTotalRefunded;
	@JsonProperty("gift_cards")
	private List<GiftCard> giftCards = null;
	@JsonProperty("base_gift_cards_amount")
	private Integer baseGiftCardsAmount;
	@JsonProperty("gift_cards_amount")
	private Integer giftCardsAmount;
	@JsonProperty("base_gift_cards_invoiced")
	private Integer baseGiftCardsInvoiced;
	@JsonProperty("gift_cards_invoiced")
	private Integer giftCardsInvoiced;
	@JsonProperty("base_gift_cards_refunded")
	private Integer baseGiftCardsRefunded;
	@JsonProperty("gift_cards_refunded")
	private Integer giftCardsRefunded;
	@JsonProperty("applied_taxes")
	private List<AppliedTax> appliedTaxes = null;
	@JsonProperty("item_applied_taxes")
	private List<ItemAppliedTax> itemAppliedTaxes = null;
	@JsonProperty("converting_from_quote")
	private Boolean convertingFromQuote;
	@JsonProperty("gift_message")
	private GiftMessage__ giftMessage;
	@JsonProperty("gw_id")
	private String gwId;
	@JsonProperty("gw_allow_gift_receipt")
	private String gwAllowGiftReceipt;
	@JsonProperty("gw_add_card")
	private String gwAddCard;
	@JsonProperty("gw_base_price")
	private String gwBasePrice;
	@JsonProperty("gw_price")
	private String gwPrice;
	@JsonProperty("gw_items_base_price")
	private String gwItemsBasePrice;
	@JsonProperty("gw_items_price")
	private String gwItemsPrice;
	@JsonProperty("gw_card_base_price")
	private String gwCardBasePrice;
	@JsonProperty("gw_card_price")
	private String gwCardPrice;
	@JsonProperty("gw_base_tax_amount")
	private String gwBaseTaxAmount;
	@JsonProperty("gw_tax_amount")
	private String gwTaxAmount;
	@JsonProperty("gw_items_base_tax_amount")
	private String gwItemsBaseTaxAmount;
	@JsonProperty("gw_items_tax_amount")
	private String gwItemsTaxAmount;
	@JsonProperty("gw_card_base_tax_amount")
	private String gwCardBaseTaxAmount;
	@JsonProperty("gw_card_tax_amount")
	private String gwCardTaxAmount;
	@JsonProperty("gw_base_price_incl_tax")
	private String gwBasePriceInclTax;
	@JsonProperty("gw_price_incl_tax")
	private String gwPriceInclTax;
	@JsonProperty("gw_items_base_price_incl_tax")
	private String gwItemsBasePriceInclTax;
	@JsonProperty("gw_items_price_incl_tax")
	private String gwItemsPriceInclTax;
	@JsonProperty("gw_card_base_price_incl_tax")
	private String gwCardBasePriceInclTax;
	@JsonProperty("gw_card_price_incl_tax")
	private String gwCardPriceInclTax;
	@JsonProperty("gw_base_price_invoiced")
	private String gwBasePriceInvoiced;
	@JsonProperty("gw_price_invoiced")
	private String gwPriceInvoiced;
	@JsonProperty("gw_items_base_price_invoiced")
	private String gwItemsBasePriceInvoiced;
	@JsonProperty("gw_items_price_invoiced")
	private String gwItemsPriceInvoiced;
	@JsonProperty("gw_card_base_price_invoiced")
	private String gwCardBasePriceInvoiced;
	@JsonProperty("gw_card_price_invoiced")
	private String gwCardPriceInvoiced;
	@JsonProperty("gw_base_tax_amount_invoiced")
	private String gwBaseTaxAmountInvoiced;
	@JsonProperty("gw_tax_amount_invoiced")
	private String gwTaxAmountInvoiced;
	@JsonProperty("gw_items_base_tax_invoiced")
	private String gwItemsBaseTaxInvoiced;
	@JsonProperty("gw_items_tax_invoiced")
	private String gwItemsTaxInvoiced;
	@JsonProperty("gw_card_base_tax_invoiced")
	private String gwCardBaseTaxInvoiced;
	@JsonProperty("gw_card_tax_invoiced")
	private String gwCardTaxInvoiced;
	@JsonProperty("gw_base_price_refunded")
	private String gwBasePriceRefunded;
	@JsonProperty("gw_price_refunded")
	private String gwPriceRefunded;
	@JsonProperty("gw_items_base_price_refunded")
	private String gwItemsBasePriceRefunded;
	@JsonProperty("gw_items_price_refunded")
	private String gwItemsPriceRefunded;
	@JsonProperty("gw_card_base_price_refunded")
	private String gwCardBasePriceRefunded;
	@JsonProperty("gw_card_price_refunded")
	private String gwCardPriceRefunded;
	@JsonProperty("gw_base_tax_amount_refunded")
	private String gwBaseTaxAmountRefunded;
	@JsonProperty("gw_tax_amount_refunded")
	private String gwTaxAmountRefunded;
	@JsonProperty("gw_items_base_tax_refunded")
	private String gwItemsBaseTaxRefunded;
	@JsonProperty("gw_items_tax_refunded")
	private String gwItemsTaxRefunded;
	@JsonProperty("gw_card_base_tax_refunded")
	private String gwCardBaseTaxRefunded;
	@JsonProperty("gw_card_tax_refunded")
	private String gwCardTaxRefunded;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("shipping_assignments")
	public List<ShippingAssignment> getShippingAssignments() {
		return shippingAssignments;
	}

	@JsonProperty("shipping_assignments")
	public void setShippingAssignments(List<ShippingAssignment> shippingAssignments) {
		this.shippingAssignments = shippingAssignments;
	}

	@JsonProperty("company_order_attributes")
	public CompanyOrderAttributes getCompanyOrderAttributes() {
		return companyOrderAttributes;
	}

	@JsonProperty("company_order_attributes")
	public void setCompanyOrderAttributes(CompanyOrderAttributes companyOrderAttributes) {
		this.companyOrderAttributes = companyOrderAttributes;
	}

	@JsonProperty("base_customer_balance_amount")
	public Integer getBaseCustomerBalanceAmount() {
		return baseCustomerBalanceAmount;
	}

	@JsonProperty("base_customer_balance_amount")
	public void setBaseCustomerBalanceAmount(Integer baseCustomerBalanceAmount) {
		this.baseCustomerBalanceAmount = baseCustomerBalanceAmount;
	}

	@JsonProperty("customer_balance_amount")
	public Integer getCustomerBalanceAmount() {
		return customerBalanceAmount;
	}

	@JsonProperty("customer_balance_amount")
	public void setCustomerBalanceAmount(Integer customerBalanceAmount) {
		this.customerBalanceAmount = customerBalanceAmount;
	}

	@JsonProperty("base_customer_balance_invoiced")
	public Integer getBaseCustomerBalanceInvoiced() {
		return baseCustomerBalanceInvoiced;
	}

	@JsonProperty("base_customer_balance_invoiced")
	public void setBaseCustomerBalanceInvoiced(Integer baseCustomerBalanceInvoiced) {
		this.baseCustomerBalanceInvoiced = baseCustomerBalanceInvoiced;
	}

	@JsonProperty("customer_balance_invoiced")
	public Integer getCustomerBalanceInvoiced() {
		return customerBalanceInvoiced;
	}

	@JsonProperty("customer_balance_invoiced")
	public void setCustomerBalanceInvoiced(Integer customerBalanceInvoiced) {
		this.customerBalanceInvoiced = customerBalanceInvoiced;
	}

	@JsonProperty("base_customer_balance_refunded")
	public Integer getBaseCustomerBalanceRefunded() {
		return baseCustomerBalanceRefunded;
	}

	@JsonProperty("base_customer_balance_refunded")
	public void setBaseCustomerBalanceRefunded(Integer baseCustomerBalanceRefunded) {
		this.baseCustomerBalanceRefunded = baseCustomerBalanceRefunded;
	}

	@JsonProperty("customer_balance_refunded")
	public Integer getCustomerBalanceRefunded() {
		return customerBalanceRefunded;
	}

	@JsonProperty("customer_balance_refunded")
	public void setCustomerBalanceRefunded(Integer customerBalanceRefunded) {
		this.customerBalanceRefunded = customerBalanceRefunded;
	}

	@JsonProperty("base_customer_balance_total_refunded")
	public Integer getBaseCustomerBalanceTotalRefunded() {
		return baseCustomerBalanceTotalRefunded;
	}

	@JsonProperty("base_customer_balance_total_refunded")
	public void setBaseCustomerBalanceTotalRefunded(Integer baseCustomerBalanceTotalRefunded) {
		this.baseCustomerBalanceTotalRefunded = baseCustomerBalanceTotalRefunded;
	}

	@JsonProperty("customer_balance_total_refunded")
	public Integer getCustomerBalanceTotalRefunded() {
		return customerBalanceTotalRefunded;
	}

	@JsonProperty("customer_balance_total_refunded")
	public void setCustomerBalanceTotalRefunded(Integer customerBalanceTotalRefunded) {
		this.customerBalanceTotalRefunded = customerBalanceTotalRefunded;
	}

	@JsonProperty("gift_cards")
	public List<GiftCard> getGiftCards() {
		return giftCards;
	}

	@JsonProperty("gift_cards")
	public void setGiftCards(List<GiftCard> giftCards) {
		this.giftCards = giftCards;
	}

	@JsonProperty("base_gift_cards_amount")
	public Integer getBaseGiftCardsAmount() {
		return baseGiftCardsAmount;
	}

	@JsonProperty("base_gift_cards_amount")
	public void setBaseGiftCardsAmount(Integer baseGiftCardsAmount) {
		this.baseGiftCardsAmount = baseGiftCardsAmount;
	}

	@JsonProperty("gift_cards_amount")
	public Integer getGiftCardsAmount() {
		return giftCardsAmount;
	}

	@JsonProperty("gift_cards_amount")
	public void setGiftCardsAmount(Integer giftCardsAmount) {
		this.giftCardsAmount = giftCardsAmount;
	}

	@JsonProperty("base_gift_cards_invoiced")
	public Integer getBaseGiftCardsInvoiced() {
		return baseGiftCardsInvoiced;
	}

	@JsonProperty("base_gift_cards_invoiced")
	public void setBaseGiftCardsInvoiced(Integer baseGiftCardsInvoiced) {
		this.baseGiftCardsInvoiced = baseGiftCardsInvoiced;
	}

	@JsonProperty("gift_cards_invoiced")
	public Integer getGiftCardsInvoiced() {
		return giftCardsInvoiced;
	}

	@JsonProperty("gift_cards_invoiced")
	public void setGiftCardsInvoiced(Integer giftCardsInvoiced) {
		this.giftCardsInvoiced = giftCardsInvoiced;
	}

	@JsonProperty("base_gift_cards_refunded")
	public Integer getBaseGiftCardsRefunded() {
		return baseGiftCardsRefunded;
	}

	@JsonProperty("base_gift_cards_refunded")
	public void setBaseGiftCardsRefunded(Integer baseGiftCardsRefunded) {
		this.baseGiftCardsRefunded = baseGiftCardsRefunded;
	}

	@JsonProperty("gift_cards_refunded")
	public Integer getGiftCardsRefunded() {
		return giftCardsRefunded;
	}

	@JsonProperty("gift_cards_refunded")
	public void setGiftCardsRefunded(Integer giftCardsRefunded) {
		this.giftCardsRefunded = giftCardsRefunded;
	}

	@JsonProperty("applied_taxes")
	public List<AppliedTax> getAppliedTaxes() {
		return appliedTaxes;
	}

	@JsonProperty("applied_taxes")
	public void setAppliedTaxes(List<AppliedTax> appliedTaxes) {
		this.appliedTaxes = appliedTaxes;
	}

	@JsonProperty("item_applied_taxes")
	public List<ItemAppliedTax> getItemAppliedTaxes() {
		return itemAppliedTaxes;
	}

	@JsonProperty("item_applied_taxes")
	public void setItemAppliedTaxes(List<ItemAppliedTax> itemAppliedTaxes) {
		this.itemAppliedTaxes = itemAppliedTaxes;
	}

	@JsonProperty("converting_from_quote")
	public Boolean getConvertingFromQuote() {
		return convertingFromQuote;
	}

	@JsonProperty("converting_from_quote")
	public void setConvertingFromQuote(Boolean convertingFromQuote) {
		this.convertingFromQuote = convertingFromQuote;
	}

	@JsonProperty("gift_message")
	public GiftMessage__ getGiftMessage() {
		return giftMessage;
	}

	@JsonProperty("gift_message")
	public void setGiftMessage(GiftMessage__ giftMessage) {
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

	@JsonProperty("gw_allow_gift_receipt")
	public String getGwAllowGiftReceipt() {
		return gwAllowGiftReceipt;
	}

	@JsonProperty("gw_allow_gift_receipt")
	public void setGwAllowGiftReceipt(String gwAllowGiftReceipt) {
		this.gwAllowGiftReceipt = gwAllowGiftReceipt;
	}

	@JsonProperty("gw_add_card")
	public String getGwAddCard() {
		return gwAddCard;
	}

	@JsonProperty("gw_add_card")
	public void setGwAddCard(String gwAddCard) {
		this.gwAddCard = gwAddCard;
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

	@JsonProperty("gw_items_base_price")
	public String getGwItemsBasePrice() {
		return gwItemsBasePrice;
	}

	@JsonProperty("gw_items_base_price")
	public void setGwItemsBasePrice(String gwItemsBasePrice) {
		this.gwItemsBasePrice = gwItemsBasePrice;
	}

	@JsonProperty("gw_items_price")
	public String getGwItemsPrice() {
		return gwItemsPrice;
	}

	@JsonProperty("gw_items_price")
	public void setGwItemsPrice(String gwItemsPrice) {
		this.gwItemsPrice = gwItemsPrice;
	}

	@JsonProperty("gw_card_base_price")
	public String getGwCardBasePrice() {
		return gwCardBasePrice;
	}

	@JsonProperty("gw_card_base_price")
	public void setGwCardBasePrice(String gwCardBasePrice) {
		this.gwCardBasePrice = gwCardBasePrice;
	}

	@JsonProperty("gw_card_price")
	public String getGwCardPrice() {
		return gwCardPrice;
	}

	@JsonProperty("gw_card_price")
	public void setGwCardPrice(String gwCardPrice) {
		this.gwCardPrice = gwCardPrice;
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

	@JsonProperty("gw_items_base_tax_amount")
	public String getGwItemsBaseTaxAmount() {
		return gwItemsBaseTaxAmount;
	}

	@JsonProperty("gw_items_base_tax_amount")
	public void setGwItemsBaseTaxAmount(String gwItemsBaseTaxAmount) {
		this.gwItemsBaseTaxAmount = gwItemsBaseTaxAmount;
	}

	@JsonProperty("gw_items_tax_amount")
	public String getGwItemsTaxAmount() {
		return gwItemsTaxAmount;
	}

	@JsonProperty("gw_items_tax_amount")
	public void setGwItemsTaxAmount(String gwItemsTaxAmount) {
		this.gwItemsTaxAmount = gwItemsTaxAmount;
	}

	@JsonProperty("gw_card_base_tax_amount")
	public String getGwCardBaseTaxAmount() {
		return gwCardBaseTaxAmount;
	}

	@JsonProperty("gw_card_base_tax_amount")
	public void setGwCardBaseTaxAmount(String gwCardBaseTaxAmount) {
		this.gwCardBaseTaxAmount = gwCardBaseTaxAmount;
	}

	@JsonProperty("gw_card_tax_amount")
	public String getGwCardTaxAmount() {
		return gwCardTaxAmount;
	}

	@JsonProperty("gw_card_tax_amount")
	public void setGwCardTaxAmount(String gwCardTaxAmount) {
		this.gwCardTaxAmount = gwCardTaxAmount;
	}

	@JsonProperty("gw_base_price_incl_tax")
	public String getGwBasePriceInclTax() {
		return gwBasePriceInclTax;
	}

	@JsonProperty("gw_base_price_incl_tax")
	public void setGwBasePriceInclTax(String gwBasePriceInclTax) {
		this.gwBasePriceInclTax = gwBasePriceInclTax;
	}

	@JsonProperty("gw_price_incl_tax")
	public String getGwPriceInclTax() {
		return gwPriceInclTax;
	}

	@JsonProperty("gw_price_incl_tax")
	public void setGwPriceInclTax(String gwPriceInclTax) {
		this.gwPriceInclTax = gwPriceInclTax;
	}

	@JsonProperty("gw_items_base_price_incl_tax")
	public String getGwItemsBasePriceInclTax() {
		return gwItemsBasePriceInclTax;
	}

	@JsonProperty("gw_items_base_price_incl_tax")
	public void setGwItemsBasePriceInclTax(String gwItemsBasePriceInclTax) {
		this.gwItemsBasePriceInclTax = gwItemsBasePriceInclTax;
	}

	@JsonProperty("gw_items_price_incl_tax")
	public String getGwItemsPriceInclTax() {
		return gwItemsPriceInclTax;
	}

	@JsonProperty("gw_items_price_incl_tax")
	public void setGwItemsPriceInclTax(String gwItemsPriceInclTax) {
		this.gwItemsPriceInclTax = gwItemsPriceInclTax;
	}

	@JsonProperty("gw_card_base_price_incl_tax")
	public String getGwCardBasePriceInclTax() {
		return gwCardBasePriceInclTax;
	}

	@JsonProperty("gw_card_base_price_incl_tax")
	public void setGwCardBasePriceInclTax(String gwCardBasePriceInclTax) {
		this.gwCardBasePriceInclTax = gwCardBasePriceInclTax;
	}

	@JsonProperty("gw_card_price_incl_tax")
	public String getGwCardPriceInclTax() {
		return gwCardPriceInclTax;
	}

	@JsonProperty("gw_card_price_incl_tax")
	public void setGwCardPriceInclTax(String gwCardPriceInclTax) {
		this.gwCardPriceInclTax = gwCardPriceInclTax;
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

	@JsonProperty("gw_items_base_price_invoiced")
	public String getGwItemsBasePriceInvoiced() {
		return gwItemsBasePriceInvoiced;
	}

	@JsonProperty("gw_items_base_price_invoiced")
	public void setGwItemsBasePriceInvoiced(String gwItemsBasePriceInvoiced) {
		this.gwItemsBasePriceInvoiced = gwItemsBasePriceInvoiced;
	}

	@JsonProperty("gw_items_price_invoiced")
	public String getGwItemsPriceInvoiced() {
		return gwItemsPriceInvoiced;
	}

	@JsonProperty("gw_items_price_invoiced")
	public void setGwItemsPriceInvoiced(String gwItemsPriceInvoiced) {
		this.gwItemsPriceInvoiced = gwItemsPriceInvoiced;
	}

	@JsonProperty("gw_card_base_price_invoiced")
	public String getGwCardBasePriceInvoiced() {
		return gwCardBasePriceInvoiced;
	}

	@JsonProperty("gw_card_base_price_invoiced")
	public void setGwCardBasePriceInvoiced(String gwCardBasePriceInvoiced) {
		this.gwCardBasePriceInvoiced = gwCardBasePriceInvoiced;
	}

	@JsonProperty("gw_card_price_invoiced")
	public String getGwCardPriceInvoiced() {
		return gwCardPriceInvoiced;
	}

	@JsonProperty("gw_card_price_invoiced")
	public void setGwCardPriceInvoiced(String gwCardPriceInvoiced) {
		this.gwCardPriceInvoiced = gwCardPriceInvoiced;
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

	@JsonProperty("gw_items_base_tax_invoiced")
	public String getGwItemsBaseTaxInvoiced() {
		return gwItemsBaseTaxInvoiced;
	}

	@JsonProperty("gw_items_base_tax_invoiced")
	public void setGwItemsBaseTaxInvoiced(String gwItemsBaseTaxInvoiced) {
		this.gwItemsBaseTaxInvoiced = gwItemsBaseTaxInvoiced;
	}

	@JsonProperty("gw_items_tax_invoiced")
	public String getGwItemsTaxInvoiced() {
		return gwItemsTaxInvoiced;
	}

	@JsonProperty("gw_items_tax_invoiced")
	public void setGwItemsTaxInvoiced(String gwItemsTaxInvoiced) {
		this.gwItemsTaxInvoiced = gwItemsTaxInvoiced;
	}

	@JsonProperty("gw_card_base_tax_invoiced")
	public String getGwCardBaseTaxInvoiced() {
		return gwCardBaseTaxInvoiced;
	}

	@JsonProperty("gw_card_base_tax_invoiced")
	public void setGwCardBaseTaxInvoiced(String gwCardBaseTaxInvoiced) {
		this.gwCardBaseTaxInvoiced = gwCardBaseTaxInvoiced;
	}

	@JsonProperty("gw_card_tax_invoiced")
	public String getGwCardTaxInvoiced() {
		return gwCardTaxInvoiced;
	}

	@JsonProperty("gw_card_tax_invoiced")
	public void setGwCardTaxInvoiced(String gwCardTaxInvoiced) {
		this.gwCardTaxInvoiced = gwCardTaxInvoiced;
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

	@JsonProperty("gw_items_base_price_refunded")
	public String getGwItemsBasePriceRefunded() {
		return gwItemsBasePriceRefunded;
	}

	@JsonProperty("gw_items_base_price_refunded")
	public void setGwItemsBasePriceRefunded(String gwItemsBasePriceRefunded) {
		this.gwItemsBasePriceRefunded = gwItemsBasePriceRefunded;
	}

	@JsonProperty("gw_items_price_refunded")
	public String getGwItemsPriceRefunded() {
		return gwItemsPriceRefunded;
	}

	@JsonProperty("gw_items_price_refunded")
	public void setGwItemsPriceRefunded(String gwItemsPriceRefunded) {
		this.gwItemsPriceRefunded = gwItemsPriceRefunded;
	}

	@JsonProperty("gw_card_base_price_refunded")
	public String getGwCardBasePriceRefunded() {
		return gwCardBasePriceRefunded;
	}

	@JsonProperty("gw_card_base_price_refunded")
	public void setGwCardBasePriceRefunded(String gwCardBasePriceRefunded) {
		this.gwCardBasePriceRefunded = gwCardBasePriceRefunded;
	}

	@JsonProperty("gw_card_price_refunded")
	public String getGwCardPriceRefunded() {
		return gwCardPriceRefunded;
	}

	@JsonProperty("gw_card_price_refunded")
	public void setGwCardPriceRefunded(String gwCardPriceRefunded) {
		this.gwCardPriceRefunded = gwCardPriceRefunded;
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

	@JsonProperty("gw_items_base_tax_refunded")
	public String getGwItemsBaseTaxRefunded() {
		return gwItemsBaseTaxRefunded;
	}

	@JsonProperty("gw_items_base_tax_refunded")
	public void setGwItemsBaseTaxRefunded(String gwItemsBaseTaxRefunded) {
		this.gwItemsBaseTaxRefunded = gwItemsBaseTaxRefunded;
	}

	@JsonProperty("gw_items_tax_refunded")
	public String getGwItemsTaxRefunded() {
		return gwItemsTaxRefunded;
	}

	@JsonProperty("gw_items_tax_refunded")
	public void setGwItemsTaxRefunded(String gwItemsTaxRefunded) {
		this.gwItemsTaxRefunded = gwItemsTaxRefunded;
	}

	@JsonProperty("gw_card_base_tax_refunded")
	public String getGwCardBaseTaxRefunded() {
		return gwCardBaseTaxRefunded;
	}

	@JsonProperty("gw_card_base_tax_refunded")
	public void setGwCardBaseTaxRefunded(String gwCardBaseTaxRefunded) {
		this.gwCardBaseTaxRefunded = gwCardBaseTaxRefunded;
	}

	@JsonProperty("gw_card_tax_refunded")
	public String getGwCardTaxRefunded() {
		return gwCardTaxRefunded;
	}

	@JsonProperty("gw_card_tax_refunded")
	public void setGwCardTaxRefunded(String gwCardTaxRefunded) {
		this.gwCardTaxRefunded = gwCardTaxRefunded;
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
