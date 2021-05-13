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
public enum ErrorCode {
	USER_ALREADY_EXIST(5001, "Username already taken"), PASSWORD_EMPTY(5002, "Password is empty"), OLDPASSWORD_EMPTY(
			5011, "Old password is empty"), NEWPASSWORD_EMPTY(5012, "New password is empty"), OLDPASSWORD_INCORRECT(
					5013, "Old password is incorrect"), INVALID_ROLE(5003, "Invalid Role"), ROLE_DOES_NOT_EXIST(5004,
							"Role Does Not Exist In Database"), BAD_CREDENTIALS(5005,
									"Bad Credentials"), USERNAME_EMPTY(5006, "Username is empty"), USER_NOT_EXIST(5007,
											"User does not exist"), ARTICLE_NOT_EXIST(5008,
													"Article does not exist"), ARTICLE_SET_NOT_EXIST(5009,
															"Article Set does not exist"), PACKET_NOT_EXIST(5010,
																	"Packet does not exist"), ARTICLE_ASSOCIATED_WITH_SET(
																			5011,
																			"Article can not be deactivated. It is associated with an Active ArticleSet"), IMAGE_ORDER_EXIST(
																					5012,
																					"Image with this order number, already exists"), ARTICLE_NUMBER_SPACE(
																							5013,
																							"Space is not allowed in Article Number"), ARTICLESET_NUMBER_SPACE(
																									5014,
																									"Space is not allowed in Article Set Number"), DUPLICATE_LONGNAME(
																											5015,
																											"Long Name Already Exists."), DUPLICATE_SHORTNAME(
																													5016,
																													"Short Name Already Exists."), DUPLICATE_ARTNUM(
																															5017,
																															"Article Number Already Exists."), DUPLICATE_EAN(
																																	5018,
																																	"EAN Already Exists."), ORDER_NOT_EXIST(
																																			5019,
																																			"OrderRequest does not exist"), DUPLICATE_BARCODE(
																																					5020,
																																					"Barcode is duplicate. It must be unique."),;

	private final int code;
	private final String message;

	ErrorCode(int code, String message) {
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
