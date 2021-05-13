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
@JsonPropertyOrder({ "address_type", "city", "company", "country_id", "customer_address_id", "customer_id", "email",
		"entity_id", "fax", "firstname", "lastname", "middlename", "parent_id", "postcode", "prefix", "region",
		"region_code", "region_id", "street", "suffix", "telephone", "vat_id", "vat_is_valid", "vat_request_date",
		"vat_request_id", "vat_request_success", "extension_attributes" })
public class Address {

	@JsonProperty("address_type")
	private String addressType;
	@JsonProperty("city")
	private String city;
	@JsonProperty("company")
	private String company;
	@JsonProperty("country_id")
	private String countryId;
	@JsonProperty("customer_address_id")
	private Integer customerAddressId;
	@JsonProperty("customer_id")
	private Integer customerId;
	@JsonProperty("email")
	private String email;
	@JsonProperty("entity_id")
	private Integer entityId;
	@JsonProperty("fax")
	private String fax;
	@JsonProperty("firstname")
	private String firstname;
	@JsonProperty("lastname")
	private String lastname;
	@JsonProperty("middlename")
	private String middlename;
	@JsonProperty("parent_id")
	private Integer parentId;
	@JsonProperty("postcode")
	private String postcode;
	@JsonProperty("prefix")
	private String prefix;
	@JsonProperty("region")
	private String region;
	@JsonProperty("region_code")
	private String regionCode;
	@JsonProperty("region_id")
	private Integer regionId;
	@JsonProperty("street")
	private List<String> street = null;
	@JsonProperty("suffix")
	private String suffix;
	@JsonProperty("telephone")
	private String telephone;
	@JsonProperty("vat_id")
	private String vatId;
	@JsonProperty("vat_is_valid")
	private Integer vatIsValid;
	@JsonProperty("vat_request_date")
	private String vatRequestDate;
	@JsonProperty("vat_request_id")
	private String vatRequestId;
	@JsonProperty("vat_request_success")
	private Integer vatRequestSuccess;
	@JsonProperty("extension_attributes")
	private ExtensionAttributes___________ extensionAttributes;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("address_type")
	public String getAddressType() {
		return addressType;
	}

	@JsonProperty("address_type")
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	@JsonProperty("city")
	public String getCity() {
		return city;
	}

	@JsonProperty("city")
	public void setCity(String city) {
		this.city = city;
	}

	@JsonProperty("company")
	public String getCompany() {
		return company;
	}

	@JsonProperty("company")
	public void setCompany(String company) {
		this.company = company;
	}

	@JsonProperty("country_id")
	public String getCountryId() {
		return countryId;
	}

	@JsonProperty("country_id")
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}

	@JsonProperty("customer_address_id")
	public Integer getCustomerAddressId() {
		return customerAddressId;
	}

	@JsonProperty("customer_address_id")
	public void setCustomerAddressId(Integer customerAddressId) {
		this.customerAddressId = customerAddressId;
	}

	@JsonProperty("customer_id")
	public Integer getCustomerId() {
		return customerId;
	}

	@JsonProperty("customer_id")
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("entity_id")
	public Integer getEntityId() {
		return entityId;
	}

	@JsonProperty("entity_id")
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@JsonProperty("fax")
	public String getFax() {
		return fax;
	}

	@JsonProperty("fax")
	public void setFax(String fax) {
		this.fax = fax;
	}

	@JsonProperty("firstname")
	public String getFirstname() {
		return firstname;
	}

	@JsonProperty("firstname")
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@JsonProperty("lastname")
	public String getLastname() {
		return lastname;
	}

	@JsonProperty("lastname")
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@JsonProperty("middlename")
	public String getMiddlename() {
		return middlename;
	}

	@JsonProperty("middlename")
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	@JsonProperty("parent_id")
	public Integer getParentId() {
		return parentId;
	}

	@JsonProperty("parent_id")
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@JsonProperty("postcode")
	public String getPostcode() {
		return postcode;
	}

	@JsonProperty("postcode")
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@JsonProperty("prefix")
	public String getPrefix() {
		return prefix;
	}

	@JsonProperty("prefix")
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@JsonProperty("region")
	public String getRegion() {
		return region;
	}

	@JsonProperty("region")
	public void setRegion(String region) {
		this.region = region;
	}

	@JsonProperty("region_code")
	public String getRegionCode() {
		return regionCode;
	}

	@JsonProperty("region_code")
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	@JsonProperty("region_id")
	public Integer getRegionId() {
		return regionId;
	}

	@JsonProperty("region_id")
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	@JsonProperty("street")
	public List<String> getStreet() {
		return street;
	}

	@JsonProperty("street")
	public void setStreet(List<String> street) {
		this.street = street;
	}

	@JsonProperty("suffix")
	public String getSuffix() {
		return suffix;
	}

	@JsonProperty("suffix")
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@JsonProperty("telephone")
	public String getTelephone() {
		return telephone;
	}

	@JsonProperty("telephone")
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@JsonProperty("vat_id")
	public String getVatId() {
		return vatId;
	}

	@JsonProperty("vat_id")
	public void setVatId(String vatId) {
		this.vatId = vatId;
	}

	@JsonProperty("vat_is_valid")
	public Integer getVatIsValid() {
		return vatIsValid;
	}

	@JsonProperty("vat_is_valid")
	public void setVatIsValid(Integer vatIsValid) {
		this.vatIsValid = vatIsValid;
	}

	@JsonProperty("vat_request_date")
	public String getVatRequestDate() {
		return vatRequestDate;
	}

	@JsonProperty("vat_request_date")
	public void setVatRequestDate(String vatRequestDate) {
		this.vatRequestDate = vatRequestDate;
	}

	@JsonProperty("vat_request_id")
	public String getVatRequestId() {
		return vatRequestId;
	}

	@JsonProperty("vat_request_id")
	public void setVatRequestId(String vatRequestId) {
		this.vatRequestId = vatRequestId;
	}

	@JsonProperty("vat_request_success")
	public Integer getVatRequestSuccess() {
		return vatRequestSuccess;
	}

	@JsonProperty("vat_request_success")
	public void setVatRequestSuccess(Integer vatRequestSuccess) {
		this.vatRequestSuccess = vatRequestSuccess;
	}

	@JsonProperty("extension_attributes")
	public ExtensionAttributes___________ getExtensionAttributes() {
		return extensionAttributes;
	}

	@JsonProperty("extension_attributes")
	public void setExtensionAttributes(ExtensionAttributes___________ extensionAttributes) {
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
