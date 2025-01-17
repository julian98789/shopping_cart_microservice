package com.shopping_cart_microservice.shopping_cart.application.handler.card_handler;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.mapper.article_mapper.IArticleResponseMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart_mapper.ICartRequestMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart_mapper.ICartResponseMapper;
import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Transactional
public class CartHandler implements ICartHandler{

    private final ICartRequestMapper cartRequestMapper;
    private final ICartResponseMapper cartResponseMapper;
    private final ICartModelServicePort cartServicePort;
    private final IArticleResponseMapper articleResponseMapper;

    @Override
    public CartResponse addArticleToCart(CartRequest cartRequest) {
        CartModel cartModel = cartRequestMapper.cartRequestToCartModel(cartRequest);
        CartModel saveCart = cartServicePort.addArticleToCart(cartModel);

        return cartResponseMapper.cartModelToCartResponse(saveCart);
    }

    @Override
    public void removeProductToCart(Long productId) {
        cartServicePort.removeProductToCart(productId);
    }

    @Override
    public Paginated<ArticleDetailsCartResponse> getAllArticlesPaginatedByIds(int page, int size, String sort, boolean ascending, String categoryName, String brandName) {

        return cartServicePort.findArticleIdsByUserId(page, size, sort, ascending, categoryName, brandName);
    }
}
