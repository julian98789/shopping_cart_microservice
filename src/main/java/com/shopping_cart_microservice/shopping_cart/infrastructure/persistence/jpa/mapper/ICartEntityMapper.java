package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.mapper;

import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.entity.CartEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICartEntityMapper {

    CartEntity cartModelToCartEntity(CartModel cartModel);
    CartModel cartEntityToCartModel(CartEntity cartEntity);
}
