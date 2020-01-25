package com.xfb.xinfubao.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class ItemBalanceModel implements MultiItemEntity {
    private String id;
    private String name;
    private String createDate;
    private double amount;
    private int state;
    private String orderNum;
    private String stateDepict;
    private int itemType = 0;
    private int type = 0;

    public boolean isCashIn() {
        return type == 0;
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
