package com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CartResponse {
    private Long id;

    private Long articleId;

    private Long userId;

    private Integer quantity;

    private LocalDate creationDate;

    private LocalDate lastUpdatedDate;
}
