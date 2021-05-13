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
public class GoogleShopping implements Cloneable {

	// Vorlage: https://support.google.com/merchants/answer/7052112?hl=de

	// REQUIRED
	String id;
	String title;
	String description;
	String link;
	String imageLink;
	String availability;
	String price;
	String googleProductCategory;
	String brand;
	String mpn;
	String condition;
	String adult;
	String shipping; // ShippingPrice
	// String shippingWeight;
	String identifierExists;

	// OPTIONAL
	String productType;
	String gtin;
	String salePrice;
	String customLabel0; // Einzigartiges Produkt oder normale Produkte
	String customLabel1; // Lieferzeit abgestuft
	String customLabel2; // Preis abgestuft
	String customLabel3;
	String customLabel4;

	@Override
	public Object clone() {
		Object theClone = null;
		try {
			theClone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return theClone;
	}

}
