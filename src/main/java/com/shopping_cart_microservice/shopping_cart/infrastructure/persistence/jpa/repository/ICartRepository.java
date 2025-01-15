package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.repository;

import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface ICartRepository extends JpaRepository<CartEntity, Long> {

    CartEntity findByUserIdAndArticleId(Long userId, Long articleId);

    List<CartEntity> findByUserId(Long userId);
}
