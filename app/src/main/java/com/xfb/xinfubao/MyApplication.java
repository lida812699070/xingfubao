package com.xfb.xinfubao;

import android.app.Application;

import com.careagle.sdk.Config;
import com.tencent.bugly.crashreport.CrashReport;

public class MyApplication extends Application {
    private static MyApplication context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Config.setApp(this);
        Config.setBaseUrl("http://xfbapi.tynet.vip:8088");
        CrashReport.initCrashReport(getApplicationContext(), "e613e24431", false);
    }

    public static MyApplication getInstance() {
        return context;
    }
}
