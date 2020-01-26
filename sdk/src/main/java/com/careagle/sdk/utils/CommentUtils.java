package com.careagle.sdk.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.careagle.sdk.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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


    /**
     * 复制文件
     *
     * @param oldFile
     * @param newFile
     * @param isImage
     */
    public static void copyFile(String oldFile, String newFile, boolean isImage) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(oldFile);
            fos = new FileOutputStream(newFile);
            byte[] buf = new byte[1024];
            int by = 0;
            while ((by = fis.read(buf)) != -1) {
                fos.write(buf, 0, by);
            }
            if (isImage) {
                sendImageChangeBroadcast(newFile);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新系统图库
     *
     * @param imgPath
     */
    public static void sendImageChangeBroadcast(String imgPath) {
        if (TextUtils.isEmpty(imgPath)) return;
        File file = new File(imgPath);
        if (file.exists() && file.isFile()) {
            MediaScannerConnection.scanFile(Config.getContext(), new String[]{file.getAbsolutePath()}, null, null);
        }
    }

    /**
     * 获取手机相册路径
     *
     * @return
     */
    public static String getCameraPath() {
        String cameraPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/";
        File file = new File(cameraPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return cameraPath;
    }

}
