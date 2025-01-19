package com.shopping_cart_microservice.shopping_cart.domain.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
