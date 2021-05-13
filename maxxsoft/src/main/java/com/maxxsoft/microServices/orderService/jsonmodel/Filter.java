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

import com.fasterxml.jackson.annotation.JsonProperty;

public class Filter {

	@JsonProperty("field")
	private String field;

	@JsonProperty("value")
	private String value;

	@JsonProperty("condition_type")
	private String conditionType;

	public String getField() {
		return field;
	}

	public void setField(String value) {
		this.field = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getConditionType() {
		return conditionType;
	}

	public void setConditionType(String value) {
		this.conditionType = value;
	}
}
