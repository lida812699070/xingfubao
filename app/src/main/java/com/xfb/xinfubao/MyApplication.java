package com.xfb.xinfubao;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.careagle.sdk.Config;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xfb.xinfubao.constant.Constant;
import com.xfb.xinfubao.wxapi.WXUtils;

public class MyApplication extends Application {
    private static MyApplication context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Config.setApp(this);
        Config.setBaseUrl(Constant.SERVER_API);
        CrashReport.initCrashReport(getApplicationContext(), "e613e24431", false);
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
// 将该app注册到微信
        msgApi.registerApp(WXUtils.WX_APP_ID);
    }

    public static MyApplication getInstance() {
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
