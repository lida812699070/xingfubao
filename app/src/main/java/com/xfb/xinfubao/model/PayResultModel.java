package com.xfb.xinfubao.model;

import java.io.Serializable;

public class PayResultModel implements Serializable {

    private PayMethod payMethod;
    private String orderNo;
    private double payAmout;

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(PayMethod payMethod) {
        this.payMethod = payMethod;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getPayAmout() {
        return payAmout;
    }

    public void setPayAmout(double payAmout) {
        this.payAmout = payAmout;
    }
}
