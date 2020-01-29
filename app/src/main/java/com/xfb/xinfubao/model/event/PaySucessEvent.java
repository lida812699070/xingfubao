package com.xfb.xinfubao.model.event;

public class PaySucessEvent {

    public int code;

    public String msg;
    public String orderNo;

    public PaySucessEvent(int code,String orderNo) {
        this.code = code;
        this.orderNo = orderNo;
    }

    public PaySucessEvent() {
    }
}
