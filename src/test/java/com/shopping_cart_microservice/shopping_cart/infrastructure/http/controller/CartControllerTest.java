package com.shopping_cart_microservice.shopping_cart.infrastructure.http.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.articledto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.dto.cartdto.CartUpdateQuantityRequest;
import com.shopping_cart_microservice.shopping_cart.application.handler.cardhandler.ICartHandler;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICartHandler cartHandler;

    @InjectMocks
    private CartController cartController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    @DisplayName("Should return 201 Created when adding an article to the cart")
    void addArticleToCart_ShouldReturnCreated() throws Exception {
        CartRequest cartRequest = new CartRequest();
        cartRequest.setArticleId(1L);
        cartRequest.setQuantity(2);

        CartResponse cartResponse = new CartResponse();

        when(cartHandler.addArticleToCart(any(CartRequest.class))).thenReturn(cartResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/add-article-to-card")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(cartResponse)));

        verify(cartHandler, times(1)).addArticleToCart(any(CartRequest.class));
    }

    @Test
    @DisplayName("Should return 200 OK when removing an article from the cart")
    void removeArticleFromCart_ShouldReturnOk() throws Exception {
        doNothing().when(cartHandler).removeProductToCart(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/delete-article-from-cart/{articleId}", 1L))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Util.ARTICLE_DELETED_SUCCESSFULLY));

        verify(cartHandler, times(1)).removeProductToCart(1L);
    }

    @Test
    @DisplayName("Should return 200 OK when getting all articles paginated by IDs")
    void getAllArticlesPaginatedByIds_ShouldReturnOk() throws Exception {
        Paginated<ArticleDetailsCartResponse> paginatedResponse = new Paginated<>
                (Collections.emptyList(), 0, 10, 0);

        when(cartHandler.getAllArticlesPaginatedByIds(anyInt(), anyInt(), anyString(), anyBoolean(), any(), any()))
                .thenReturn(paginatedResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/article-cart")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name")
                        .param("ascending", "true"))
                .andExpect(status().isOk());

        verify(cartHandler, times(1))
                .getAllArticlesPaginatedByIds(0, 10, "name", true, null, null);
    }

    @Test
    @DisplayName("Should return 200 OK when getting the cart by user")
    void buyUser_ShouldReturnOk() throws Exception {
        List<CartResponse> cartResponses = Collections.emptyList();
        when(cartHandler.findCartByUserId()).thenReturn(cartResponses);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/get-cart-by-user"))
                .andExpect(status().isOk());

        verify(cartHandler, times(1)).findCartByUserId();
    }

    @Test
    @DisplayName("Should return 200 OK when deleting the cart")
    void deleteCart_ShouldReturnOk() throws Exception {
        doNothing().when(cartHandler).deleteCart();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/delete-cart"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Util.DELETE_CART_RESPONSE_BODY));

        verify(cartHandler, times(1)).deleteCart();
    }

    @Test
    @DisplayName("Should return 200 OK when getting the latest cart update date")
    void getLatestCartUpdateDate_ShouldReturnOk() throws Exception {
        when(cartHandler.getLatestCartUpdateDate()).thenReturn("2025-02-11T12:00:00Z");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/latest-update"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("2025-02-11T12:00:00Z"));

        verify(cartHandler, times(1)).getLatestCartUpdateDate();
    }

    @Test
    @DisplayName("Should return 201 Created when updating product quantity in the cart")
    void updateProductQuantity_ShouldReturnCreated() throws Exception {
        CartUpdateQuantityRequest request = new CartUpdateQuantityRequest();
        CartResponse response = new CartResponse();

        when(cartHandler.updateCartQuantity(any(CartUpdateQuantityRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/cart/update-quantity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(cartHandler, times(1)).updateCartQuantity(any(CartUpdateQuantityRequest.class));
    }
}