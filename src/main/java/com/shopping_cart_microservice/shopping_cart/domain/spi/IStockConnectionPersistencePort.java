package com.shopping_cart_microservice.shopping_cart.domain.spi;

import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleCartRequest;
import com.shopping_cart_microservice.shopping_cart.application.dto.article_dto.ArticleDetailsCartResponse;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;

import java.util.List;

public interface IStockConnectionPersistencePort {

    boolean existById(Long articleId);

    boolean isStockSufficient(Long articleId, Integer articleQuantity);

    List<String> getCategoryNamesByarticleId(Long articleId);

    Paginated<ArticleDetailsCartResponse> getAllArticlesPaginatedByIds(
            int page,
            int size,
            String sort,
            boolean ascending,
            String categoryName,
            String brandName,
            ArticleCartRequest articleCartRequest
    );

}
