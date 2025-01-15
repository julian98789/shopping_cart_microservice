package com.shopping_cart_microservice.shopping_cart.domain.model;

import com.shopping_cart_microservice.shopping_cart.application.dto.stock_dto.BrandResponse;


public class ArticleModel {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private double price;
    private BrandResponse brand;
    private Integer cartQuantity;
    private String nextSupplyDate;
    private Double subtotal;

    public ArticleModel(Long id, String name, String description, Integer quantity, double price,
                        BrandResponse brand, Integer cartQuantity, String nextSupplyDate, Double subtotal) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.brand = brand;
        this.cartQuantity = cartQuantity;
        this.nextSupplyDate = nextSupplyDate;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BrandResponse getBrand() {
        return brand;
    }

    public void setBrand(BrandResponse brand) {
        this.brand = brand;
    }

    public Integer getCartQuantity() {
        return cartQuantity;
    }

    public void setCartQuantity(Integer cartQuantity) {
        this.cartQuantity = cartQuantity;
    }

    public String getNextSupplyDate() {
        return nextSupplyDate;
    }

    public void setNextSupplyDate(String nextSupplyDate) {
        this.nextSupplyDate = nextSupplyDate;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
