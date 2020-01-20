
package com.careagle.sdk.utils;

import android.util.Log;


/**
 * Created by lida on 2017/11/27.
 */

public class JLog {
    public static void e(String msg) {
        Log.e("jlog", msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void d(String msg) {
        Log.e("http", msg);
    }

    public static void i(String msg) {
        Log.i("http", msg);
    }
}
