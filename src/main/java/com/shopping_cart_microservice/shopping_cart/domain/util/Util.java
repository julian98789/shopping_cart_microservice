package com.shopping_cart_microservice.shopping_cart.domain.util;

public class Util {

    public static final String ROLE_CLIENT = "hasRole('CLIENT')";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String AUTH_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String STOCK_SERVICE_NAME = "stock";
    public static final String STOCK_SERVICE_URL = "http://localhost:8080";
    public static final String TRANSACTION_SERVICE_NAME = "transaction";
    public static final String TRANSACTION_SERVICE_URL = "http://localhost:8082";
    public static final String ARTICLE_NOT_FOUND = "Article not found";
    public static final String ARTICLE_QUANTITY_REQUIRED = "Article quantity required";
    public static final String ARTICLE_REQUIRED = "Article required";
    public static final String NEXT_SUPPLY_DATE_KEY = "nextSupplyDate";
    public static final String INSUFFICIENT_STOCK = "Insufficient stock";
    public static final String CATEGORIES_LIMIT_EXCEEDED = "Categories limit exceeded for category: ";
    public static final String ARTICLE_DELETED_SUCCESSFULLY = "Article successfully removed from cart";
    public static final int TOKEN_PREFIX_LENGTH = 7;
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final int ARTICLE_QUANTITY_MIN = 1;
    public static final String ARTICLE_QUANTITY_MIN_MESSAGE = "Quantity must be greater than 0";
    public static final String DELETE_CART_RESPONSE_BODY = "Cart successfully deleted";

    private Util() {
    }
}
