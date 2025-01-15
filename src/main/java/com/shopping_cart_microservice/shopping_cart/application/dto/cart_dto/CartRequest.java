package com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {

    private Integer quantity;
    private Long articleId;
}
