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

    private CartModel cartModel;
    private CartEntity cartEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartModel = new CartModel();
        cartEntity = new CartEntity();
    }

    @Test
    @DisplayName("Should add an article to the cart and return the updated cart model")
    void shouldAddArticleToCart() {
        when(cartEntityMapper.cartModelToCartEntity(any(CartModel.class))).thenReturn(cartEntity);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(cartEntity);
        when(cartEntityMapper.cartEntityToCartModel(any(CartEntity.class))).thenReturn(cartModel);

        CartModel result = cartJpaAdapter.addArticleToCart(cartModel);

        assertEquals(cartModel, result);

        verify(cartRepository, times(1)).save(cartEntity);
        verify(cartEntityMapper, times(1)).cartModelToCartEntity(cartModel);
        verify(cartEntityMapper, times(1)).cartEntityToCartModel(cartEntity);
    }

    @Test
    @DisplayName("Should find an article by user ID and article ID and return the cart model")
    void shouldFindArticleByUserIdAndArticleId() {
        when(cartRepository.findByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(cartEntity);
        when(cartEntityMapper.cartEntityToCartModel(any(CartEntity.class))).thenReturn(cartModel);

        CartModel result = cartJpaAdapter.findArticleByUserIdAndArticleId(1L, 1L);

        assertEquals(cartModel, result);

        verify(cartRepository, times(1)).findByUserIdAndArticleId(1L, 1L);
        verify(cartEntityMapper, times(1)).cartEntityToCartModel(cartEntity);
    }

    @Test
    @DisplayName("Should find article IDs by user ID and return a list of article IDs")
    void shouldFindArticleIdsByUserId() {
        List<CartEntity> cartEntities = Collections.singletonList(new CartEntity());
        when(cartRepository.findByUserId(anyLong())).thenReturn(cartEntities);

        List<Long> result = cartJpaAdapter.findArticleIdsByUserId(1L);

        assertEquals(1, result.size());

        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Should remove an article from the cart by user ID and article ID")
    void shouldRemoveArticleFromCart() {
        when(cartRepository.findByUserIdAndArticleId(anyLong(), anyLong())).thenReturn(cartEntity);
        doNothing().when(cartRepository).delete(any(CartEntity.class));

        cartJpaAdapter.removeArticleFromCart(1L, 1L);

        verify(cartRepository, times(1)).delete(cartEntity);
        verify(cartRepository, times(1)).findByUserIdAndArticleId(1L, 1L);
    }

    @Test
    @DisplayName("Should update the last updated date of cart items for a given user ID")
    void shouldUpdateCartItemsLastUpdatedDate() {
        List<CartEntity> cartEntities = Collections.singletonList(new CartEntity());
        when(cartRepository.findByUserId(anyLong())).thenReturn(cartEntities);
        when(cartRepository.save(any(CartEntity.class))).thenReturn(new CartEntity());

        cartJpaAdapter.updateCartItemsUpdatedAt(1L, LocalDate.now());

        verify(cartRepository, times(1)).save(any(CartEntity.class));
        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    @DisplayName("Should find the cart by user ID and return a list of cart models")
    void shouldFindCartByUserId() {
        List<CartEntity> cartEntities = Collections.singletonList(new CartEntity());
        List<CartModel> cartModels = Collections.singletonList(new CartModel());

        when(cartRepository.findByUserId(anyLong())).thenReturn(cartEntities);
        when(cartEntityMapper.cartEntitiesListToCartModelsList(anyList())).thenReturn(cartModels);

        List<CartModel> result = cartJpaAdapter.findCartByUserId(1L);

        assertEquals(cartModels, result);

        verify(cartRepository, times(1)).findByUserId(1L);
        verify(cartEntityMapper, times(1)).cartEntitiesListToCartModelsList(cartEntities);
    }

    @Test
    @DisplayName("Should delete the cart for a given user ID")
    void shouldDeleteCart() {
        doNothing().when(cartRepository).deleteByUserId(anyLong());

        cartJpaAdapter.deleteCart(1L);

        verify(cartRepository, times(1)).deleteByUserId(1L);
    }

    @Test
    @DisplayName("Should get the latest cart update date for a given user ID")
    void shouldGetLatestCartUpdateDate() {
        cartEntity.setLastUpdatedDate(LocalDate.now());
        when(cartRepository.findTopByUserIdOrderByLastUpdatedDateDesc(anyLong())).thenReturn(cartEntity);

        LocalDate result = cartJpaAdapter.getLatestCartUpdateDate(1L);

        assertEquals(cartEntity.getLastUpdatedDate(), result);

        verify(cartRepository, times(1)).findTopByUserIdOrderByLastUpdatedDateDesc(1L);
    }
}