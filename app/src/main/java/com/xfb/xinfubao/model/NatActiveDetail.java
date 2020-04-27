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
    private Object natInputNum;
    private String activityWay;
    private String activityRules;
    private String premiumRateDesc;
    private List<ConfigDates> configDates;

    public List<ConfigDates> getConfigDates() {
        return configDates;
    }

    public void setConfigDates(List<ConfigDates> configDates) {
        this.configDates = configDates;
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

    public Object getNatInputNum() {
        return natInputNum;
    }

    public void setNatInputNum(Object natInputNum) {
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
