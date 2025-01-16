package com.shopping_cart_microservice.shopping_cart.application.handler.card_handler;

import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto.CartResponse;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart.ICartRequestMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.cart.ICartResponseMapper;
import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.security.IAuthenticationSecurityPort;
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
    private final IAuthenticationSecurityPort authenticationPersistencePort;

    @Override
    public CartResponse addArticleToCart(CartRequest cartRequest) {
        CartModel cartModel = cartRequestMapper.cartRequestToCartModel(cartRequest);
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        cartModel.setUserId(userId);

        CartModel saveCart = cartServicePort.addArticleToCart(cartModel);

        return cartResponseMapper.cartModelToCartResponse(saveCart);
    }

    @Override
    public void removeProductToCart(Long productId) {
        cartServicePort.removeProductToCart(productId);
    }
}
