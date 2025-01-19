package com.shopping_cart_microservice.shopping_cart.infrastructure.persistence.jpa.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopping_cart_microservice.shopping_cart.domain.spi.ISupplyConnectionPersistencePort;
import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign.ISupplyFeignClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SupplyConnectionAdapter implements ISupplyConnectionPersistencePort {

    private final ISupplyFeignClient supplyFeignClient;
    private final ObjectMapper objectMapper;

    @Override
    public String getNextSupplyDate(Long articleId) {
        try {
            String nextSupplyDateJson = supplyFeignClient.getNextSupplyDate(articleId);
            JsonNode jsonNode = objectMapper.readTree(nextSupplyDateJson);
            return jsonNode.get(Util.NEXT_SUPPLY_DATE_KEY).asText();
        } catch (Exception e) {
            return null;
        }
    }
}
