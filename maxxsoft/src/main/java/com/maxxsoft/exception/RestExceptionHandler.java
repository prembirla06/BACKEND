package com.maxxsoft.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.maxxsoft.common.model.ErrorCode;
import com.maxxsoft.common.model.Response;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

		return new ResponseEntity<>(
				new Response(ErrorCode.DUPLICATE_BARCODE.getCode(), ErrorCode.DUPLICATE_BARCODE.getMessage(),
						ErrorCode.DUPLICATE_BARCODE.toString(), HttpStatus.UNPROCESSABLE_ENTITY.name()),
				HttpStatus.NOT_ACCEPTABLE);
	}

}