package com.shopping_cart_microservice.shopping_cart.application.handler.cardhandler;

import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.articledto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.application.mapper.articlemapper.IArticleResponseMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cartmapper.ICartRequestMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cartmapper.ICartResponseMapper;
import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartHandlerTest {

    @Mock
    private ICartRequestMapper cartRequestMapper;

    @Mock
    private ICartResponseMapper cartResponseMapper;

    @Mock
    private ICartModelServicePort cartServicePort;

    @Mock
    private IArticleResponseMapper articleResponseMapper;

    @InjectMocks
    private CartHandler cartHandler;

    private CartModel cartModel;
    private CartResponse cartResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartModel = new CartModel();
        cartResponse = new CartResponse();
    }

    @Test
    @DisplayName("Should add an article to the cart and return the updated cart response")
    void shouldAddArticleToCart() {
        CartRequest cartRequest = new CartRequest();

        when(cartRequestMapper.cartRequestToCartModel(any(CartRequest.class))).thenReturn(cartModel);
        when(cartServicePort.addArticleToCart(any(CartModel.class))).thenReturn(cartModel);
        when(cartResponseMapper.cartModelToCartResponse(any(CartModel.class))).thenReturn(cartResponse);

        CartResponse result = cartHandler.addArticleToCart(cartRequest);

        assertEquals(cartResponse, result);

        verify(cartRequestMapper, times(1)).cartRequestToCartModel(cartRequest);
        verify(cartServicePort, times(1)).addArticleToCart(cartModel);
        verify(cartResponseMapper, times(1)).cartModelToCartResponse(cartModel);
    }

    @Test
    @DisplayName("Should remove a product from the cart by product ID")
    void shouldRemoveProductFromCart() {
        doNothing().when(cartServicePort).removeProductToCart(anyLong());

        cartHandler.removeProductToCart(1L);

        verify(cartServicePort, times(1)).removeProductToCart(1L);
    }

    @Test
    @DisplayName("Should find the cart by user ID and return a list of cart responses")
    void shouldFindCartByUserId() {
        List<CartModel> cartModels = Collections.singletonList(new CartModel());
        List<CartResponse> cartResponses = Collections.singletonList(new CartResponse());

        when(cartServicePort.findCartByUserId()).thenReturn(cartModels);
        when(cartResponseMapper.cartModelListToCartResponseList(anyList())).thenReturn(cartResponses);

        List<CartResponse> result = cartHandler.findCartByUserId();

        assertEquals(cartResponses, result);

        verify(cartServicePort, times(1)).findCartByUserId();
        verify(cartResponseMapper, times(1)).cartModelListToCartResponseList(cartModels);
    }

    @Test
    @DisplayName("Should delete the cart")
    void shouldDeleteCart() {
        doNothing().when(cartServicePort).deleteCart();

        cartHandler.deleteCart();

        verify(cartServicePort, times(1)).deleteCart();
    }

    @Test
    @DisplayName("Should get the latest cart update date")
    void shouldGetLatestCartUpdateDate() {
        String latestUpdateDate = "2023-10-10";
        when(cartServicePort.getLatestCartUpdateDate()).thenReturn(latestUpdateDate);

        String result = cartHandler.getLatestCartUpdateDate();

        assertEquals(latestUpdateDate, result);

        verify(cartServicePort, times(1)).getLatestCartUpdateDate();
    }

    @Test
    @DisplayName("Should update the quantity of products in the cart and return the updated cart response")
    void shouldUpdateCartQuantity() {
        CartUpdateQuantityRequest cartRequest = new CartUpdateQuantityRequest();

        when(cartRequestMapper.cartUpdateRequestToCartModel(any(CartUpdateQuantityRequest.class))).thenReturn(cartModel);
        when(cartServicePort.updateCartQuantity(any(CartModel.class))).thenReturn(cartModel);
        when(cartResponseMapper.cartModelToCartResponse(any(CartModel.class))).thenReturn(cartResponse);

        CartResponse result = cartHandler.updateCartQuantity(cartRequest);

        assertEquals(cartResponse, result);

        verify(cartRequestMapper, times(1)).cartUpdateRequestToCartModel(cartRequest);
        verify(cartServicePort, times(1)).updateCartQuantity(cartModel);
        verify(cartResponseMapper, times(1)).cartModelToCartResponse(cartModel);
    }

    @Test
    @DisplayName("Should get all articles paginated by IDs and return a paginated response")
    void shouldGetAllArticlesPaginatedByIds() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;
        String categoryName = "electronics";
        String brandName = "brandA";

        ArticleDetailsCartModel articleModel = new ArticleDetailsCartModel();
        List<ArticleDetailsCartModel> articleModels = Collections.singletonList(articleModel);
        Paginated<ArticleDetailsCartModel> paginatedArticles = new Paginated<>(articleModels, page, size, 1);
        ArticleDetailsCartResponse articleResponse = new ArticleDetailsCartResponse();
        List<ArticleDetailsCartResponse> articleResponses = Collections.singletonList(articleResponse);

        when(cartServicePort.findArticleIdsByUserId(page, size, sort, ascending, categoryName, brandName))
                .thenReturn(paginatedArticles);
        when(articleResponseMapper.articleModelToArticleDetailsCartResponse(articleModel))
                .thenReturn(articleResponse);

        Paginated<ArticleDetailsCartResponse> result = cartHandler.getAllArticlesPaginatedByIds(
                page, size, sort, ascending, categoryName, brandName);

        assertEquals(articleResponses, result.getContent());
        assertEquals(page, result.getPageNumber());
        assertEquals(size, result.getPageSize());
        assertEquals(1, result.getTotalElements());

        verify(cartServicePort, times(1))
                .findArticleIdsByUserId(page, size, sort, ascending, categoryName, brandName);
        verify(articleResponseMapper, times(1))
                .articleModelToArticleDetailsCartResponse(articleModel);
    }
}