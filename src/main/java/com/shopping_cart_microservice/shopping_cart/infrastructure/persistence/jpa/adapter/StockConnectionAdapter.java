package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter;

import com.shopping_cart_microservice.shopping_cart.domain.spi.IStockConnectionPersistencePort;
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
    private static final Logger logger = LoggerFactory.getLogger(StockConnectionAdapter.class);
    @Override
    public boolean existById(Long articleId) {
        try {
            logger.info("El ID del articulo es: {}", articleId);
            return stockFeignClient.getArticleById(articleId);
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
}
