package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter;

import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ICartModelPersistencePort;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.entity.CartEntity;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.mapper.ICartEntityMapper;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.repository.ICartRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CartJpaAdapter implements ICartModelPersistencePort {

    private final ICartEntityMapper cartEntityMapper;
    private final ICartRepository cartRepository;

    @Override
    public void addProductToCart(CartModel cartModel) {
        cartRepository.save(cartEntityMapper.cartModelToCartEntity(cartModel));
    }

    @Override
    public CartModel findArticleByUserIdAndArticleId(Long userId, Long articleId) {
        return cartEntityMapper.cartEntityToCartModel(cartRepository.findByUserIdAndArticleId(userId, articleId));
    }

    @Override
    public List<Long> findArticleIdsByUserId(Long userId) {
        return cartRepository.findByUserId(userId).stream()
                .map(CartEntity::getArticleId)
                .toList();
    }
}
