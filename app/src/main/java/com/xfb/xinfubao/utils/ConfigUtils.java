package com.xfb.xinfubao.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.careagle.sdk.utils.SPUtils;
import com.google.gson.Gson;
import com.xfb.xinfubao.MyApplication;
import com.xfb.xinfubao.model.UserInfo;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * 配置
 */
public class ConfigUtils {

    public static UserInfo mUserInfo = null;

    //用户登录信息
    public static void saveUserInfo(UserInfo userInfo) {
        mUserInfo = userInfo;
        String json = new Gson().toJson(userInfo);
        SPUtils.put("userInfo", json);
    }

    @Nullable
    public static UserInfo getUserInfo() {
        if (mUserInfo != null) {
            return mUserInfo;
        }
        String userInfoJson = (String) SPUtils.get(MyApplication.getInstance(), "userInfo", "");
        if (!TextUtils.isEmpty(userInfoJson)) {
            try {
                UserInfo userInfo = new Gson().fromJson(userInfoJson, UserInfo.class);
                mUserInfo = userInfo;
                return userInfo;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Nullable
    public static String userId() {
        UserInfo userInfo = getUserInfo();
        if (userInfo != null) {
            return userInfo.getUserId() + "";
        } else {
            return null;
        }
    }


    /**
     * 获取剪切板的内容。
     */
    public static String getPrimaryClip(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        if (manager == null) {
            return "";
        }
        ClipData data = manager.getPrimaryClip();
        if (data == null || data.getItemCount() <= 0) {
            return "";
        }
        ClipData.Item item = data.getItemAt(0);
        return item != null && item.getText() != null ? item.getText().toString() : "";
    }

}
