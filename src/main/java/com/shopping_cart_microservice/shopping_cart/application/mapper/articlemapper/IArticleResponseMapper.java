package com.shopping_cart_microservice.shopping_cart.application.mapper.articlemapper;

import com.shopping_cart_microservice.shopping_cart.application.dto.articledto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IArticleResponseMapper {

    ArticleDetailsCartResponse articleModelToArticleDetailsCartResponse(ArticleDetailsCartModel articleDetailsCartModel);

    ArticleDetailsCartModel articleDetailsCartResponseToArticleDetailsCartModel(ArticleDetailsCartResponse articleDetailsCartResponse);
}
