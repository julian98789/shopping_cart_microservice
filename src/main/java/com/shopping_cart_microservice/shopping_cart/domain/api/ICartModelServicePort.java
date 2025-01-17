package com.shopping_cart_microservice.shopping_cart.domain.api;

import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;

public interface ICartModelServicePort {

    CartModel addArticleToCart(CartModel cartModel);

    void removeProductToCart(Long productId );

    Paginated<ArticleDetailsCartResponse> findArticleIdsByUserId(
            int page, int size, String sort, boolean ascending, String categoryName, String brandName);

}
