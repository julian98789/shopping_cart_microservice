package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter;

import com.shopping_cart_microservice.shopping_cart.application.dto.articledto.ArticleCartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.articledto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.application.mapper.articlemapper.IArticleRequestMapper;
import com.shopping_cart_microservice.shopping_cart.application.mapper.articlemapper.IArticleResponseMapper;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.spi.IStockConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;
import com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign.IStockFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class StockConnectionAdapter implements IStockConnectionPersistencePort {

    private final IStockFeignClient stockFeignClient;
    private final IArticleRequestMapper articleRequestMapper;
    private final IArticleResponseMapper articleResponseMapper;

    private static final Logger logger = LoggerFactory.getLogger(StockConnectionAdapter.class);
    @Override
    public boolean existById(Long articleId) {
        try {
            logger.info("El ID del articulo es: {}", articleId);
            return stockFeignClient.articleExistsById(articleId);
        } catch (FeignException.NotFound e) {
            return false;
        }
    }

    @Override
    public boolean isStockSufficient(Long articleId, Integer articleQuantity) {
        try {
            return stockFeignClient.isStockSufficient(articleId, articleQuantity);
        } catch (FeignException.NotFound e) {
            return false;
        }
    }

    @Override
    public List<String> getCategoryNamesByarticleId(Long articleId) {
        try {
            return stockFeignClient.getCategoryNamesByArticleId(articleId);
        } catch (FeignException.NotFound e) {
            return Collections.emptyList();
        }
    }

    @Override
    public Paginated<ArticleDetailsCartModel> getAllArticlesPaginatedByIds(
            int page, int size, String sort, boolean ascending,
            String categoryName, String brandName, ArticleCartModel cartModel) {

            ArticleCartRequest articleCartRequest = articleRequestMapper.articleCartModelToArticleCartRequest(cartModel);

            Paginated<ArticleDetailsCartResponse> paginatedResponse = stockFeignClient.getArticlesCart(
                    page, size, sort, ascending, categoryName, brandName, articleCartRequest);

            List<ArticleDetailsCartModel> content = paginatedResponse.getContent().stream()
                    .map(articleResponseMapper::articleDetailsCartResponseToArticleDetailsCartModel)
                    .toList();

        return new Paginated<>(content, page, size, paginatedResponse.getTotalElements());

    }

}
