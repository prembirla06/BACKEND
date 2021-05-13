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
public class Moebel24de {

	String name;
	String sku_id;
	String master_sku_id;
	String price;
	String alternate_price;
	String tax;
	String delivery_cost;
	String delivery_time; // immer feste lieferzeit ab lager
	String category_path;
	String description;
	String product_url;
	String clean_url;
	String color;
	String material;
	String length;
	String width;
	String height;
	String product_image_url_1;
	String product_image_url_2;
	String product_image_url_3;
	String product_image_url_4;
	String product_image_url_5;
	String ean;
	String brand;
	String cpc;
	String availability;

}
