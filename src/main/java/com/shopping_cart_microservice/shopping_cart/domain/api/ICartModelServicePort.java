package com.shopping_cart_microservice.shopping_cart.domain.api;

import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;

public interface ICartModelServicePort {

    void addArticleToCart(CartModel cartModel);
}
