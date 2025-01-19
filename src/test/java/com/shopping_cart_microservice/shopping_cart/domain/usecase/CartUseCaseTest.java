package com.shopping_cart_microservice.shopping_cart.domain.usecase;


import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.security.IAuthenticationSecurityPort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ICartModelPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.IStockConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ISupplyConnectionPersistencePort;
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
    private ISupplyConnectionPersistencePort supplyConnectionPersistencePort;

    @Mock
    private IAuthenticationSecurityPort authenticationPersistencePort;

    @InjectMocks
    private CartUseCase cartUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Agregar articulo al carrito")
    void testAddArticleToCart() {
        CartModel cartModel = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(stockConnectionPersistencePort.existById(anyLong())).thenReturn(true);
        when(cartPersistencePort.findArticleByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(null);
        when(stockConnectionPersistencePort.isStockSufficient(anyLong(), anyInt())).thenReturn(true);
        when(cartPersistencePort.addArticleToCart(any(CartModel.class))).thenReturn(cartModel);

        CartModel result = cartUseCase.addArticleToCart(cartModel);

        assertEquals(cartModel, result);
    }

    @Test
    @DisplayName("Eliminar producto del carrito")
    void testRemoveProductToCart() {
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findArticleByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now()));

        cartUseCase.removeProductToCart(1L);

        verify(cartPersistencePort, times(1)).removeArticleFromCart(anyLong(), anyLong());
        verify(cartPersistencePort, times(1)).updateCartItemsUpdatedAt(anyLong(), any(LocalDate.class));
    }

    @Test
    @DisplayName("Buscar carrito por ID de usuario")
    void testFindCartByUserId() {
        List<CartModel> cartModels = Collections.singletonList(new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now()));
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findCartByUserId(anyLong())).thenReturn(cartModels);

        List<CartModel> result = cartUseCase.findCartByUserId();

        assertEquals(cartModels, result);
    }

    @Test
    @DisplayName("Eliminar carrito")
    void testDeleteCart() {
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);

        cartUseCase.deleteCart();

        verify(cartPersistencePort, times(1)).deleteCart(anyLong());
    }

    @Test
    @DisplayName("Obtener la ultima fecha de actualizacion del carrito")
    void testGetLatestCartUpdateDate() {
        LocalDate latestUpdateDate = LocalDate.of(2023, 10, 10);
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.getLatestCartUpdateDate(anyLong())).thenReturn(latestUpdateDate);

        String result = cartUseCase.getLatestCartUpdateDate();

        assertEquals("2023-10-10", result);
    }

    @Test
    @DisplayName("Actualizar cantidad de productos")
    void testUpdateCartQuantity() {
        CartModel cartModel = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        CartModel existingCart = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findArticleByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(existingCart);
        when(stockConnectionPersistencePort.isStockSufficient(anyLong(), anyInt())).thenReturn(true);
        when(cartPersistencePort.addArticleToCart(any(CartModel.class))).thenReturn(cartModel);

        CartModel result = cartUseCase.updateCartQuantity(cartModel);

        assertEquals(cartModel, result);
    }

    @Test
    @DisplayName("Obtener todos los artículos paginados por IDs")
    void testFindArticleIdsByUserId() {
        List<Long> articleIds = Collections.singletonList(1L);
        Paginated<ArticleDetailsCartModel> paginatedArticles = new Paginated<>(Collections.emptyList(), 0, 0, 0);
        when(authenticationPersistencePort.getAuthenticatedUserId()).thenReturn(1L);
        when(cartPersistencePort.findArticleIdsByUserId(anyLong())).thenReturn(articleIds);
        when(stockConnectionPersistencePort.getAllArticlesPaginatedByIds(anyInt(), anyInt(), anyString(), anyBoolean(), anyString(), anyString(), any(ArticleCartModel.class)))
                .thenReturn(paginatedArticles);

        Paginated<ArticleDetailsCartModel> result = cartUseCase.findArticleIdsByUserId(0, 10, "name", true, "electronics", "brandA");

        assertEquals(paginatedArticles, result);
    }
}