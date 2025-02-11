package com.shopping_cart_microservice.shopping_cart.application.mapper.cartmapper;

import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICartResponseMapper {

    CartResponse cartModelToCartResponse (CartModel cartModel);


    List<CartResponse> cartModelListToCartResponseList (List<CartModel> cartModelList);
}
