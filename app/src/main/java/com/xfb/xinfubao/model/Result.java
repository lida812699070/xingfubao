package com.xfb.xinfubao.model;

public class Result<T> {

    /**
     * code : 0
     * data : null
     * count : 0
     * msg :
     */

    private int code;
    private T data;
    private int count;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
