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
package com.maxxsoft.exception;

import org.apache.commons.lang3.StringUtils;

import com.maxxsoft.common.model.ErrorCode;

/**
 * @author Mahinga Singh
 * @email ms@algoson.com
 */

public class ErrorException extends RuntimeException {
	private static final long serialVersionUID = -7367148604694447251L;

	private final ErrorCode error;

	private final String message;

	public ErrorException(ErrorCode error) {
		this.error = error;
		this.message = StringUtils.EMPTY;
	}

	public ErrorCode getError() {
		return this.error;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
