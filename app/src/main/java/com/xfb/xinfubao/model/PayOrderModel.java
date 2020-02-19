package com.xfb.xinfubao.model;

public class PayOrderModel {

    /**
     * id : 512
     * orderStr : alipay_sdk=alipay-sdk-java-4.9.5.ALL&app_id=2021001115682384&biz_content=%7B%22out_trade_no%22%3A%22%E6%9D%8F%E7%A6%8F%E5%AE%9D%E5%95%86%E5%93%81%E8%B4%AD%E4%B9%B0%22%2C%22subject%22%3A%22%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%222.0%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2Fxfb.cxtx.info%3A8082%2Fapi%2Falipay%2Fnotify&sign=OrqTZtIvNWi8JVYg5nig%2FxT2xmQKzegSZQyVcBUjbxvfPFANCrbsAH6qSbidfx77AfKSt6zVwWigWd8dro2xn2xS0uVEu4xw05fcM2HL%2Bj3bWyQrFec2ekAjzAR8L5ee2EVciKkSglGl6NDPuZqpvX0tC9RupcApaH4y3ALIvr1q%2Fpn6ftCKBFUmk%2BMGUMfJfn8lwtPtp5TWUlVWniPYrJHyN6pinfh3AucW%2FQlnkS7iSIk0GbZvQQLgJ0ZqtQ6xOvOMU3K%2FT%2FWjO6B0frl6EAf8DNCVKy%2FWez%2BL3qfdXrtaxErWrrugSwLoxy5QQWofxyQvnLfGxczF%2F1Dafju3Bw%3D%3D&sign_type=RSA2&timestamp=2020-02-18+23%3A51%3A58&version=1.0
     */

    private String id;
    private String orderStr;
    private PaySign paySign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderStr() {
        return orderStr;
    }

    public void setOrderStr(String orderStr) {
        this.orderStr = orderStr;
    }

    public PaySign getPaySign() {
        return paySign;
    }

    public void setPaySign(PaySign paySign) {
        this.paySign = paySign;
    }
}
