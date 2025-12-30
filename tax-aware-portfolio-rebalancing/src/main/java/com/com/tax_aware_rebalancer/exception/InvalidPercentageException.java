package com.com.tax_aware_rebalancer.exception;

import org.springframework.http.HttpStatus;

public class InvalidPercentageException extends RuntimeException{
	 private final HttpStatus status;

	    public InvalidPercentageException(String message, HttpStatus status) {
	        super(message);
	        this.status = status;
	    }

	    public HttpStatus getStatus() {
	        return status;
	    }
}
