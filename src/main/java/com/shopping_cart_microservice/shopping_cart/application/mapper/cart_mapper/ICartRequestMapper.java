package com.shopping_cart_microservice.shopping_cart.application.mapper.cart_mapper;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICartRequestMapper {

    CartModel cartRequestToCartModel(CartRequest cartRequest);

    CartModel cartUpdateRequestToCartModel(CartUpdateQuantityRequest cartRequest);
}
