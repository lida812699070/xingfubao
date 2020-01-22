package com.xfb.xinfubao.model;

public class HelpBottomModel {

    private String title;
    private String subtitle;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public HelpBottomModel() {
    }

    public HelpBottomModel(String title, String subtitle) {
        this.title = title;
        this.subtitle = subtitle;
    }
}
