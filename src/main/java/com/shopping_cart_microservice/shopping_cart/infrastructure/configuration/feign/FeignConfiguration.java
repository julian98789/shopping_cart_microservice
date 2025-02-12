package com.shopping_cart_microservice.shopping_cart.infrastructure.configuration.feign;

import com.shopping_cart_microservice.shopping_cart.infrastructure.http.feign.interceptor.JwtRequestInterceptor;
import org.springframework.context.annotation.Configuration;
import feign.Client;
import feign.Logger;
import feign.RequestInterceptor;
import feign.httpclient.ApacheHttpClient;
import org.springframework.context.annotation.Bean;


@Configuration
public class FeignConfiguration {

    @Bean
    public Client feignClient() {
        return new ApacheHttpClient();
    }

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new JwtRequestInterceptor();
    }

    @Bean
    public Logger .Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

}