package com.shopping_cart_microservice.shopping_cart.domain.exception;

public class CategoriesLimitException extends RuntimeException {

    public CategoriesLimitException(String message) {
        super(message);
    }
}
