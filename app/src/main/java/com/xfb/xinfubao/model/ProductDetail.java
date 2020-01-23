package com.xfb.xinfubao.model;

import java.util.List;

public class ProductDetail {


    /**
     * productId : 0
     * productImg : [{"imgUrl":"","isMain":true}]
     * productName :
     * inventory : 0
     * productMainImg :
     * productDetails :
     * productPrice : 0
     * productState : true
     * isReal : true
     * productStateDesc :
     * isRealDesc :
     */

    private long productId;
    private String productName;
    private int inventory;
    private String productMainImg;
    private String productDetails;
    private double productPrice;
    private boolean productState;
    //是否实物商品
    private boolean isReal;
    private String productStateDesc;
    private String isRealDesc;
    private List<ProductImg> productImg;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getInventory() {
        return inventory;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public String getProductMainImg() {
        return productMainImg;
    }

    public void setProductMainImg(String productMainImg) {
        this.productMainImg = productMainImg;
    }

    public String getProductDetails() {
        return productDetails;
    }

    public void setProductDetails(String productDetails) {
        this.productDetails = productDetails;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public boolean isProductState() {
        return productState;
    }

    public void setProductState(boolean productState) {
        this.productState = productState;
    }

    public boolean isIsReal() {
        return isReal;
    }

    public void setIsReal(boolean isReal) {
        this.isReal = isReal;
    }

    public String getProductStateDesc() {
        return productStateDesc;
    }

    public void setProductStateDesc(String productStateDesc) {
        this.productStateDesc = productStateDesc;
    }

    public String getIsRealDesc() {
        return isRealDesc;
    }

    public void setIsRealDesc(String isRealDesc) {
        this.isRealDesc = isRealDesc;
    }

    public List<ProductImg> getProductImg() {
        return productImg;
    }

    public void setProductImg(List<ProductImg> productImg) {
        this.productImg = productImg;
    }

    public Product toProduct() {
        Product product = new Product();
        product.setProductId(productId);
        product.setImgUrl(productMainImg);
        product.setProductPrice(productPrice);
        product.setProductName(productName);
        return product;
    }
}
