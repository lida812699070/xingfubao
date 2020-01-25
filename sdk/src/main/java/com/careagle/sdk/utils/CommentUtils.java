package com.careagle.sdk.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.careagle.sdk.Config;

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

    //复制
    public static void copy(String data) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) Config.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）,其他的还有
        // newHtmlText、
        // newIntent、
        // newUri、
        // newRawUri
        ClipData clipData = ClipData.newPlainText(null, data);

        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);
    }


}
