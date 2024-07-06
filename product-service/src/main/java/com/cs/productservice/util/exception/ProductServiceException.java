package com.cs.productservice.util.exception;

import lombok.Getter;

@Getter
public class ProductServiceException extends RuntimeException {
	private static final long serialVersionUID = -1030936364884963064L;

	private String message;

    public ProductServiceException() {}

    public ProductServiceException(String msg) {
        super(msg);
        this.message = msg;
    }
}
