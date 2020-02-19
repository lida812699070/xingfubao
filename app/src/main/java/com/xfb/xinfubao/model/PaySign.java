package com.xfb.xinfubao.model;

import com.tencent.mm.opensdk.modelpay.PayReq;

public class PaySign {

    /**
     * app_id : wxa70a398c8a7adbe1
     * mch_id : 1556257351
     * nonce_str : ulHPsA3q8ZeGfuaR
     * prepay_id : wx19100432936645496ff8bf8d1219603200
     * sign : A1B1C856ABF960EB29400D8757178A1C
     * attach : 556
     * timeStamp : 1582077872584
     */

    private String app_id;
    private String mch_id;
    private String nonce_str;
    private String prepay_id;
    private String sign;
    private String attach;
    private String timeStamp;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public PayReq convert() {
        PayReq payReq = new PayReq();
        payReq.appId = app_id;
        payReq.partnerId = mch_id;
        payReq.prepayId = prepay_id;
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr = nonce_str;
        payReq.timeStamp = timeStamp;
        payReq.sign = sign;
        return payReq;
    }
}
