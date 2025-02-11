package com.shopping_cart_microservice.shopping_cart.application.handler.cardhandler;

import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.articledto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;

import java.util.List;

public interface ICartHandler {

    CartResponse addArticleToCart(CartRequest cartRequest);

    void removeProductToCart(Long productId);

    Paginated<ArticleDetailsCartResponse> getAllArticlesPaginatedByIds(
            int page, int size, String sort, boolean ascending,
            String categoryName, String brandName);

    List<CartResponse> findCartByUserId();

    void deleteCart();

    String getLatestCartUpdateDate();

    CartResponse updateCartQuantity(CartUpdateQuantityRequest cartRequest);
}
