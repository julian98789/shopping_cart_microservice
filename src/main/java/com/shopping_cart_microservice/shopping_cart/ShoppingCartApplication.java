package com.shopping_cart_microservice.shopping_cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign")
public class ShoppingCartApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingCartApplication.class, args);
	}

}
