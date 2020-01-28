package com.xfb.xinfubao.model;

public class ExchangeModel {


    /**
     * assetsConfigId : 0
     * assetsId : 0
     * ratio : 0
     * assetsName :
     */

    private int assetsConfigId;
    private int assetsId;
    private double ratio;
    private String assetsName;
    private String assetsIcon;
    private double assetsQuantity;

    public String getAssetsIcon() {
        return assetsIcon;
    }

    public void setAssetsIcon(String assetsIcon) {
        this.assetsIcon = assetsIcon;
    }

    public double getAssetsQuantity() {
        return assetsQuantity;
    }

    public void setAssetsQuantity(double assetsQuantity) {
        this.assetsQuantity = assetsQuantity;
    }

    public int getAssetsConfigId() {
        return assetsConfigId;
    }

    public void setAssetsConfigId(int assetsConfigId) {
        this.assetsConfigId = assetsConfigId;
    }

    public int getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(int assetsId) {
        this.assetsId = assetsId;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }


}
