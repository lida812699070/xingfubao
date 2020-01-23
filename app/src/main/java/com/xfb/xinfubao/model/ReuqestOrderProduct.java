package com.xfb.xinfubao.model;

import java.io.Serializable;

public class ReuqestOrderProduct implements Serializable {
    private String productId;
    private String num;
    private String cartId;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }
}
