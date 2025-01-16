package com.shopping_cart_microservice.shopping_cart.infrastructure.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping_cart_microservice.shopping_cart.domain.api.ICartModelServicePort;
import com.shopping_cart_microservice.shopping_cart.domain.security.IAuthenticationSecurityPort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ICartModelPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.IStockConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ISupplyConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.usecase.CartUseCase;
import com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign.IStockFeignClient;
import com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign.ISupplyFeignClient;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter.CartJpaAdapter;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter.StockConnectionAdapter;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter.SupplyConnectionAdapter;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.mapper.ICartEntityMapper;
import com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.repository.ICartRepository;
import com.shopping_cart_microservice.shopping_cart.infrastructure.security.adapter.AuthenticationAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public ICartModelPersistencePort cartPersistencePort(ICartRepository cartRepository, ICartEntityMapper cartEntityMapper) {
        return new CartJpaAdapter( cartEntityMapper, cartRepository);
    }

    @Bean
    public IStockConnectionPersistencePort stockConnectionPersistencePort(IStockFeignClient stockFeignClient) {
        return new StockConnectionAdapter(stockFeignClient);
    }

    @Bean
    public ISupplyConnectionPersistencePort supplyConnectionPersistencePort(ISupplyFeignClient supplyFeignClient, ObjectMapper objectMapper) {
        return new SupplyConnectionAdapter(supplyFeignClient, objectMapper);
    }

    @Bean
    public ICartModelServicePort cartServicePort(
            ICartModelPersistencePort cartPersistencePort,
            IStockConnectionPersistencePort stockConnectionPersistencePort,
            ISupplyConnectionPersistencePort supplyConnectionPersistencePort,
            IAuthenticationSecurityPort authenticationSecurityPort
    ) {
        return new CartUseCase( cartPersistencePort, stockConnectionPersistencePort,  supplyConnectionPersistencePort, authenticationSecurityPort);
    }

    @Bean
    public IAuthenticationSecurityPort authenticationSecurityPort() {
        return new AuthenticationAdapter();
    }
}
