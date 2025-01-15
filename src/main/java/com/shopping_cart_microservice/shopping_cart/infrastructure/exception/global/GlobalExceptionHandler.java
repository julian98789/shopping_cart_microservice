package com.shopping_cart_microservice.shopping_cart.infrastructure.exception.global;

import com.shopping_cart_microservice.shopping_cart.domain.exception.CategoriesLimitException;
import com.shopping_cart_microservice.shopping_cart.domain.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(InstantiationException.class)
    public ResponseEntity<String> instantiationException(InstantiationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(CategoriesLimitException.class)
    public ResponseEntity<String> categoriesLimitException(CategoriesLimitException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
