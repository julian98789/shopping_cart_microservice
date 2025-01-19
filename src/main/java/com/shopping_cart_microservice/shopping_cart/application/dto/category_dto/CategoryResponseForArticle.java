package com.shopping_cart_microservice.shopping_cart.application.dto.category_dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryResponseForArticle implements Serializable {
    private Long id;
    private String name;
}