package com.xfb.xinfubao.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

public class ItemBalanceModel implements MultiItemEntity, Serializable {
    private String id;
    private String name;
    private String createDate;
    private double amount;
    private double redeemMoney;
    //状态；nat抵押记录1.处理中/2.完成3/释放中4已释放，银杏宝转出记录1待审核，2完成
    private int state;
    private String orderNum;
    private String stateDepict;
    //质押手续费
    private String commission;
    private String typeDepict;
    private String productName;
    private int itemType = 0;
    private int type = 0;
    private boolean isSuccess = true;
    private boolean use;
    private boolean exchange;
    private boolean pledge;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getRedeemMoney() {
        return redeemMoney;
    }

    public void setRedeemMoney(double redeemMoney) {
        this.redeemMoney = redeemMoney;
    }

    public String getTypeDepict() {
        return typeDepict;
    }

    public void setTypeDepict(String typeDepict) {
        this.typeDepict = typeDepict;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public boolean isUse() {
        return use;
    }

    public void setUse(boolean use) {
        this.use = use;
    }

    public boolean isExchange() {
        return exchange;
    }

    public void setExchange(boolean exchange) {
        this.exchange = exchange;
    }

    public boolean isPledge() {
        return pledge;
    }

    public void setPledge(boolean pledge) {
        this.pledge = pledge;
    }

    public boolean isSuccess() {
        return state != 1;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isCashIn() {
        return type == 1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getStateDepict() {
        return stateDepict;
    }

    public void setStateDepict(String stateDepict) {
        this.stateDepict = stateDepict;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}
