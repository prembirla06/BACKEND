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
public enum StatusCode {
	EVALUATION_DONE(2100, "Evaluation has been completed successfully"), STATE_UPDATED(2101,
			"Modelsnapshot state updated successfully"), EVALUATION_FAILED(2102, "Evaluation failed"), NO_RECORD_FOUND(
					2103,
					"No Record Found"), CONFIG_UPDATED(2104, "Configuration updated successfully"), REQUEST_COMPLETED(
							2105, "Request completed successfully"), REQUEST_FAILED(2106,
									"Request failed"), NO_RECORD_FOUND_TO_UPDATE(2107,
											"No record found to update"), USER_CREATED(4001,
													"User successfully created"), PASSWORD_CHANGED(4010,
															"Password successfully changed"), USER_DELETED(4011,
																	"User deleted successfully"), ARTICLE_CREATED(4012,
																			"Article successfully created"), ARTICLE_DELETED(
																					4013,
																					"Article deleted successfully"), ARTICLE_UPDATED(
																							4014,
																							"Article successfully updated"), ARTICLE_SET_CREATED(
																									4015,
																									"Article set successfully created"), PACKET_CREATED(
																											4016,
																											"packet created successfully created"), ARTICLE_SET_DELETED(
																													4017,
																													"Article Set deleted successfully"), PACKET_DELETED(
																															4018,
																															"Packet deleted successfully"), ARTICLE_SET_UPDATED(
																																	4019,
																																	"Article Set successfully updated"), PACKET_UPDATED(
																																			4020,
																																			"Packet successfully updated"), ARTICLE_IMAGE_UPDATED(
																																					4021,
																																					"Article image successfully updated"), ARTICLE_SET_IMAGE_UPDATED(
																																							4022,
																																							"Article Set image successfully updated"), ARTICLE_IMAGE_DELETED(
																																									4023,
																																									"Article image successfully deleted"), ARTICLE_SET_IMAGE_DELETED(
																																											4024,
																																											"Article Set image successfully deleted"), IMAGE_ADDED(
																																													4025,
																																													"Image successfully added"), TOUR_CREATED(
																																															4026,
																																															"Tour successfully created"), ORDER_REMOVED_TOUR(
																																																	4027,
																																																	"Order removed from Tour successfully"), BARCODE_DELETED(
																																																			4028,
																																																			"Barcode deleted successfully");

	private final int code;
	private final String message;

	StatusCode(int code, String message) {
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
