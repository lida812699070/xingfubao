package com.xfb.xinfubao.model;

import android.text.TextUtils;

import java.io.Serializable;

public class Product implements Serializable {

    /**
     * productId : 0
     * imgUrl :
     * productName :
     * productPrice : 0
     */

    private long productId;
    private String imgUrl;
    private String productName;
    private double productPrice;
    private int productNum;
    private String productMainImg;
    private String cartId;
    private int num;
    private double freight;

    public int getNum() {
        if (num == 0) return productNum;
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getProductNum() {
        if (productNum == 0) return num;
        return productNum;
    }

    public void setProductNum(int productNum) {
        this.productNum = productNum;
    }

    public String getProductMainImg() {
        if (TextUtils.isEmpty(productMainImg)) {
            return imgUrl;
        }
        return productMainImg;
    }

    public void setProductMainImg(String productMainImg) {
        this.productMainImg = productMainImg;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getImgUrl() {
        if (TextUtils.isEmpty(imgUrl)) {
            return productMainImg;
        }
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
