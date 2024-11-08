package com.capgemini.cartservice.exception;

public class CartServiceException extends RuntimeException {
    public CartServiceException(String message) {
        super(message);
    }
}
