package com.careagle.sdk.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentUtils {

    /**
     * 是否是手机号
     */
    public static boolean isMobile(String mobile) {
        return !TextUtils.isEmpty(mobile) && mobile.length() == 11 && mobile.startsWith("1");
    }

    /**
     * 是否是邮箱
     */
    public static boolean isEmail(String string) {
        if (string == null)
            return false;
        String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regEx1);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }
}
