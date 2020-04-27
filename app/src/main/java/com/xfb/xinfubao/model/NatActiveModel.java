package com.xfb.xinfubao.model;

public class NatActiveModel {
    private int id;
    private String activityName;
    private boolean buttonState;
    private boolean inputState;
    private int activityWay;
    private int joinNumber;
    private String thawTime;

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

    public String getThawTime() {
        return thawTime;
    }

    public void setThawTime(String thawTime) {
        this.thawTime = thawTime;
    }
}
