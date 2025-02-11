package com.shopping_cart_microservice.shopping_cart.application.dto.articledto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.shopping_cart_microservice.shopping_cart.application.dto.branddto.BrandResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.categorydto.CategoryResponseForArticle;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ArticleDetailsCartResponse {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private double price;
    private BrandResponse brand;
    private List<CategoryResponseForArticle> categories;
    private Integer cartQuantity;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nextSupplyDate;
    private Double subtotal;
}
