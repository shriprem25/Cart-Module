package com.capgemini.cartservice.dto;

public class ItemDTO {
    private Long id;
    private String productName;
    private double price;
    private int quantity;

    // Getters and Setters
//    public Long getId() {
//        return id;
//    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
