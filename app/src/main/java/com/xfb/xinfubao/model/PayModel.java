package com.xfb.xinfubao.model;

import android.text.TextUtils;

public class PayModel {
    //支付状态
    private String orderStr;
    //支付类型 value="支付方式( 微信：1,  支付宝：2)"
    private int payType;
    private int payWay;
    //订单号
    private String orderNO;
    private String orderNo;

    private String payInfo;
    //支付成功后回调页面
    private String alipaySuccessUrl;

    public PayModel(int payType, String orderNo, String orderStr, String alipaySuccessUrl) {
        this.orderStr = orderStr;
        this.orderNo = orderNo;
        this.payType = payType;
        this.payWay = payType;
        this.orderNO = orderNo;
        this.alipaySuccessUrl = alipaySuccessUrl;
    }

    public int getPayWay() {
        return payWay == 0 ? payType : payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
        this.payType = payWay;
    }

    public String getOrderNo() {
        return TextUtils.isEmpty(orderNo) ? orderNO : orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        this.orderNO = orderNo;
    }

    public String getAlipaySuccessUrl() {
        return alipaySuccessUrl;
    }

    public void setAlipaySuccessUrl(String alipaySuccessUrl) {
        this.alipaySuccessUrl = alipaySuccessUrl;
    }

    public String getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(String payInfo) {
        this.payInfo = payInfo;
    }

    public String getOrderNO() {
        return TextUtils.isEmpty(orderNO) ? orderNo : orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
        this.orderNo = orderNO;
    }

    public String getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }

    public int getPayType() {
        return payType == 0 ? payWay : payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
        this.payWay = payType;
    }
}
