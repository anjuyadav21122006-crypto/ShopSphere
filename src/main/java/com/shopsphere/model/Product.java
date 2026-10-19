
package com.shopsphere.model;

import java.math.BigDecimal;

public class Product {
    private int productId;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private int stock;
    private String imageUrl;

    public Product(int productId, String name, String brand,
                   String description, BigDecimal price,
                   BigDecimal discount, int stock, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.stock = stock;
        this.imageUrl = imageUrl;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
