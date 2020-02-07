package com.xfb.xinfubao.model;

public class MyTeamModel {

    /**
     * tel :
     * grade :
     * teamNumber : 0
     * teamPerformance : 0
     */

    private String tel;
    private String grade;
    private int teamNumber;
    private double teamPerformance;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(int teamNumber) {
        this.teamNumber = teamNumber;
    }

    public double getTeamPerformance() {
        return teamPerformance;
    }

    public void setTeamPerformance(double teamPerformance) {
        this.teamPerformance = teamPerformance;
    }
}
