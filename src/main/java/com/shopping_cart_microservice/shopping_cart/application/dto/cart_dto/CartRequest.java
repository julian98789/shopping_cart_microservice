package com.shopping_cart_microservice.shopping_cart.application.dto.cart_dto;

import com.shopping_cart_microservice.shopping_cart.domain.util.Util;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {

    @NotNull(message = Util.ARTICLE_QUANTITY_REQUIRED)
    @Min(value = Util.ARTICLE_QUANTITY_MIN, message = Util.ARTICLE_QUANTITY_MIN_MESSAGE)
    private Integer quantity;

    @NotNull(message = Util.ARTICLE_REQUIRED)
    private Long articleId;
}
