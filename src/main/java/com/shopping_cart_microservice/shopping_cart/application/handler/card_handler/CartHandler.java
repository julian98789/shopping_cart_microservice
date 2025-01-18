package com.shopping_cart_microservice.shopping_cart.application.handler.card_handler;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.application.mapper.article_mapper.IArticleResponseMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart_mapper.ICartRequestMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart_mapper.ICartResponseMapper;
import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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
        Paginated<ArticleDetailsCartModel> articles = cartServicePort.findArticleIdsByUserId(
                page, size, sort, ascending, categoryName, brandName);

        List<ArticleDetailsCartResponse> responseList = articles.getContent().stream()
                .map(articleResponseMapper::articleModelToArticleDetailsCartResponse)
                .toList();

        return new Paginated<>(responseList, page, size, articles.getTotalElements());
    }

    @Override
    public List<CartResponse> findCartByUserId() {

        List<CartModel> cartModels = cartServicePort.findCartByUserId();
        return cartResponseMapper.cartModelListToCartResponseList(cartModels);
    }

    @Override
    public void deleteCart() {
        cartServicePort.deleteCart();
    }

    @Override
    public String getLatestCartUpdateDate() {
        return cartServicePort.getLatestCartUpdateDate();
    }

    @Override
    public CartResponse updateCartQuantity(CartUpdateQuantityRequest cartRequest) {

        CartModel cartModel = cartRequestMapper.cartUpdateRequestToCartModel(cartRequest);

        // Llamamos al servicio que contiene la lógica de negocio
        CartModel updatedCartModel = cartServicePort.updateCartQuantity(cartModel);

        // Mapeamos el modelo a la respuesta
        return cartResponseMapper.cartModelToCartResponse(updatedCartModel);
    }
}
