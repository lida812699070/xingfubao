package com.xfb.xinfubao.model;

import java.io.Serializable;

/** 支付方式 */
public class PayMethod implements Serializable {

    private long payMethodId;
    private String payMethodIdentity;
    private String payMethodName;
    private double balance;
    private boolean isThirdParty;

    public long getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(long payMethodId) {
        this.payMethodId = payMethodId;
    }

    public String getPayMethodIdentity() {
        return payMethodIdentity;
    }

    public void setPayMethodIdentity(String payMethodIdentity) {
        this.payMethodIdentity = payMethodIdentity;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isThirdParty() {
        return isThirdParty;
    }

    public void setThirdParty(boolean thirdParty) {
        isThirdParty = thirdParty;
    }
}
