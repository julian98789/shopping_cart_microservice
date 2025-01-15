package com.shopping_cart_microservice.shopping_cart.domain.usecase;

import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.exception.CategoriesLimitException;
import com.shopping_cart_microservice.shopping_cart.domain.exception.InsufficientStockException;
import com.shopping_cart_microservice.shopping_cart.domain.exception.NotFoundException;
import com.shopping_cart_microservice.shopping_cart.domain.model.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.security.IAuthenticationSecurityPort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ICartModelPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.IStockConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ISupplyConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter.StockConnectionAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartUseCase implements ICartModelServicePort {
    private final IAuthenticationSecurityPort authenticationPersistencePort;
    private final ICartModelPersistencePort cartPersistencePort;
    private final IStockConnectionPersistencePort stockConnectionPersistencePort;
    private final ISupplyConnectionPersistencePort supplyConnectionPersistencePort;

    private static final Logger logger = LoggerFactory.getLogger(StockConnectionAdapter.class);

    public CartUseCase(IAuthenticationSecurityPort authenticationPersistencePort, ICartModelPersistencePort cartPersistencePort, IStockConnectionPersistencePort stockConnectionPersistencePort, ISupplyConnectionPersistencePort supplyConnectionPersistencePort) {
        this.authenticationPersistencePort = authenticationPersistencePort;
        this.cartPersistencePort = cartPersistencePort;
        this.stockConnectionPersistencePort = stockConnectionPersistencePort;
        this.supplyConnectionPersistencePort = supplyConnectionPersistencePort;
    }

    @Override
    public void addArticleToCart(CartModel cartModel) {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        cartModel.setUserId(userId);

        logger.info("ARTICULO ID: {}", cartModel.getArticleId());
        validateProductExistence(cartModel.getArticleId());

        CartModel existingCart = cartPersistencePort.findArticleByUserIdAndArticleId(userId, cartModel.getArticleId());

        int totalQuantity = cartModel.getQuantity();

        if (existingCart != null) {
            totalQuantity += existingCart.getQuantity();
        }

        validateStockAvailability(cartModel.getArticleId(), totalQuantity);

        if (existingCart != null) {
            updateExistingCart(existingCart, cartModel.getQuantity());
            cartModel.setCreationDate(existingCart.getCreationDate());
            cartModel.setLastUpdatedDate(existingCart.getLastUpdatedDate());
            return;
        }

        List<Long> productsCart = new ArrayList<>(cartPersistencePort.findArticleIdsByUserId(userId));
        productsCart.add(cartModel.getArticleId());
        checkCategoriesLimit(productsCart);
        createNewCart(cartModel);
    }

    private void createNewCart(CartModel cartModel) {
        cartModel.setCreationDate(LocalDate.now());
        cartModel.setLastUpdatedDate(LocalDate.now());
        cartPersistencePort.addProductToCart(cartModel);
    }

    private void validateProductExistence(Long articleId) {
        if (!stockConnectionPersistencePort.existById(articleId)) {
            throw new NotFoundException(Util.ARTICLE_NOT_FOUND);
        }
    }
    private void validateStockAvailability(Long articleId, int totalQuantity) {
        if (!stockConnectionPersistencePort.isStockSufficient(articleId, totalQuantity)) {
            String nextSupplyDate = supplyConnectionPersistencePort.getNextSupplyDate(articleId);
            throw new InsufficientStockException(Util.INSUFFICIENT_STOCK, nextSupplyDate);
        }
    }

    private void updateExistingCart(CartModel existingCart, int quantityToAdd) {
        existingCart.setQuantity(existingCart.getQuantity() + quantityToAdd);
        existingCart.setLastUpdatedDate(LocalDate.now());
        cartPersistencePort.addProductToCart(existingCart);
    }

    private void checkCategoriesLimit(List<Long> articleIds) {
        Map<String, Integer> categoryCountMap = new HashMap<>();
        for (Long articleId : articleIds) {
            List<String> categories = stockConnectionPersistencePort.getCategoryNamesByarticleId(articleId);
            updateCategoryCountMap(categoryCountMap, categories);
        }
    }

    private void updateCategoryCountMap(Map<String, Integer> categoryCountMap, List<String> categories) {
        for (String category : categories) {
            categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1);
            if (categoryCountMap.get(category) > 3) {
                throw new CategoriesLimitException(Util.CATEGORIES_LIMIT_EXCEEDED + category);
            }
        }
    }
}



