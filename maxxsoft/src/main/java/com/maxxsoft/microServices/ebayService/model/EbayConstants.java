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
package com.maxxsoft.microServices.ebayService.model;

public interface EbayConstants {

	String ORDER_URL = "https://api.sandbox.ebay.com/sell/fulfillment/v1/order?filter=creationdate:[";

	String REFRESH_TOKEN_SANDBOX = "v^1.1#i^1#f^0#r^1#p^3#I^3#t^Ul4xMF8xMTo5QkU2REYxNEFENDE5MDY5ODhCMTBERDk0QTIyNDBGOF8wXzEjRV4xMjg0";

	String REFRESH_TOKEN_PROD = "v^1.1#i^1#p^3#I^3#r^1#f^0#t^Ul4xMF8xMDo4RURFRTAwQTU5RDdEMTREM0YwQUFBOEQyODIzMjJFNV8wXzEjRV4yNjA=";

	String SCOPE = "https://api.ebay.com/oauth/api_scope https://api.ebay.com/oauth/api_scope/sell.marketing.readonly https://api.ebay.com/oauth/api_scope/sell.marketing https://api.ebay.com/oauth/api_scope/sell.inventory.readonly https://api.ebay.com/oauth/api_scope/sell.inventory https://api.ebay.com/oauth/api_scope/sell.account.readonly https://api.ebay.com/oauth/api_scope/sell.account https://api.ebay.com/oauth/api_scope/sell.fulfillment.readonly https://api.ebay.com/oauth/api_scope/sell.fulfillment https://api.ebay.com/oauth/api_scope/sell.analytics.readonly https://api.ebay.com/oauth/api_scope/sell.finances https://api.ebay.com/oauth/api_scope/sell.payment.dispute https://api.ebay.com/oauth/api_scope/commerce.identity.readonly";
}
