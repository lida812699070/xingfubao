package com.xfb.xinfubao.model;

public class NatActiveModel {
    private int id;
    private String activityName;
    private String activityIcon;
    private boolean buttonState;
    private boolean inputState;
    //    1=时间限制 2=人数限制
    private int activityWay;
    private int joinNumber;
    private ThawTimeModel thawTime;

    public String getActivityIcon() {
        return activityIcon;
    }

    public void setActivityIcon(String activityIcon) {
        this.activityIcon = activityIcon;
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

    public boolean isButtonState() {
        return buttonState;
    }

    public void setButtonState(boolean buttonState) {
        this.buttonState = buttonState;
    }

    public boolean isInputState() {
        return inputState;
    }

    public void setInputState(boolean inputState) {
        this.inputState = inputState;
    }

    public int getActivityWay() {
        return activityWay;
    }

    public void setActivityWay(int activityWay) {
        this.activityWay = activityWay;
    }

    public int getJoinNumber() {
        return joinNumber;
    }

    public void setJoinNumber(int joinNumber) {
        this.joinNumber = joinNumber;
    }

    public ThawTimeModel getThawTime() {
        return thawTime;
    }

    public void setThawTime(ThawTimeModel thawTime) {
        this.thawTime = thawTime;
    }
}
