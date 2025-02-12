package com.shopping_cart_microservice.shopping_cart.domain.spi;

import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.article.ArticleDetailsCartModel;
import com.shopping_cart_microservice.shopping_cart.domain.util.Paginated;

import java.util.List;

public interface IStockConnectionPersistencePort {

    boolean existById(Long articleId);

    boolean isStockSufficient(Long articleId, Integer articleQuantity);

    List<String> getCategoryNamesByarticleId(Long articleId);

    Paginated<ArticleDetailsCartModel> getAllArticlesPaginatedByIds(
            int page,
            int size,
            String sort,
            boolean ascending,
            String categoryName,
            String brandName,
            ArticleCartModel cartModel
    );

}
