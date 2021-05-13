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
@JsonPropertyOrder({ "custom_options", "bundle_options", "downloadable_option", "giftcard_item_option",
		"configurable_item_options" })
public class ExtensionAttributes______________ {

	@JsonProperty("custom_options")
	private List<CustomOption_> customOptions = null;
	@JsonProperty("bundle_options")
	private List<BundleOption_> bundleOptions = null;
	@JsonProperty("downloadable_option")
	private DownloadableOption_ downloadableOption;
	@JsonProperty("giftcard_item_option")
	private GiftcardItemOption_ giftcardItemOption;
	@JsonProperty("configurable_item_options")
	private List<ConfigurableItemOption_> configurableItemOptions = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("custom_options")
	public List<CustomOption_> getCustomOptions() {
		return customOptions;
	}

	@JsonProperty("custom_options")
	public void setCustomOptions(List<CustomOption_> customOptions) {
		this.customOptions = customOptions;
	}

	@JsonProperty("bundle_options")
	public List<BundleOption_> getBundleOptions() {
		return bundleOptions;
	}

	@JsonProperty("bundle_options")
	public void setBundleOptions(List<BundleOption_> bundleOptions) {
		this.bundleOptions = bundleOptions;
	}

	@JsonProperty("downloadable_option")
	public DownloadableOption_ getDownloadableOption() {
		return downloadableOption;
	}

	@JsonProperty("downloadable_option")
	public void setDownloadableOption(DownloadableOption_ downloadableOption) {
		this.downloadableOption = downloadableOption;
	}

	@JsonProperty("giftcard_item_option")
	public GiftcardItemOption_ getGiftcardItemOption() {
		return giftcardItemOption;
	}

	@JsonProperty("giftcard_item_option")
	public void setGiftcardItemOption(GiftcardItemOption_ giftcardItemOption) {
		this.giftcardItemOption = giftcardItemOption;
	}

	@JsonProperty("configurable_item_options")
	public List<ConfigurableItemOption_> getConfigurableItemOptions() {
		return configurableItemOptions;
	}

	@JsonProperty("configurable_item_options")
	public void setConfigurableItemOptions(List<ConfigurableItemOption_> configurableItemOptions) {
		this.configurableItemOptions = configurableItemOptions;
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
