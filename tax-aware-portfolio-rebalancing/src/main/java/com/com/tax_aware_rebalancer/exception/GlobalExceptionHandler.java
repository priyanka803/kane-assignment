package com.com.tax_aware_rebalancer.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<Map<String, Object>> handleCustomException(CustomException ex) {

		Map<String, Object> errorResponse = new HashMap<>();
		errorResponse.put("status", ex.getStatus().value());
		errorResponse.put("error", ex.getStatus().name());
		errorResponse.put("message", ex.getMessage());
		errorResponse.put("timestamp", LocalDateTime.now());

		return new ResponseEntity<>(errorResponse, ex.getStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {

		Map<String, Object> errors = new HashMap<>();
		errors.put("status", HttpStatus.BAD_REQUEST.value());
		errors.put("error", HttpStatus.BAD_REQUEST.name());

		String message = ex.getBindingResult().getFieldError().getDefaultMessage();
		errors.put("message", message);
		errors.put("timestamp", LocalDateTime.now());

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidPercentageException.class)
    public ResponseEntity<Map<String, String>> invalidPercentageException(InvalidPercentageException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", "400");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
