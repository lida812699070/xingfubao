package com.xfb.xinfubao.model;

import java.util.List;

/**
 * 支付请求参数
 */
public class RequestPay {
    private String userId;
    private List<String> orderNO;
    private String payWay;
    private String payAmount;
    private String payPwd;
    private Integer favorableId;

    public Integer getFavorableId() {
        return favorableId;
    }

    public void setFavorableId(Integer favorableId) {
        this.favorableId = favorableId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(List<String> orderNO) {
        this.orderNO = orderNO;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }
}
