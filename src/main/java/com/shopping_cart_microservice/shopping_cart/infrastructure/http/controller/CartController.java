package com.shopping_cart_microservice.shopping_cart.infrastructure.http.controller;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart.ICartRequestMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart.ICartResponseMapper;
import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final ICartRequestMapper cartRequestMapper;
    private final ICartResponseMapper cartResponseMapper;
    private final ICartModelServicePort cartServicePort;
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    @PreAuthorize(Util.ROLE_CLIENT )
    @PostMapping("/add-article-to-card")
    public ResponseEntity<CartResponse> addArticleToCart( @Valid @RequestBody CartRequest cartRequest) {
        CartModel cart = cartRequestMapper.cartRequestToCartModel(cartRequest);
        cartServicePort.addArticleToCart(cart);
        CartResponse cartResponse = cartResponseMapper.cartModelToCartResponse(cart);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartResponse);
    }
}