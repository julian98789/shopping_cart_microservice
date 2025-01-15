package com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign;

import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = Util.TRANSACTION_SERVICE_NAME, url = Util.TRANSACTION_SERVICE_URL)
public interface ISupplyFeignClient {

    @GetMapping("/api/supply/next-supply-date/{supplyId}")
    String getNextSupplyDate(@PathVariable Long supplyId);
}
