package com.shopping_cart_microservice.shopping_cart.application.mapper.article_mapper;

import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.model.ArticleDetailsCartModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IArticleResponseMapper {

    ArticleDetailsCartResponse articleModelToArticleDetailsCartResponse(ArticleDetailsCartModel articleDetailsCartModel);
}
