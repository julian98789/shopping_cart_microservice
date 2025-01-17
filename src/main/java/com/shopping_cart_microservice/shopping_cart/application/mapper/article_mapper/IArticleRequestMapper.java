package com.shopping_cart_microservice.shopping_cart.application.mapper.article_mapper;

import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleCartRequest;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleCartModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IArticleRequestMapper {

    ArticleCartRequest articleCartModelToArticleCartRequest(ArticleCartModel articleCartModel);

}
