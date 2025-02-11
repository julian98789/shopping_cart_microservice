package com.shopping_cart_microservice.shopping_cart.application.mapper.articlemapper;

import com.shopping_cart_microservice.shopping_cart.application.dto.articledto.ArticleCartRequest;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleCartModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IArticleRequestMapper {

    ArticleCartRequest articleCartModelToArticleCartRequest(ArticleCartModel articleCartModel);

}
