package com.shopping_cart_microservice.shopping_cart.domain.usecase;

import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.exception.CategoriesLimitException;
import com.shopping_cart_microservice.shopping_cart.domain.exception.InsufficientStockException;
import com.shopping_cart_microservice.shopping_cart.domain.exception.NotFoundException;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.cart.CartModel;
import com.shopping_cart_microservice.shopping_cart.domain.security.IAuthenticationSecurityPort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ICartModelPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.IStockConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ISupplyConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CartUseCase implements ICartModelServicePort {

    private final ICartModelPersistencePort cartPersistencePort;
    private final IStockConnectionPersistencePort stockConnectionPersistencePort;
    private final ISupplyConnectionPersistencePort supplyConnectionPersistencePort;
    private final IAuthenticationSecurityPort authenticationPersistencePort;


    public CartUseCase(ICartModelPersistencePort cartPersistencePort, IStockConnectionPersistencePort stockConnectionPersistencePort, ISupplyConnectionPersistencePort supplyConnectionPersistencePort, IAuthenticationSecurityPort authenticationPersistencePort) {
        this.cartPersistencePort = cartPersistencePort;
        this.stockConnectionPersistencePort = stockConnectionPersistencePort;
        this.supplyConnectionPersistencePort = supplyConnectionPersistencePort;
        this.authenticationPersistencePort = authenticationPersistencePort;
    }

    @Override
    public CartModel addArticleToCart(CartModel cartModel) {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        cartModel.setUserId(userId);
        validateProductExistence(cartModel.getArticleId());
        CartModel existingCart = findExistingCart(cartModel);

        int totalQuantity = cartModel.getQuantity() + (existingCart != null ? existingCart.getQuantity() : 0);
        validateStockAvailability(cartModel.getArticleId(), totalQuantity);

        if (existingCart != null) {
            updateExistingCart(existingCart, cartModel.getQuantity());
            return existingCart;
        }

        checkCategoriesLimit(cartModel.getUserId(), cartModel.getArticleId());
        createNewCart(cartModel);

        return cartPersistencePort.addArticleToCart(cartModel);
    }

    @Override
    public void removeProductToCart(Long articleId) {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        CartModel existingCart = cartPersistencePort.findArticleByUserIdAndArticleId(userId, articleId);

        if (existingCart == null) {
            throw new NotFoundException(Util.ARTICLE_NOT_FOUND);
        }

        cartPersistencePort.removeArticleFromCart(userId, articleId);
        cartPersistencePort.updateCartItemsUpdatedAt(userId, LocalDate.now());
    }

    @Override
    public Paginated<ArticleDetailsCartModel> findArticleIdsByUserId(int page, int size, String sort, boolean ascending, String categoryName, String brandName) {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        List<Long> articleIds = cartPersistencePort.findArticleIdsByUserId(userId);

        if (articleIds.isEmpty()) {
            return new Paginated<>(Collections.emptyList(), 0, page, size);
        }

        return getPaginatedArticles(page, size, sort, ascending, categoryName, brandName, articleIds, userId);
    }

    @Override
    public List<CartModel> findCartByUserId() {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        return cartPersistencePort.findCartByUserId(userId);
    }

    @Override
    public void deleteCart() {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        cartPersistencePort.deleteCart(userId);
    }

    @Override
    public String getLatestCartUpdateDate() {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        LocalDate lastUpdatedDate = cartPersistencePort.getLatestCartUpdateDate(userId);
        return formatDate(lastUpdatedDate);
    }

    @Override
    public CartModel updateCartQuantity(CartModel cartModel) {
        Long userId = authenticationPersistencePort.getAuthenticatedUserId();
        CartModel existingCart = cartPersistencePort.findArticleByUserIdAndArticleId(userId, cartModel.getArticleId());

        if (existingCart == null) {
            throw new NotFoundException(Util.ARTICLE_NOT_FOUND);
        }

        int totalQuantity = cartModel.getQuantity() + existingCart.getQuantity();

        validateStockAvailability(cartModel.getArticleId(), totalQuantity);

        existingCart.setQuantity(cartModel.getQuantity());
        existingCart.setLastUpdatedDate(LocalDate.now());

        return cartPersistencePort.addArticleToCart(existingCart);
    }



    // ---------------------------- Metodos auxiliares ----------------------------

    private CartModel findExistingCart(CartModel cartModel) {
        return cartPersistencePort.findArticleByUserIdAndArticleId(cartModel.getUserId(), cartModel.getArticleId());
    }

    private String formatDate(LocalDate date) {
        LocalDateTime localDateTime = date.atStartOfDay();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Util.DATE_FORMAT);
        return localDateTime.format(formatter);
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
        cartPersistencePort.addArticleToCart(existingCart);
    }

    private void checkCategoriesLimit(Long userId, Long articleId) {
        List<Long> productsCart = new ArrayList<>(cartPersistencePort.findArticleIdsByUserId(userId));
        productsCart.add(articleId);
        Map<String, Integer> categoryCountMap = countArticleCategories(productsCart);
        checkIfCategoriesExceedLimit(categoryCountMap);
    }

    private Map<String, Integer> countArticleCategories(List<Long> articleIds) {
        Map<String, Integer> categoryCountMap = new HashMap<>();
        for (Long articleId : articleIds) {
            List<String> categories = stockConnectionPersistencePort.getCategoryNamesByarticleId(articleId);
            categories.forEach(category -> categoryCountMap.put(category, categoryCountMap.getOrDefault(category, 0) + 1));
        }
        return categoryCountMap;
    }

    private void checkIfCategoriesExceedLimit(Map<String, Integer> categoryCountMap) {
        categoryCountMap.forEach((category, count) -> {
            if (count > 3) {
                throw new CategoriesLimitException(Util.CATEGORIES_LIMIT_EXCEEDED + category);
            }
        });
    }

    private void createNewCart(CartModel cartModel) {
        cartModel.setCreationDate(LocalDate.now());
        cartModel.setLastUpdatedDate(LocalDate.now());
    }

    private Paginated<ArticleDetailsCartModel> getPaginatedArticles(
            int page, int size, String sort, boolean ascending, String categoryName,
            String brandName, List<Long> articleIds, Long userId) {

        ArticleCartModel articleCartModel = new ArticleCartModel(articleIds);
        Paginated<ArticleDetailsCartModel> paginatedArticles = stockConnectionPersistencePort.getAllArticlesPaginatedByIds(
                page, size, sort, ascending, categoryName, brandName, articleCartModel);

        paginatedArticles.getContent().forEach(article -> updateArticleDetails(userId, article));
        return paginatedArticles;
    }

    private void updateArticleDetails(Long userId, ArticleDetailsCartModel article) {
        CartModel cart = cartPersistencePort.findArticleByUserIdAndArticleId(userId, article.getId());
        if (cart != null) {
            article.setCartQuantity(cart.getQuantity());
            article.setSubtotal(article.getPrice() * cart.getQuantity());


            boolean isStockSufficient = stockConnectionPersistencePort.isStockSufficient(article.getId(), cart.getQuantity());

            if (!isStockSufficient) {
                String nextSupplyDate = supplyConnectionPersistencePort.getNextSupplyDate(article.getId());
                article.setNextSupplyDate(nextSupplyDate != null ? nextSupplyDate : "N/A");
            } else {
                article.setNextSupplyDate(null);
            }
        } else {
            article.setCartQuantity(0);
            article.setSubtotal(0.0);
            article.setNextSupplyDate(null);
        }
    }
}




