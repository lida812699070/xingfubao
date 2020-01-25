package com.xfb.xinfubao.model;

import java.util.List;

public class MyOrderModel {
    private long orderId;
    private String orderNumber;
    private List<Product> productBase;
    private double totalAmount;
    private int orderState;
    private String createDate;
    private String ostateDescribe;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<Product> getProductBase() {
        return productBase;
    }

    public void setProductBase(List<Product> productBase) {
        this.productBase = productBase;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getOstateDescribe() {
        return ostateDescribe;
    }

    public void setOstateDescribe(String ostateDescribe) {
        this.ostateDescribe = ostateDescribe;
    }
}
