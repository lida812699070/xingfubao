package com.xfb.xinfubao;

import android.app.Application;

import com.careagle.sdk.Config;

public class MyApplication extends Application {
    private static MyApplication context = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Config.setApp(this);
        Config.setBaseUrl("http://xfbapi.tynet.vip:8088");
    }

    public static MyApplication getInstance() {
        return context;
    }
}
