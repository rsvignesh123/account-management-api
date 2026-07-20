package com.management.Accounts.DTO;

import java.math.BigDecimal;

public class OrderItemRequest {

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    private String product;
    private Double quantity;
    private String scale;
    private Double price;

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    private BigDecimal total;


    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
