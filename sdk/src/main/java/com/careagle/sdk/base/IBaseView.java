package com.careagle.sdk.base;


/**
 * Created by lida on 2018/4/3.
 */

public interface IBaseView {

    /**
     * Toast 消息
     *
     * @param msg
     */
    void showMessage(String msg);

    /**
     * 展示Progress
     *
     * @param msg
     */
    void showProgress(String msg);

    /**
     * 隐藏Progress
     */
    void hideProgress();

}
