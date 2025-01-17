package com.shopping_cart_microservice.shopping_cart.application.dto.article_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleCartRequest {

    private List<Long> articleIds;
}
