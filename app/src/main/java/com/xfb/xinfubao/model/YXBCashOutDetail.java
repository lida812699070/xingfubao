package com.xfb.xinfubao.model;

public class YXBCashOutDetail {


    /**
     * amount : 0
     * discountAmount : 0
     * thawTime : {}
     * isTransferredOut : true
     */

    private double amount;
    private double discountAmount;
    private ThawTimeBean thawTime;
    private boolean isTransferredOut;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public ThawTimeBean getThawTime() {
        return thawTime;
    }

    public void setThawTime(ThawTimeBean thawTime) {
        this.thawTime = thawTime;
    }

    public boolean isTransferredOut() {
        return isTransferredOut;
    }

    public void setTransferredOut(boolean transferredOut) {
        isTransferredOut = transferredOut;
    }

}
