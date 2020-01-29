package com.xfb.xinfubao;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
