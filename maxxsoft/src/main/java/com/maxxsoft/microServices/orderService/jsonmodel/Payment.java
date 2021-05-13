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
@JsonPropertyOrder({ "account_status", "additional_data", "additional_information", "address_status",
		"amount_authorized", "amount_canceled", "amount_ordered", "amount_paid", "amount_refunded", "anet_trans_method",
		"base_amount_authorized", "base_amount_canceled", "base_amount_ordered", "base_amount_paid",
		"base_amount_paid_online", "base_amount_refunded", "base_amount_refunded_online", "base_shipping_amount",
		"base_shipping_captured", "base_shipping_refunded", "cc_approval", "cc_avs_status", "cc_cid_status",
		"cc_debug_request_body", "cc_debug_response_body", "cc_debug_response_serialized", "cc_exp_month",
		"cc_exp_year", "cc_last4", "cc_number_enc", "cc_owner", "cc_secure_verify", "cc_ss_issue", "cc_ss_start_month",
		"cc_ss_start_year", "cc_status", "cc_status_description", "cc_trans_id", "cc_type", "echeck_account_name",
		"echeck_account_type", "echeck_bank_name", "echeck_routing_number", "echeck_type", "entity_id", "last_trans_id",
		"method", "parent_id", "po_number", "protection_eligibility", "quote_payment_id", "shipping_amount",
		"shipping_captured", "shipping_refunded", "extension_attributes" })
public class Payment {

	@JsonProperty("account_status")
	private String accountStatus;
	@JsonProperty("additional_data")
	private String additionalData;
	@JsonProperty("additional_information")
	private List<String> additionalInformation = null;
	@JsonProperty("address_status")
	private String addressStatus;
	@JsonProperty("amount_authorized")
	private Integer amountAuthorized;
	@JsonProperty("amount_canceled")
	private Integer amountCanceled;
	@JsonProperty("amount_ordered")
	private Integer amountOrdered;
	@JsonProperty("amount_paid")
	private Integer amountPaid;
	@JsonProperty("amount_refunded")
	private Integer amountRefunded;
	@JsonProperty("anet_trans_method")
	private String anetTransMethod;
	@JsonProperty("base_amount_authorized")
	private Integer baseAmountAuthorized;
	@JsonProperty("base_amount_canceled")
	private Integer baseAmountCanceled;
	@JsonProperty("base_amount_ordered")
	private Integer baseAmountOrdered;
	@JsonProperty("base_amount_paid")
	private Integer baseAmountPaid;
	@JsonProperty("base_amount_paid_online")
	private Integer baseAmountPaidOnline;
	@JsonProperty("base_amount_refunded")
	private Integer baseAmountRefunded;
	@JsonProperty("base_amount_refunded_online")
	private Integer baseAmountRefundedOnline;
	@JsonProperty("base_shipping_amount")
	private Integer baseShippingAmount;
	@JsonProperty("base_shipping_captured")
	private Integer baseShippingCaptured;
	@JsonProperty("base_shipping_refunded")
	private Integer baseShippingRefunded;
	@JsonProperty("cc_approval")
	private String ccApproval;
	@JsonProperty("cc_avs_status")
	private String ccAvsStatus;
	@JsonProperty("cc_cid_status")
	private String ccCidStatus;
	@JsonProperty("cc_debug_request_body")
	private String ccDebugRequestBody;
	@JsonProperty("cc_debug_response_body")
	private String ccDebugResponseBody;
	@JsonProperty("cc_debug_response_serialized")
	private String ccDebugResponseSerialized;
	@JsonProperty("cc_exp_month")
	private String ccExpMonth;
	@JsonProperty("cc_exp_year")
	private String ccExpYear;
	@JsonProperty("cc_last4")
	private String ccLast4;
	@JsonProperty("cc_number_enc")
	private String ccNumberEnc;
	@JsonProperty("cc_owner")
	private String ccOwner;
	@JsonProperty("cc_secure_verify")
	private String ccSecureVerify;
	@JsonProperty("cc_ss_issue")
	private String ccSsIssue;
	@JsonProperty("cc_ss_start_month")
	private String ccSsStartMonth;
	@JsonProperty("cc_ss_start_year")
	private String ccSsStartYear;
	@JsonProperty("cc_status")
	private String ccStatus;
	@JsonProperty("cc_status_description")
	private String ccStatusDescription;
	@JsonProperty("cc_trans_id")
	private String ccTransId;
	@JsonProperty("cc_type")
	private String ccType;
	@JsonProperty("echeck_account_name")
	private String echeckAccountName;
	@JsonProperty("echeck_account_type")
	private String echeckAccountType;
	@JsonProperty("echeck_bank_name")
	private String echeckBankName;
	@JsonProperty("echeck_routing_number")
	private String echeckRoutingNumber;
	@JsonProperty("echeck_type")
	private String echeckType;
	@JsonProperty("entity_id")
	private Integer entityId;
	@JsonProperty("last_trans_id")
	private String lastTransId;
	@JsonProperty("method")
	private String method;
	@JsonProperty("parent_id")
	private Integer parentId;
	@JsonProperty("po_number")
	private String poNumber;
	@JsonProperty("protection_eligibility")
	private String protectionEligibility;
	@JsonProperty("quote_payment_id")
	private Integer quotePaymentId;
	@JsonProperty("shipping_amount")
	private Integer shippingAmount;
	@JsonProperty("shipping_captured")
	private Integer shippingCaptured;
	@JsonProperty("shipping_refunded")
	private Integer shippingRefunded;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("account_status")
	public String getAccountStatus() {
		return accountStatus;
	}

