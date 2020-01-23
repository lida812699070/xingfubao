package com.xfb.xinfubao.model;

import java.util.List;

/**
 * 订单信息
 */
public class OrderInfo {
    //默认收货地址
    private ReceiveVo receiveVo;
    //商品信息
    private List<Product> productBaseVos;
    //商品总数
    private int productTotalNum;
    //商品总金额
    private double productTotalAmount;
    //运费
    private double freight;
    //商品小计
    private double orderTotalAmount;
    //会员折扣
    private double discount;
    //实付金额
    private double payAmount;

    public ReceiveVo getReceiveVo() {
        return receiveVo;
    }

    public void setReceiveVo(ReceiveVo receiveVo) {
        this.receiveVo = receiveVo;
    }

    public List<Product> getProductBaseVos() {
        return productBaseVos;
    }

    public void setProductBaseVos(List<Product> productBaseVos) {
        this.productBaseVos = productBaseVos;
    }

    public int getProductTotalNum() {
        return productTotalNum;
    }

    public void setProductTotalNum(int productTotalNum) {
        this.productTotalNum = productTotalNum;
    }

    public double getProductTotalAmount() {
        return productTotalAmount;
    }

    public void setProductTotalAmount(double productTotalAmount) {
        this.productTotalAmount = productTotalAmount;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public double getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(double orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(double payAmount) {
        this.payAmount = payAmount;
    }
}
