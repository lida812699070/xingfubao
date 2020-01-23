package com.xfb.xinfubao.model;

/**
 * 收货地址
 */
public class ReceiveVo {

    private long receiveId;
    //收货人
    private String consignee;
    private String phone;
    private String address;
    private boolean isDefault;
    //门牌
    private String doorplate;

    public long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(long receiveId) {
        this.receiveId = receiveId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getDoorplate() {
        return doorplate;
    }

    public void setDoorplate(String doorplate) {
        this.doorplate = doorplate;
    }
}
