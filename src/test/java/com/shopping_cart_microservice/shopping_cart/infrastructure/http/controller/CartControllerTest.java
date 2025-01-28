package com.shopping_cart_microservice.shopping_cart.infrastructure.http.controller;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.application.handler.card_handler.ICartHandler;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class CartControllerTest {

    @Mock
    private ICartHandler cartHandler;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("Agregar articulo al carrito")
    void testAddArticleToCart() {
        CartRequest cartRequest = new CartRequest();
        CartResponse cartResponse = new CartResponse();
        when(cartHandler.addArticleToCart(any(CartRequest.class))).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.addArticleToCart(cartRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cartResponse, response.getBody());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("Eliminar articulo del carrito")
    void testRemoveArticleFromCart() {
        ResponseEntity<String> response = cartController.removeArticleFromCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Util.ARTICLE_DELETED_SUCCESSFULLY, response.getBody());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("Obtener todos los articulos paginados por IDs")
    void testGetAllArticlesPaginatedByIds() {
        Paginated<ArticleDetailsCartResponse> paginatedResponse = new Paginated<>(Collections.emptyList(), 0, 0, 10);
        when(cartHandler.getAllArticlesPaginatedByIds(anyInt(), anyInt(), anyString(), anyBoolean(), nullable(String.class), nullable(String.class))).thenReturn(paginatedResponse);

        ResponseEntity<Paginated<ArticleDetailsCartResponse>> response = cartController.getAllArticlesPaginatedByIds(0, 10, "name", true, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paginatedResponse, response.getBody());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("Comprar productos")
    void testBuyProducts() {
        List<CartResponse> cartResponses = Collections.emptyList();
        when(cartHandler.findCartByUserId()).thenReturn(cartResponses);

        ResponseEntity<List<CartResponse>> response = cartController.buyUser();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartResponses, response.getBody());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("Eliminar carrito")
    void testDeleteCart() {
        ResponseEntity<String> response = cartController.deleteCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Util.DELETE_CART_RESPONSE_BODY, response.getBody());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("Obtener la ultima fecha de actualizacion del carrito")
    void testGetLatestCartUpdateDate() {
        String latestUpdateDate = "2023-10-10";
        when(cartHandler.getLatestCartUpdateDate()).thenReturn(latestUpdateDate);

        ResponseEntity<String> response = cartController.getLatestCartUpdateDate();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(latestUpdateDate, response.getBody());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    @DisplayName("Actualizar cantidad de productos")
    void testUpdateProductQuantity() {
        CartUpdateQuantityRequest cartRequest = new CartUpdateQuantityRequest();
        CartResponse cartResponse = new CartResponse();
        when(cartHandler.updateCartQuantity(any(CartUpdateQuantityRequest.class))).thenReturn(cartResponse);

        ResponseEntity<CartResponse> response = cartController.updateProductQuantity(cartRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cartResponse, response.getBody());
    }
}