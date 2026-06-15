package com.management.Accounts.entity;

import org.springframework.data.annotation.Id;

public class itemModel {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    @Id
    private String id;

    public String getAddProductName() {
        return addProductName;
    }

    public void setAddProductName(String addProductName) {
        this.addProductName = addProductName;
    }

    public int getAddQuantity() {
        return addQuantity;
    }

    public void setAddQuantity(int addQuantity) {
        this.addQuantity = addQuantity;
    }

    public int getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(int addPrice) {
        this.addPrice = addPrice;
    }
    private String orderId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

        private String productId;
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    private String addProductName;
    private int addQuantity;

    private int addPrice;
}
    