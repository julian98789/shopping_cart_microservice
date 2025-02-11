package com.shopping_cart_microservice.shopping_cart.application.mapper.cartmapper;

import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ICartRequestMapper {

    CartModel cartRequestToCartModel(CartRequest cartRequest);

    CartModel cartUpdateRequestToCartModel(CartUpdateQuantityRequest cartRequest);
}
