package com.shopping_cart_microservice.shopping_cart.domain.usecase;


import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.security.IAuthenticationSecurityPort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ICartModelPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.IStockConnectionPersistencePort;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CartUseCaseTest {

    @Mock
    private ICartModelPersistencePort cartPersistencePort;

    @Mock
    private IStockConnectionPersistencePort stockConnectionPersistencePort;

    @Mock
    private IAuthenticationSecurityPort authenticationPersistencePort;

    @InjectMocks
    private CartUseCase cartUseCase;

    private CartModel cartModel;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartModel = new CartModel();
    }

    @Test
    @DisplayName("Should add an article to the cart and return the updated cart model")
    void shouldAddArticleToCart() {
        cartModel.setId(1L);
        cartModel.setArticleId(1L);
        cartModel.setQuantity(1);

        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(stockConnectionPersistencePort.existById(anyLong())).thenReturn(true);
        when(cartPersistencePort.findArticleByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(null);
        when(stockConnectionPersistencePort.isStockSufficient(anyLong(), anyInt())).thenReturn(true);
        when(cartPersistencePort.addArticleToCart(any(CartModel.class))).thenReturn(cartModel);

        CartModel result = cartUseCase.addArticleToCart(cartModel);

        assertEquals(cartModel, result);

        verify(authenticationPersistencePort, times(1)).getAuthenticatedUserId();
        verify(stockConnectionPersistencePort, times(1)).existById(anyLong());
        verify(cartPersistencePort, times(1)).addArticleToCart(any(CartModel.class));
        verify(stockConnectionPersistencePort, times(1)).isStockSufficient(anyLong(), anyInt());
        verify(cartPersistencePort, times(1)).addArticleToCart(any(CartModel.class));
    }

    @Test
    @DisplayName("Should remove an article from the cart by user ID and article ID")
    void shouldRemoveArticleFromCart() {
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findArticleByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(new CartModel());

        cartUseCase.removeProductToCart(1L);

        verify(cartPersistencePort, times(1)).removeArticleFromCart(anyLong(), anyLong());
        verify(cartPersistencePort, times(1)).updateCartItemsUpdatedAt(anyLong(), any(LocalDate.class));
    }

    @Test
    @DisplayName("Should find the cart by user ID and return a list of cart models")
    void shouldFindCartByUserId() {
        List<CartModel> cartModels = Collections.singletonList(new CartModel());
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findCartByUserId(anyLong())).thenReturn(cartModels);

        List<CartModel> result = cartUseCase.findCartByUserId();

        assertEquals(cartModels, result);

        verify(cartPersistencePort, times(1)).findCartByUserId(anyLong());
        verify(authenticationPersistencePort, times(1)).getAuthenticatedUserId();
    }

    @Test
    @DisplayName("Should delete the cart for a given user ID")
    void shouldDeleteCart() {
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);

        cartUseCase.deleteCart();

        verify(cartPersistencePort, times(1)).deleteCart(anyLong());
    }

    @Test
    @DisplayName("Should get the latest cart update date for a given user ID")
    void shouldGetLatestCartUpdateDate() {
        LocalDate latestUpdateDate = LocalDate.of(2023, 10, 10);

        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.getLatestCartUpdateDate(anyLong())).thenReturn(latestUpdateDate);

        String result = cartUseCase.getLatestCartUpdateDate();

        assertEquals("2023-10-10", result);

        verify(authenticationPersistencePort, times(1)).getAuthenticatedUserId();
        verify(cartPersistencePort, times(1)).getLatestCartUpdateDate(anyLong());
    }

    @Test
    @DisplayName("Should update the quantity of products in the cart and return the updated cart model")
    void shouldUpdateCartQuantity() {
        CartModel cartCreated = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        CartModel existingCart = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());

        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findArticleByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(existingCart);
        when(stockConnectionPersistencePort.isStockSufficient(anyLong(), anyInt())).thenReturn(true);
        when(cartPersistencePort.addArticleToCart(any(CartModel.class))).thenReturn(cartCreated);

        CartModel result = cartUseCase.updateCartQuantity(cartCreated);

        assertEquals(cartCreated, result);

        verify(authenticationPersistencePort, times(1)).getAuthenticatedUserId();
        verify(cartPersistencePort, times(1)).findArticleByUserIdAndArticleId(anyLong(), anyLong());
        verify(stockConnectionPersistencePort, times(1)).isStockSufficient(anyLong(), anyInt());
        verify(cartPersistencePort, times(1)).addArticleToCart(any(CartModel.class));
    }

    @Test
    @DisplayName("Should find article IDs by user ID and return paginated article details")
    void shouldFindArticleIdsByUserId() {
        List<Long> articleIds = Collections.singletonList(1L);
        Paginated<ArticleDetailsCartModel> paginatedArticles = new Paginated<>(Collections.emptyList(), 0, 0, 0);

        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findArticleIdsByUserId(anyLong())).thenReturn(articleIds);
        when(stockConnectionPersistencePort.getAllArticlesPaginatedByIds(anyInt(), anyInt(), anyString(), anyBoolean(), anyString(), anyString(), any(ArticleCartModel.class)))
                .thenReturn(paginatedArticles);

        Paginated<ArticleDetailsCartModel> result = cartUseCase.findArticleIdsByUserId(0, 10, "name", true, "electronics", "brandA");

        assertEquals(paginatedArticles, result);

        verify(authenticationPersistencePort, times(1)).getAuthenticatedUserId();
        verify(cartPersistencePort, times(1)).findArticleIdsByUserId(anyLong());
        verify(stockConnectionPersistencePort, times(1)).getAllArticlesPaginatedByIds(anyInt(), anyInt(), anyString(), anyBoolean(), anyString(), anyString(), any(ArticleCartModel.class));
    }
}