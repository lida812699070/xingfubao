package com.careagle.sdk.callback;

public interface OnDownloadListener {
    void onDownloadSuccess(String path);

    void onDownloading(int progress);

    void onDownloadFailed(String msg);
}