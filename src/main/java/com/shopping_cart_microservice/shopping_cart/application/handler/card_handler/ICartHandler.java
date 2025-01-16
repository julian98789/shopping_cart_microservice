package com.shopping_cart_microservice.shopping_cart.application.handler.card_handler;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;

public interface ICartHandler {

    CartResponse addArticleToCart (CartRequest cartRequest);

    void removeProductToCart(Long productId );
}
