package com.xfb.xinfubao.model;

public class GiveTypeVo {


    /**
     * id : 0
     * productName :
     * pid : 0
     * assetsId : 0
     * assetsName :
     * maxFavorable : 0
     * state : 0
     * createTime :
     * unit :
     * assetsAmount : 0
     * code :
     * favorable : 0
     * assetsRatio : 0
     * status : true
     */

    private int id;
    private String productName;
    private int pid;
    private int assetsId;
    private String assetsName;
    private double maxFavorable;
    private int state;
    private String createTime;
    private String unit;
    private double assetsAmount;
    private String code;
    private boolean status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(int assetsId) {
        this.assetsId = assetsId;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public double getMaxFavorable() {
        return maxFavorable;
    }

    public void setMaxFavorable(double maxFavorable) {
        this.maxFavorable = maxFavorable;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getAssetsAmount() {
        return assetsAmount;
    }

    public void setAssetsAmount(double assetsAmount) {
        this.assetsAmount = assetsAmount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
