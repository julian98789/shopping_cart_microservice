package com.shopping_cart_microservice.shopping_cart.domain.model.stock.article;

import com.shopping_cart_microservice.shopping_cart.domain.model.stock.brand.BrandModel;
import com.shopping_cart_microservice.shopping_cart.domain.model.stock.category.CategoryModel;

import java.util.List;


public class ArticleDetailsCartModel {

    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private double price;
    private BrandModel brand;
    private List<CategoryModel> categories;
    private Integer cartQuantity;
    private String nextSupplyDate;
    private Double subtotal;

    public ArticleDetailsCartModel(
            Long id, String name, String description, Integer quantity,
            double price, BrandModel brand, List<CategoryModel> categories,
            Integer cartQuantity, String nextSupplyDate, Double subtotal) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
        this.brand = brand;
        this.categories = categories;
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

    public BrandModel getBrand() {
        return brand;
    }

    public void setBrand(BrandModel brand) {
        this.brand = brand;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
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
