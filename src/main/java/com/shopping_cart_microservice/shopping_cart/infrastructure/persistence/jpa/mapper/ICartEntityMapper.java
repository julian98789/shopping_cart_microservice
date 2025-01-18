package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.mapper;

import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.entity.CartEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartEntityMapper {

    CartEntity cartModelToCartEntity(CartModel cartModel);
    CartModel cartEntityToCartModel(CartEntity cartEntity);
    List<CartModel> cartEntitiesListToCartModelsList(List<CartEntity> cartEntities);
}
