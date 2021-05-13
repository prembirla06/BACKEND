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
public class RealProduktdatei {

	// PFLICHTANGABEN
	String ean;
	String category;
	String title;
	String description;
	String picture1;
	String picture2;
	String picture3;
	String picture4;
	String manufacturer;
	// ANGABEN KATEGORIE
	String short_description;
	String width;
	String decor;
	String energy_efficiency_class;
	String energylabel;
	String colour;
	String mpn;
	String height;
	String depth;
	String additional_categories;
	String material_composition;
}