	@JsonProperty("account_status")
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	@JsonProperty("additional_data")
	public String getAdditionalData() {
		return additionalData;
	}

	@JsonProperty("additional_data")
	public void setAdditionalData(String additionalData) {
		this.additionalData = additionalData;
	}

	@JsonProperty("additional_information")
	public List<String> getAdditionalInformation() {
		return additionalInformation;
	}

	@JsonProperty("additional_information")
	public void setAdditionalInformation(List<String> additionalInformation) {
		this.additionalInformation = additionalInformation;
	}

	@JsonProperty("address_status")
	public String getAddressStatus() {
		return addressStatus;
	}

	@JsonProperty("address_status")
	public void setAddressStatus(String addressStatus) {
		this.addressStatus = addressStatus;
	}

	@JsonProperty("amount_authorized")
	public Integer getAmountAuthorized() {
		return amountAuthorized;
	}

	@JsonProperty("amount_authorized")
	public void setAmountAuthorized(Integer amountAuthorized) {
		this.amountAuthorized = amountAuthorized;
	}

	@JsonProperty("amount_canceled")
	public Integer getAmountCanceled() {
		return amountCanceled;
	}

	@JsonProperty("amount_canceled")
	public void setAmountCanceled(Integer amountCanceled) {
		this.amountCanceled = amountCanceled;
	}

	@JsonProperty("amount_ordered")
	public Integer getAmountOrdered() {
		return amountOrdered;
	}

	@JsonProperty("amount_ordered")
	public void setAmountOrdered(Integer amountOrdered) {
		this.amountOrdered = amountOrdered;
	}

	@JsonProperty("amount_paid")
	public Integer getAmountPaid() {
		return amountPaid;
	}

	@JsonProperty("amount_paid")
	public void setAmountPaid(Integer amountPaid) {
		this.amountPaid = amountPaid;
	}

	@JsonProperty("amount_refunded")
	public Integer getAmountRefunded() {
		return amountRefunded;
	}

	@JsonProperty("amount_refunded")
	public void setAmountRefunded(Integer amountRefunded) {
		this.amountRefunded = amountRefunded;
	}

	@JsonProperty("anet_trans_method")
	public String getAnetTransMethod() {
		return anetTransMethod;
	}

	@JsonProperty("anet_trans_method")
	public void setAnetTransMethod(String anetTransMethod) {
		this.anetTransMethod = anetTransMethod;
	}

	@JsonProperty("base_amount_authorized")
	public Integer getBaseAmountAuthorized() {
		return baseAmountAuthorized;
	}

