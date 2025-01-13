package com.shopping_cart_microservice.shopping_cart.infrastructure.http.controller;

import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @PreAuthorize(Util.ROLE_AUX_BODEGA )
    @GetMapping("/saludo")
    public String getSaludo() {
        return "¡Hola!";
    }
}