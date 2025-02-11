package com.shopping_cart_microservice.shopping_cart.application.dto.categorydto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CategoryResponseForArticle implements Serializable {
    private Long id;
    private String name;
}