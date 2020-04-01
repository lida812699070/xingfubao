package com.xfb.xinfubao.model;

public class NatUnlockPakeageModel {
    private long id;
    private String name;
    private long lockTime;
    private double unlockRatio;
    private double unlockNum;

    public double getUnlockNum() {
        return unlockNum;
    }

    public void setUnlockNum(double unlockNum) {
        this.unlockNum = unlockNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLockTime() {
        return lockTime;
    }

    public void setLockTime(long lockTime) {
        this.lockTime = lockTime;
    }

    public double getUnlockRatio() {
        return unlockRatio;
    }

    public void setUnlockRatio(double unlockRatio) {
        this.unlockRatio = unlockRatio;
    }
}
