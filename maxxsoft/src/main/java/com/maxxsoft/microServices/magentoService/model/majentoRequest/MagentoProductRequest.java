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
package com.maxxsoft.microServices.magentoService.model.majentoRequest;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
@Getter
@Setter
public class MagentoProductRequest {

	// public static final int VISIBILITY_NOT_VISIBLE = 1;
	// public static final int VISIBILITY_IN_CATALOG = 2;
	// public static final int VISIBILITY_IN_SEARCH = 3;
	// public static final int VISIBILITY_BOTH = 4;
	//
	// public static final int STATUS_DISABLED = 2;
	// public static final int STATUS_ENABLED = 1;
	@NotBlank
	private String sku;
	@NotBlank
	private String name;
	private long attribute_set_id = 4;
	private BigDecimal price;
	private int status = 1;
	private int visibility = 1;
	private String type_id = "simple";
	// private String description;
	// private String short_description;

	private double weight;

	// @JSONField(deserializeUsing = ProductAttributeValueDeserializer.class)
	private MagentoExtensionAttribute extension_attributes;
	// private List<String> product_links = new ArrayList<>();
	// private List<TierPrices> tier_prices = new ArrayList<>();

	// @JSONField(deserializeUsing = ProductAttributeValueDeserializer.class)
	private Set<MagentoCustomAttributeValueRequest> custom_attributes = new HashSet();

}