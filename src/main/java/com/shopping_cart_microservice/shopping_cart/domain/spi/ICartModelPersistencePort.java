package com.shopping_cart_microservice.shopping_cart.domain.spi;

import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;

import java.util.List;

public interface ICartModelPersistencePort {

    void addProductToCart(CartModel cartModel);

    CartModel findArticleByUserIdAndArticleId(Long userId, Long articleId);

    List<Long> findArticleIdsByUserId(Long userId);
}
