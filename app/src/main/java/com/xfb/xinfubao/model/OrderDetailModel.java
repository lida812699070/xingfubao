package com.xfb.xinfubao.model;

import java.util.List;

public class OrderDetailModel {
    private long orderId;
    private ReceiveVo receive;
    private String orderNumber;
    private double freight;
    private List<Product> productBase;
    private String deliveryNumber;
    private double totalAmount;
    private long deliveryState;
    private long orderState;
    private String createDate;
    private String deliveryStateDesc;
    private String ostateDescribe;
    private OrderDetailPayInfo paymentInfo;
    private double discountAmount;
    private String orderDesc;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public ReceiveVo getReceive() {
        return receive;
    }

    public void setReceive(ReceiveVo receive) {
        this.receive = receive;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public List<Product> getProductBase() {
        return productBase;
    }

    public void setProductBase(List<Product> productBase) {
        this.productBase = productBase;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public long getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(long deliveryState) {
        this.deliveryState = deliveryState;
    }

    public long getOrderState() {
        return orderState;
    }

    public void setOrderState(long orderState) {
        this.orderState = orderState;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDeliveryStateDesc() {
        return deliveryStateDesc;
    }

    public void setDeliveryStateDesc(String deliveryStateDesc) {
        this.deliveryStateDesc = deliveryStateDesc;
    }

    public String getOstateDescribe() {
        return ostateDescribe;
    }

    public void setOstateDescribe(String ostateDescribe) {
        this.ostateDescribe = ostateDescribe;
    }

    public OrderDetailPayInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(OrderDetailPayInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }
}
