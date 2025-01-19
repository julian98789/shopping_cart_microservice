package com.shopping_cart_microservice.shopping_cart.domain.spi;

public interface ISupplyConnectionPersistencePort {
    String getNextSupplyDate(Long articleId);
}
