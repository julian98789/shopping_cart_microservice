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
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.brand.BrandModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.category.CategoryModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Agregar articulo al carrito")
    void testAddArticleToCart() {
        CartRequest cartRequest = new CartRequest();
        CartModel cartModel = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        CartResponse cartResponse = new CartResponse();
        when(cartRequestMapper.cartRequestToCartModel(any(CartRequest.class))).thenReturn(cartModel);
        when(cartServicePort.addArticleToCart(any(CartModel.class))).thenReturn(cartModel);
        when(cartResponseMapper.cartModelToCartResponse(any(CartModel.class))).thenReturn(cartResponse);

        CartResponse result = cartHandler.addArticleToCart(cartRequest);

        assertEquals(cartResponse, result);
    }

    @Test
    @DisplayName("Eliminar producto del carrito")
    void testRemoveProductToCart() {
        doNothing().when(cartServicePort).removeProductToCart(anyLong());

        cartHandler.removeProductToCart(1L);

        verify(cartServicePort, times(1)).removeProductToCart(1L);
    }



    @Test
    @DisplayName("Buscar carrito por ID de usuario")
    void testFindCartByUserId() {
        List<CartModel> cartModels = Collections.singletonList(new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now()));
        List<CartResponse> cartResponses = Collections.singletonList(new CartResponse());
        when(cartServicePort.findCartByUserId()).thenReturn(cartModels);
        when(cartResponseMapper.cartModelListToCartResponseList(anyList())).thenReturn(cartResponses);

        List<CartResponse> result = cartHandler.findCartByUserId();

        assertEquals(cartResponses, result);
    }

    @Test
    @DisplayName("Eliminar carrito")
    void testDeleteCart() {
        doNothing().when(cartServicePort).deleteCart();

        cartHandler.deleteCart();

        verify(cartServicePort, times(1)).deleteCart();
    }

    @Test
    @DisplayName("Obtener la ultima fecha de actualizacion del carrito")
    void testGetLatestCartUpdateDate() {
        String latestUpdateDate = "2023-10-10";
        when(cartServicePort.getLatestCartUpdateDate()).thenReturn(latestUpdateDate);

        String result = cartHandler.getLatestCartUpdateDate();

        assertEquals(latestUpdateDate, result);
    }

    @Test
    @DisplayName("Actualizar cantidad de productos")
    void testUpdateCartQuantity() {
        CartUpdateQuantityRequest cartRequest = new CartUpdateQuantityRequest();
        CartModel cartModel = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        CartResponse cartResponse = new CartResponse();
        when(cartRequestMapper.cartUpdateRequestToCartModel(any(CartUpdateQuantityRequest.class))).thenReturn(cartModel);
        when(cartServicePort.updateCartQuantity(any(CartModel.class))).thenReturn(cartModel);
        when(cartResponseMapper.cartModelToCartResponse(any(CartModel.class))).thenReturn(cartResponse);

        CartResponse result = cartHandler.updateCartQuantity(cartRequest);

        assertEquals(cartResponse, result);
    }

    @Test
    @DisplayName("Obtener todos los artículos paginados por IDs")
    void testGetAllArticlesPaginatedByIds() {
        int page = 0;
        int size = 10;
        String sort = "name";
        boolean ascending = true;
        String categoryName = "electronics";
        String brandName = "brandA";

        CategoryModel categoryModel = new CategoryModel(1L, "CategoryName");
        BrandModel brandModel = new BrandModel(); // No constructor
        ArticleDetailsCartModel articleModel = new ArticleDetailsCartModel(1L, "ArticleName",
                "Description", 10, 100.0, brandModel, List.of(categoryModel), 1,"2023-10-10", 1000.0);
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
    }
}