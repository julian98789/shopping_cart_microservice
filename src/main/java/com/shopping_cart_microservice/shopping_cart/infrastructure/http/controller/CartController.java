package com.shopping_cart_microservice.shopping_cart.infrastructure.http.controller;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.handler.card_handler.ICartHandler;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {


    private final ICartHandler cartHandler;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @PreAuthorize(Util.ROLE_CLIENT )
    @PostMapping("/add-article-to-card")
    public ResponseEntity<CartResponse> addArticleToCart( @Valid @RequestBody CartRequest cartRequest) {

        CartResponse cartResponse = cartHandler.addArticleToCart(cartRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }

    @PreAuthorize(Util.ROLE_CLIENT )
    @DeleteMapping("/delete-article-from-cart/{articleId}")
    public ResponseEntity<String> removeArticleFromCart(@PathVariable Long articleId) {
        cartHandler.removeProductToCart(articleId);
        return ResponseEntity.status(HttpStatus.OK).body(Util.ARTICLE_DELETED_SUCCESSFULLY);
    }

}