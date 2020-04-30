package com.xfb.xinfubao.model;

import java.util.List;

public class NatActiveDetail {
    private int id;
    private String activityName;
    private String openObjects;
//    保值开启状态：0：关闭 1：开启
    private int hedgeState;
    private String hedgeStateDesc;
    //增值开启状态：0：关闭 1：开启
    private int incrementState;
    private String incrementStateDesc;
    private double premiumRate;
    private double natInputNum;
    private String activityWay;
    private String activityRules;
    private String premiumRateDesc;
    private String confirmButtonText;
//    自动投入状态 0：关闭 1：开启
    private int autoInputState;
    private int inputType;
    //投入方式；1:最少投入数量 2:按每月NAT释放量的比率，设置百分比
    private List<ConfigDates> incrementVos;

    public int getAutoInputState() {
        return autoInputState;
    }

    public void setAutoInputState(int autoInputState) {
        this.autoInputState = autoInputState;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getConfirmButtonText() {
        return confirmButtonText;
    }

    public void setConfirmButtonText(String confirmButtonText) {
        this.confirmButtonText = confirmButtonText;
    }

    public List<ConfigDates> getIncrementVos() {
        return incrementVos;
    }

    public void setIncrementVos(List<ConfigDates> incrementVos) {
        this.incrementVos = incrementVos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getOpenObjects() {
        return openObjects;
    }

    public void setOpenObjects(String openObjects) {
        this.openObjects = openObjects;
    }

    public int getHedgeState() {
        return hedgeState;
    }

    public void setHedgeState(int hedgeState) {
        this.hedgeState = hedgeState;
    }

    public String getHedgeStateDesc() {
        return hedgeStateDesc;
    }

    public void setHedgeStateDesc(String hedgeStateDesc) {
        this.hedgeStateDesc = hedgeStateDesc;
    }

    public int getIncrementState() {
        return incrementState;
    }

    public void setIncrementState(int incrementState) {
        this.incrementState = incrementState;
    }

    public String getIncrementStateDesc() {
        return incrementStateDesc;
    }

    public void setIncrementStateDesc(String incrementStateDesc) {
        this.incrementStateDesc = incrementStateDesc;
    }

    public double getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(double premiumRate) {
        this.premiumRate = premiumRate;
    }

    public double getNatInputNum() {
        return natInputNum;
    }

    public void setNatInputNum(double natInputNum) {
        this.natInputNum = natInputNum;
    }

    public String getActivityWay() {
        return activityWay;
    }

    public void setActivityWay(String activityWay) {
        this.activityWay = activityWay;
    }

    public String getActivityRules() {
        return activityRules;
    }

    public void setActivityRules(String activityRules) {
        this.activityRules = activityRules;
    }

    public String getPremiumRateDesc() {
        return premiumRateDesc;
    }

    public void setPremiumRateDesc(String premiumRateDesc) {
        this.premiumRateDesc = premiumRateDesc;
    }
}
