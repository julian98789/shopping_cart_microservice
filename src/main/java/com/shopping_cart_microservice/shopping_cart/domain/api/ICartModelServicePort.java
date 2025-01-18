package com.shopping_cart_microservice.shopping_cart.domain.api;

import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;

import java.util.List;

public interface ICartModelServicePort {

    CartModel addArticleToCart(CartModel cartModel);

    void removeProductToCart(Long productId );

    Paginated<ArticleDetailsCartModel> findArticleIdsByUserId(
            int page, int size, String sort, boolean ascending, String categoryName, String brandName);

    List<CartModel> findCartByUserId();

    void deleteCart();

    String getLatestCartUpdateDate();
}
