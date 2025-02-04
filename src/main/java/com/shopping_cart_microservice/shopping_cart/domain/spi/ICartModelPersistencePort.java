package com.shopping_cart_microservice.shopping_cart.domain.spi;

import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;

import java.time.LocalDate;
import java.util.List;

public interface ICartModelPersistencePort {

    CartModel addArticleToCart(CartModel cartModel);

    CartModel findArticleByUserIdAndArticleId(Long userId, Long articleId);

    List<Long> findArticleIdsByUserId(Long userId);

    void removeArticleFromCart(Long userId, Long articleId);

    void updateCartItemsUpdatedAt (Long userId, LocalDate lastUpdatedDate);

    List<CartModel> findCartByUserId(Long userId);

    void deleteCart(Long userId);

    LocalDate getLatestCartUpdateDate(Long userId);
}
