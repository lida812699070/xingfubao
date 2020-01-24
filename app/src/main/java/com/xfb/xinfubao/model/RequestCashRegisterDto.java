package com.xfb.xinfubao.model;

import java.util.List;

/**
 * 收银台请求数据模型
 */
public class RequestCashRegisterDto {
    private String userId;
    private List<String> orderNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(List<String> orderNumber) {
        this.orderNumber = orderNumber;
    }
}
