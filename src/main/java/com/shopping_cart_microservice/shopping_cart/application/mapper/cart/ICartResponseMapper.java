package com.shopping_cart_microservice.shopping_cart.application.mapper.cart;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICartResponseMapper {

    CartResponse cartModelToCartResponse (CartModel cartModel);
}
