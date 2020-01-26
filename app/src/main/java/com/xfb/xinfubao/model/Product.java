package com.xfb.xinfubao.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Objects;

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
    private int quantity;

    public int getQuantity() {
        int count = 0;
        if (num != 0) {
            count = num;
        } else if (productNum != 0) {
            count = productNum;
        } else {
            count = quantity;
        }
        return count;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getNum() {
        int count = 0;
        if (num != 0) {
            count = num;
        } else if (productNum != 0) {
            count = productNum;
        } else {
            count = quantity;
        }
        return count;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getProductNum() {
        int count = 0;
        if (num != 0) {
            count = num;
        } else if (productNum != 0) {
            count = productNum;
        } else {
            count = quantity;
        }
        return count;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return getProductId() == product.getProductId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProductId());
    }
}