	@JsonProperty("base_amount_authorized")
	public void setBaseAmountAuthorized(Integer baseAmountAuthorized) {
		this.baseAmountAuthorized = baseAmountAuthorized;
	}

	@JsonProperty("base_amount_canceled")
	public Integer getBaseAmountCanceled() {
		return baseAmountCanceled;
	}

	@JsonProperty("base_amount_canceled")
	public void setBaseAmountCanceled(Integer baseAmountCanceled) {
		this.baseAmountCanceled = baseAmountCanceled;
	}

	@JsonProperty("base_amount_ordered")
	public Integer getBaseAmountOrdered() {
		return baseAmountOrdered;
	}

	@JsonProperty("base_amount_ordered")
	public void setBaseAmountOrdered(Integer baseAmountOrdered) {
		this.baseAmountOrdered = baseAmountOrdered;
	}

	@JsonProperty("base_amount_paid")
	public Integer getBaseAmountPaid() {
		return baseAmountPaid;
	}

	@JsonProperty("base_amount_paid")
	public void setBaseAmountPaid(Integer baseAmountPaid) {
		this.baseAmountPaid = baseAmountPaid;
	}

	@JsonProperty("base_amount_paid_online")
	public Integer getBaseAmountPaidOnline() {
		return baseAmountPaidOnline;
	}

	@JsonProperty("base_amount_paid_online")
	public void setBaseAmountPaidOnline(Integer baseAmountPaidOnline) {
		this.baseAmountPaidOnline = baseAmountPaidOnline;
	}

	@JsonProperty("base_amount_refunded")
	public Integer getBaseAmountRefunded() {
		return baseAmountRefunded;
	}

	@JsonProperty("base_amount_refunded")
	public void setBaseAmountRefunded(Integer baseAmountRefunded) {
		this.baseAmountRefunded = baseAmountRefunded;
	}

	@JsonProperty("base_amount_refunded_online")
	public Integer getBaseAmountRefundedOnline() {
		return baseAmountRefundedOnline;
	}

	@JsonProperty("base_amount_refunded_online")
	public void setBaseAmountRefundedOnline(Integer baseAmountRefundedOnline) {
		this.baseAmountRefundedOnline = baseAmountRefundedOnline;
	}

	@JsonProperty("base_shipping_amount")
	public Integer getBaseShippingAmount() {
		return baseShippingAmount;
	}

	@JsonProperty("base_shipping_amount")
	public void setBaseShippingAmount(Integer baseShippingAmount) {
		this.baseShippingAmount = baseShippingAmount;
	}

	@JsonProperty("base_shipping_captured")
	public Integer getBaseShippingCaptured() {
		return baseShippingCaptured;
	}

	@JsonProperty("base_shipping_captured")
	public void setBaseShippingCaptured(Integer baseShippingCaptured) {
		this.baseShippingCaptured = baseShippingCaptured;
	}

	@JsonProperty("base_shipping_refunded")
	public Integer getBaseShippingRefunded() {
		return baseShippingRefunded;
	}

	@JsonProperty("base_shipping_refunded")
	public void setBaseShippingRefunded(Integer baseShippingRefunded) {
		this.baseShippingRefunded = baseShippingRefunded;
	}

	@JsonProperty("cc_approval")
	public String getCcApproval() {
		return ccApproval;
	}

	@JsonProperty("cc_approval")
	public void setCcApproval(String ccApproval) {
		this.ccApproval = ccApproval;
	}

	@JsonProperty("cc_avs_status")
	public String getCcAvsStatus() {
		return ccAvsStatus;
	}

	@JsonProperty("cc_avs_status")
	public void setCcAvsStatus(String ccAvsStatus) {
		this.ccAvsStatus = ccAvsStatus;
	}

	@JsonProperty("cc_cid_status")
	public String getCcCidStatus() {
		return ccCidStatus;
	}

	@JsonProperty("cc_cid_status")
	public void setCcCidStatus(String ccCidStatus) {
		this.ccCidStatus = ccCidStatus;
	}

