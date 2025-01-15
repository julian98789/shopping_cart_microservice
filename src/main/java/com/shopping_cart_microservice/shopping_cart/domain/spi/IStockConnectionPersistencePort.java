package com.shopping_cart_microservice.shopping_cart.domain.spi;

import java.util.List;

public interface IStockConnectionPersistencePort {

    boolean existById(Long articleId);

    boolean isStockSufficient(Long articleId, Integer articleQuantity);


    List<String> getCategoryNamesByarticleId(Long articleId);
}
