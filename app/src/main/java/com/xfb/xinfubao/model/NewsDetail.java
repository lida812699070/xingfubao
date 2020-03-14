package com.xfb.xinfubao.model;

public class NewsDetail {

    /**
     * id : 0
     * source :
     * title :
     * content :
     * createtime :
     * iocurl :
     * noticeid : 0
     * authorid : 0
     * state : 0
     */

    private int id;
    private String source;
    private String title;
    private String content;
    private String createtime;
    private String iocurl;
    private int noticeid;
    private int authorid;
    private int state;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getIocurl() {
        return iocurl;
    }

    public void setIocurl(String iocurl) {
        this.iocurl = iocurl;
    }

    public int getNoticeid() {
        return noticeid;
    }

    public void setNoticeid(int noticeid) {
        this.noticeid = noticeid;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
