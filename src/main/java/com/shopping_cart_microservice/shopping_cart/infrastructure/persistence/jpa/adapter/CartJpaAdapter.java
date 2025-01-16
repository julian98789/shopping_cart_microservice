package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter;

import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ICartModelPersistencePort;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.entity.CartEntity;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.mapper.ICartEntityMapper;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.repository.ICartRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
public class CartJpaAdapter implements ICartModelPersistencePort {

    private final ICartEntityMapper cartEntityMapper;
    private final ICartRepository cartRepository;

    @Override
    public CartModel addArticleToCart(CartModel cartModel) {
        CartEntity cartEntity = cartRepository.save(cartEntityMapper.cartModelToCartEntity(cartModel));
        return cartEntityMapper.cartEntityToCartModel(cartEntity);
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

    @Override
    public void removeArticleFromCart(Long userId, Long articleId) {
        CartEntity cartEntity = cartRepository.findByUserIdAndArticleId(userId, articleId);
        cartRepository.delete(cartEntity);
    }

    @Override
    public void updateCartItemsUpdatedAt(Long userId, LocalDate lastUpdatedDate) {
        List<CartEntity> cartEntities = cartRepository.findByUserId(userId);
        for (CartEntity cartEntity : cartEntities) {
            cartEntity.setLastUpdatedDate(lastUpdatedDate);
            cartRepository.save(cartEntity);
        }
    }
}