	@JsonProperty("cc_debug_request_body")
	public String getCcDebugRequestBody() {
		return ccDebugRequestBody;
	}

	@JsonProperty("cc_debug_request_body")
	public void setCcDebugRequestBody(String ccDebugRequestBody) {
		this.ccDebugRequestBody = ccDebugRequestBody;
	}

	@JsonProperty("cc_debug_response_body")
	public String getCcDebugResponseBody() {
		return ccDebugResponseBody;
	}

	@JsonProperty("cc_debug_response_body")
	public void setCcDebugResponseBody(String ccDebugResponseBody) {
		this.ccDebugResponseBody = ccDebugResponseBody;
	}

	@JsonProperty("cc_debug_response_serialized")
	public String getCcDebugResponseSerialized() {
		return ccDebugResponseSerialized;
	}

	@JsonProperty("cc_debug_response_serialized")
	public void setCcDebugResponseSerialized(String ccDebugResponseSerialized) {
		this.ccDebugResponseSerialized = ccDebugResponseSerialized;
	}

	@JsonProperty("cc_exp_month")
	public String getCcExpMonth() {
		return ccExpMonth;
	}

	@JsonProperty("cc_exp_month")
	public void setCcExpMonth(String ccExpMonth) {
		this.ccExpMonth = ccExpMonth;
	}

	@JsonProperty("cc_exp_year")
	public String getCcExpYear() {
		return ccExpYear;
	}

	@JsonProperty("cc_exp_year")
	public void setCcExpYear(String ccExpYear) {
		this.ccExpYear = ccExpYear;
	}

	@JsonProperty("cc_last4")
	public String getCcLast4() {
		return ccLast4;
	}

	@JsonProperty("cc_last4")
	public void setCcLast4(String ccLast4) {
		this.ccLast4 = ccLast4;
	}

	@JsonProperty("cc_number_enc")
	public String getCcNumberEnc() {
		return ccNumberEnc;
	}

	@JsonProperty("cc_number_enc")
	public void setCcNumberEnc(String ccNumberEnc) {
		this.ccNumberEnc = ccNumberEnc;
	}

	@JsonProperty("cc_owner")
	public String getCcOwner() {
		return ccOwner;
	}

	@JsonProperty("cc_owner")
	public void setCcOwner(String ccOwner) {
		this.ccOwner = ccOwner;
	}

	@JsonProperty("cc_secure_verify")
	public String getCcSecureVerify() {
		return ccSecureVerify;
	}

	@JsonProperty("cc_secure_verify")
	public void setCcSecureVerify(String ccSecureVerify) {
		this.ccSecureVerify = ccSecureVerify;
	}

	@JsonProperty("cc_ss_issue")
	public String getCcSsIssue() {
		return ccSsIssue;
	}

	@JsonProperty("cc_ss_issue")
	public void setCcSsIssue(String ccSsIssue) {
		this.ccSsIssue = ccSsIssue;
	}

	@JsonProperty("cc_ss_start_month")
	public String getCcSsStartMonth() {
		return ccSsStartMonth;
	}

	@JsonProperty("cc_ss_start_month")
	public void setCcSsStartMonth(String ccSsStartMonth) {
		this.ccSsStartMonth = ccSsStartMonth;
	}

	@JsonProperty("cc_ss_start_year")
	public String getCcSsStartYear() {
		return ccSsStartYear;
	}

	@JsonProperty("cc_ss_start_year")
	public void setCcSsStartYear(String ccSsStartYear) {
		this.ccSsStartYear = ccSsStartYear;
	}

	@JsonProperty("cc_status")
	public String getCcStatus() {
		return ccStatus;
	}

	@JsonProperty("cc_status")
	public void setCcStatus(String ccStatus) {
		this.ccStatus = ccStatus;
	}

	@JsonProperty("cc_status_description")
	public String getCcStatusDescription() {
		return ccStatusDescription;
	}

	@JsonProperty("cc_status_description")
	public void setCcStatusDescription(String ccStatusDescription) {
		this.ccStatusDescription = ccStatusDescription;
	}

