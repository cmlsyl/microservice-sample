package com.cs.orderservice.util.exception;

import lombok.Getter;

@Getter
public class OrderServiceException extends RuntimeException {
	private static final long serialVersionUID = -5301447600982245880L;

	private String message;

    public OrderServiceException() {}

    public OrderServiceException(String msg) {
        super(msg);
        this.message = msg;
    }
}