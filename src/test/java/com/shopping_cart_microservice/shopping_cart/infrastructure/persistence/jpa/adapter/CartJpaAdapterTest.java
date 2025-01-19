package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter;

import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.entity.CartEntity;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.mapper.ICartEntityMapper;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.repository.ICartRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CartJpaAdapterTest {

    @Mock
    private ICartEntityMapper cartEntityMapper;

    @Mock
    private ICartRepository cartRepository;

    @InjectMocks
    private CartJpaAdapter cartJpaAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Agregar articulo al carrito")
    void testAddArticleToCart() {
        CartModel cartModel = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        CartEntity cartEntity = new CartEntity();
        when(cartEntityMapper.cartModelToCartEntity(any(CartModel.class))).thenReturn(cartEntity);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);
        when(cartEntityMapper.cartEntityToCartModel(any(CartEntity.class))).thenReturn(cartModel);

        CartModel result = cartJpaAdapter.addArticleToCart(cartModel);

        assertEquals(cartModel, result);
    }

    @Test
    @DisplayName("Buscar articulo por ID de usuario e ID de articulo")
    void testFindArticleByUserIdAndArticleId() {
        CartEntity cartEntity = new CartEntity();
        CartModel cartModel = new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now());
        when(cartRepository.findByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(cartEntity);
        when(cartEntityMapper.cartEntityToCartModel(any(CartEntity.class))).thenReturn(cartModel);

        CartModel result = cartJpaAdapter.findArticleByUserIdAndArticleId(1L, 1L);

        assertEquals(cartModel, result);
    }

    @Test
    @DisplayName("Buscar IDs de articulos por ID de usuario")
    void testFindArticleIdsByUserId() {
        List<CartEntity> cartEntities = Collections.singletonList(new CartEntity());
        when(cartRepository.findByUserId(anyLong())).thenReturn(cartEntities);

        List<Long> result = cartJpaAdapter.findArticleIdsByUserId(1L);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Eliminar articulo del carrito")
    void testRemoveArticleFromCart() {
        CartEntity cartEntity = new CartEntity();
        when(cartRepository.findByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(cartEntity);
        doNothing().when(cartRepository).delete(any(CartEntity.class));

        cartJpaAdapter.removeArticleFromCart(1L, 1L);

        verify(cartRepository, times(1)).delete(cartEntity);
    }

    @Test
    @DisplayName("Actualizar fecha de actualizacion de los articulos del carrito")
    void testUpdateCartItemsUpdatedAt() {
        List<CartEntity> cartEntities = Collections.singletonList(new CartEntity());
        when(cartRepository.findByUserId(anyLong())).thenReturn(cartEntities);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(new CartEntity());

        cartJpaAdapter.updateCartItemsUpdatedAt(1L, LocalDate.now());

        verify(cartRepository, times(1)).save(any(CartEntity.class));
    }

    @Test
    @DisplayName("Buscar carrito por ID de usuario")
    void testFindCartByUserId() {
        List<CartEntity> cartEntities = Collections.singletonList(new CartEntity());
        List<CartModel> cartModels = Collections.singletonList(new CartModel(1L, 1L, 1L, 1, LocalDate.now(), LocalDate.now()));
        when(cartRepository.findByUserId(anyLong())).thenReturn(cartEntities);
        when(cartEntityMapper.cartEntitiesListToCartModelsList(anyList())).thenReturn(cartModels);

        List<CartModel> result = cartJpaAdapter.findCartByUserId(1L);

        assertEquals(cartModels, result);
    }

    @Test
    @DisplayName("Eliminar carrito")
    void testDeleteCart() {
        doNothing().when(cartRepository).deleteByUserId(anyLong());

        cartJpaAdapter.deleteCart(1L);

        verify(cartRepository, times(1)).deleteByUserId(1L);
    }

    @Test
    @DisplayName("Obtener la ultima fecha de actualizacion del carrito")
    void testGetLatestCartUpdateDate() {
        CartEntity cartEntity = new CartEntity();
        cartEntity.setLastUpdatedDate(LocalDate.now());
        when(cartRepository.findTopByUserIdOrderByLastUpdatedDateDesc(anyLong())).thenReturn(cartEntity);

        LocalDate result = cartJpaAdapter.getLatestCartUpdateDate(1L);

        assertEquals(cartEntity.getLastUpdatedDate(), result);
    }
}