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
package com.maxxsoft.common.model;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */
public enum OrderStates {
	RECEIVED(101, "Order has received successfully"), PROCESSING(102, "Order is under processing"), UNAVAILABLE(103,
			"Order's articles are not available in stock"), UNPAID(104,
					"Payment has not been received against this Order"), PAID(111,
							"Payment has been received against this Order"), PAYMENT_UNCONFIRMED(112,
									"Payment has not been confirmed against this Order"), INTERNAL_SHIPPING(105,
											"Order is being prepared for Shipping"), SHIPPED(106,
													"Order has been shipped"), DELIVERED(107,
															"Order has been delivered to customer"), HOLD(108,
																	"Order processing is on hold"), CANCELLED(109,
																			"Order has been cancelled"), EXTERNAL_SHIPPING(
																					110,
																					"Order is being prepared for Shipping"), PACKED(
																							111,
																							"Order is packed and ready to deliver"), PACKED_FOR_ES(
																									112,
																									"Order is packed for external Shipping and ready to deliver"), SHIPPED_BY_ES(
																											113,
																											"Order has been shipped through External Shipping"), DELIVERED_BY_ES(
																													114,
																													"Order has been delivered through External Shipping to customer"),;

	private final int code;
	private final String message;

	OrderStates(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