	@JsonProperty("cc_trans_id")
	public String getCcTransId() {
		return ccTransId;
	}

	@JsonProperty("cc_trans_id")
	public void setCcTransId(String ccTransId) {
		this.ccTransId = ccTransId;
	}

	@JsonProperty("cc_type")
	public String getCcType() {
		return ccType;
	}

	@JsonProperty("cc_type")
	public void setCcType(String ccType) {
		this.ccType = ccType;
	}

	@JsonProperty("echeck_account_name")
	public String getEcheckAccountName() {
		return echeckAccountName;
	}

	@JsonProperty("echeck_account_name")
	public void setEcheckAccountName(String echeckAccountName) {
		this.echeckAccountName = echeckAccountName;
	}

	@JsonProperty("echeck_account_type")
	public String getEcheckAccountType() {
		return echeckAccountType;
	}

	@JsonProperty("echeck_account_type")
	public void setEcheckAccountType(String echeckAccountType) {
		this.echeckAccountType = echeckAccountType;
	}

	@JsonProperty("echeck_bank_name")
	public String getEcheckBankName() {
		return echeckBankName;
	}

	@JsonProperty("echeck_bank_name")
	public void setEcheckBankName(String echeckBankName) {
		this.echeckBankName = echeckBankName;
	}

	@JsonProperty("echeck_routing_number")
	public String getEcheckRoutingNumber() {
		return echeckRoutingNumber;
	}

	@JsonProperty("echeck_routing_number")
	public void setEcheckRoutingNumber(String echeckRoutingNumber) {
		this.echeckRoutingNumber = echeckRoutingNumber;
	}

	@JsonProperty("echeck_type")
	public String getEcheckType() {
		return echeckType;
	}

	@JsonProperty("echeck_type")
	public void setEcheckType(String echeckType) {
		this.echeckType = echeckType;
	}

	@JsonProperty("entity_id")
	public Integer getEntityId() {
		return entityId;
	}

	@JsonProperty("entity_id")
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@JsonProperty("last_trans_id")
	public String getLastTransId() {
		return lastTransId;
	}

	@JsonProperty("last_trans_id")
	public void setLastTransId(String lastTransId) {
		this.lastTransId = lastTransId;
	}

	@JsonProperty("method")
	public String getMethod() {
		return method;
	}

	@JsonProperty("method")
	public void setMethod(String method) {
		this.method = method;
	}

	@JsonProperty("parent_id")
	public Integer getParentId() {
		return parentId;
	}

	@JsonProperty("parent_id")
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@JsonProperty("po_number")
	public String getPoNumber() {
		return poNumber;
	}

	@JsonProperty("po_number")
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	@JsonProperty("protection_eligibility")
	public String getProtectionEligibility() {
		return protectionEligibility;
	}

	@JsonProperty("protection_eligibility")
	public void setProtectionEligibility(String protectionEligibility) {
		this.protectionEligibility = protectionEligibility;
	}

	@JsonProperty("quote_payment_id")
	public Integer getQuotePaymentId() {
		return quotePaymentId;
	}

	@JsonProperty("quote_payment_id")
	public void setQuotePaymentId(Integer quotePaymentId) {
		this.quotePaymentId = quotePaymentId;
	}

	@JsonProperty("shipping_amount")
	public Integer getShippingAmount() {
		return shippingAmount;
	}

	@JsonProperty("shipping_amount")
	public void setShippingAmount(Integer shippingAmount) {
		this.shippingAmount = shippingAmount;
	}

	@JsonProperty("shipping_captured")
	public Integer getShippingCaptured() {
		return shippingCaptured;
	}

	@JsonProperty("shipping_captured")
	public void setShippingCaptured(Integer shippingCaptured) {
		this.shippingCaptured = shippingCaptured;
	}

	@JsonProperty("shipping_refunded")
	public Integer getShippingRefunded() {
		return shippingRefunded;
	}

	@JsonProperty("shipping_refunded")
	public void setShippingRefunded(Integer shippingRefunded) {
		this.shippingRefunded = shippingRefunded;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes________ extensionAttributes) {
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
