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
package com.maxxsoft.microServices.magentoService.model.magentoResponse;

import java.math.BigDecimal;

import com.maxxsoft.microServices.magentoService.model.majentoRequest.MagentoExtensionAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MagentoProductResponse {

	private Long id;
	private String sku;
	private String name;
	private BigDecimal price;
	private boolean status;
	private int visibility;

	MagentoExtensionAttribute extension_attributes;

}
