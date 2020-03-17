package com.xfb.xinfubao.callback;

public interface DownloadCallBack {

    void inProgress(int progress);
    void finish(String msg);
}
