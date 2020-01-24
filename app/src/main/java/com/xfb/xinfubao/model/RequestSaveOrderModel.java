package com.xfb.xinfubao.model;

import java.util.List;

/** 生成订单参数 */
public class RequestSaveOrderModel {
    private String receiveId;
    private String remark;
    private String freight;
    private String totalAmount;
    private String discount;
    private String payAmount;
    private String userId;
    private List<Product> productDtoList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public List<Product> getProductDtoList() {
        return productDtoList;
    }

    public void setProductDtoList(List<Product> productDtoList) {
        this.productDtoList = productDtoList;
    }
}
