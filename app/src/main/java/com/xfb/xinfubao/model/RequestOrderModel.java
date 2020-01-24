package com.xfb.xinfubao.model;

import java.util.List;

/** 确认订单参数 */
public class RequestOrderModel {
    private String userId;
    private List<Product> productDtoList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Product> getProductDtoList() {
        return productDtoList;
    }

    public void setProductDtoList(List<Product> productDtoList) {
        this.productDtoList = productDtoList;
    }
}
