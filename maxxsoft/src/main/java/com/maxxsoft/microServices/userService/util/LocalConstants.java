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
package com.maxxsoft.microServices.userService.util;

/**
 * @author mahingasingh
 * @email ms@algoson.com
 */

public class LocalConstants {

	private LocalConstants() {
	}

	public static final String SUCCESS = "Success";
	public static final String INVALID_METHOD = "Invalid method call";
	public static final String UNIQUE_ID_ALREADY_EXIST = "Unique id already exist";
	public static final String USER_NOT_FOUND = "User not found";
	public static final String DEVICE_NOT_FOUND = "User not found";
	public static final String INTERNAL_SERVER_ERROR = "Internal server error";
	public static final String EXCEPTION_FAILED = "Exception failed";
	public static final String NOT_FOUND = "Not Found";
	public static final String FORBIDDEN = "Forbidden";
	public static final String USER_PROFILE_NOT_UPDATED = "User profile not updated";

	public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 4 * 60l;
	public static final String SIGNING_KEY = "UpF0MST677";
	public static final String HEADER_STRING = "x-auth-token";

	public static final String HASH = "#";

	public static final Integer RECORD_FOUND_AND_UPDATED = 1;
	public static final Integer NO_RECORD_FOUND_TO_UPDATE = 0;
	public static final String COMMA = ",";
	public static final int MARKET_SCANNER_REQUEST_ID = 111;
	public static final int ACCOUNT_SUMMARY_REQUEST_ID = 222;
	public static final int MAX_REASON_LEN = 255;
	public static final String EMPTY = "";

}
