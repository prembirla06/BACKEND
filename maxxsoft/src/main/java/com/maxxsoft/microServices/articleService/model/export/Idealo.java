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
public class Idealo {
	
	String sku;
	String brand;
	String title;
	String categoryPath;
	String url;
	String description;
	String imageUrls;
	String price;
	String deliveryTime;
	String checkout;
	String fulfillmentType;
	String checkoutLimitPerPeriod;
	String eans;
	String deliveryCost_spedition;
	String paymentCosts_cash_in_advance;
	String paymentCosts_paypal;
	String size;
	String colour;
	
}
