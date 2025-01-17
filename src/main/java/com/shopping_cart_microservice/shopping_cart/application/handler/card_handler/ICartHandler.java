package com.shopping_cart_microservice.shopping_cart.application.handler.card_handler;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;

public interface ICartHandler {

    CartResponse addArticleToCart(CartRequest cartRequest);

    void removeProductToCart(Long productId);

    Paginated<ArticleDetailsCartResponse> getAllArticlesPaginatedByIds(
            int page, int size, String sort, boolean ascending, String categoryName, String brandName);

}
