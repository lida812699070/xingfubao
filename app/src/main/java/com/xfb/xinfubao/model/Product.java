package com.xfb.xinfubao.model;

import java.math.BigDecimal;

public class Product {

    /**
     * productId : 0
     * imgUrl :
     * productName :
     * productPrice : 0
     */

    private int productId;
    private String imgUrl;
    private String productName;
    private double productPrice;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
