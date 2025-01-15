package com.shopping_cart_microservice.shopping_cart.application.dto.stock_dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrandResponse {
    private Long id;
    private String name;
    private String description;
}
