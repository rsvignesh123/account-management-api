package com.management.Accounts.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection  = "productModel")
public class productModel {
    @Id
    private String id;
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    private String tenantId;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    private String productId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }



    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }


    private long price;

}
