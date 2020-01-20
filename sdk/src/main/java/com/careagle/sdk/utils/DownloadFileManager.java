package com.careagle.sdk.utils;

import android.os.Environment;
import android.text.TextUtils;

import com.careagle.sdk.Config;
import com.careagle.sdk.callback.OnDownloadListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadFileManager {

    public void download(final String url, final String saveFile, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        new OkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onDownloadFailed(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(TextUtils.isEmpty(saveFile) ? createFile().getAbsolutePath() : saveFile);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    listener.onDownloadSuccess(file.getAbsolutePath());
                } catch (Exception e) {
                    listener.onDownloadFailed(e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private File createFile() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File updateDir = new File(Config.getFileRootPath());
            File updateFile = new File(updateDir + "/" + System.currentTimeMillis() + ".apk");
            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return updateFile;
        }
        return null;
    }

}

