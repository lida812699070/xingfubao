package com.xfb.xinfubao.payment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.xfb.xinfubao.model.PayModel;
import com.xfb.xinfubao.model.event.PaySucessEvent;
import com.xfb.xinfubao.wxapi.WXUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * 支付宝支付
 */
public class PayUtils {

    private Context context;
    //微信
    private final int WECHAT = 1;
    //支付宝
    private final int ALIPAY = 2;
    //支付宝
    private final int BANkPAY = 3;
    //支付宝APPID
//    private final String ALIPAY_APPID = "2018122762706096";
    //支付信息
    private PayModel payModel = null;


    public PayUtils(Context context) {
        this.context = context;
    }

    /**
     * 支付宝支付
     *
     * @param payModel
     * @param isNativePay
     */
    public void toPay(PayModel payModel, boolean isNativePay) {
        this.payModel = payModel;
        aliPay(payModel.getOrderStr(), isNativePay);
    }

    /**
     * 支付宝支付
     *
     * @param paySign     订单信息
     * @param isNativePay 支付渠道 H5 native
     */
    private void aliPay(final String paySign, final boolean isNativePay) {
//        if (!ALIPAY_APPID.equals(getAppID(paySign)) && !paySign.contains(ALIPAY_APPID)) {
//            ToastUtil.showToast(context, R.string.label_illegal_payment);
//            return;
//        }
        Runnable payRunnable = () -> {
            PayTask alipay = new PayTask((Activity) context);
            String result = alipay.pay(paySign, true);
            Message msg = new Message();
            msg.what = isNativePay ? 1 : 2;
            msg.obj = result;
            mHandler.sendMessage(msg);
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 支付宝支付回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            String s = msg.obj.toString();
            PayResult payResult = new PayResult(msg.obj.toString());
            //对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            PaySucessEvent event = new PaySucessEvent();
            if (!TextUtils.isEmpty(resultStatus) && resultStatus.contains("9000")) {
                //支付成功
                event.code = 1;
                // msg.what == 1,NativePay  否 H5
                if (msg.what == 1) {
                    event.orderNo = payModel.getOrderNO();
                }
            } else {
                event.code = 0;
                event.msg = payResult.getMemo();
            }
            EventBus.getDefault().post(event);
        }
    };

    /**
     * 微信支付
     */
    public void wechatPayment(PayReq payReq) {
        WXUtils instance = WXUtils.getInstance();
        instance.getIWXAPI();
        instance.wechatPayment(context, payReq, req -> {
            PaySucessEvent event = new PaySucessEvent();
            if (req.errCode == BaseResp.ErrCode.ERR_OK) {
                event.code = 1;
                event.msg = "支付成功";
            } else if (req.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
                event.code = req.errCode;
                event.msg = "取消支付";
            } else if (req.errCode == BaseResp.ErrCode.ERR_COMM) {
                event.code = req.errCode;
                event.msg = req.errStr;
            }
            //H5自营商品支付
            EventBus.getDefault().post(event);
        });
    }
}
