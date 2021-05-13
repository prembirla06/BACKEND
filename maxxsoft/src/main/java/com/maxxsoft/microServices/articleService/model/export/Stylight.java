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
package com.maxxsoft.microServices.articleService.model.export;

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
public class Stylight {

	String name;
	String desc;
	String price;
	String msrp; // not needed
	String currency; // not needed
	String dimension;
	String category;
	String shipping_cost;
	String images_url;
	String gender; // not needed
	String brand;
	String product_url;
	String GTIN; // ean Nummer
	String product_id;
	String item_group_id;
	String age_group; // not needed
	String fabric; // not needed
	String color;
	String availability;
	String pattern; // not needed
	String stype; // not needed
	String cpc_bid_desktop;
	String cpc_bid_mobile;
	String cpc_bid_tabled;
}
