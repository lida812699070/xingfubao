package com.xfb.xinfubao.callback;

import com.careagle.sdk.utils.MyToast;
import com.careagle.sdk.utils.NetworkConnectionUtils;
import com.xfb.xinfubao.MyApplication;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class RequestCallBack<T> implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        if (NetworkConnectionUtils.isNetworkAvailable(MyApplication.getInstance())) {
            MyToast.toast("请求失败");
        } else {
            MyToast.toast("请求失败,请检查网络");
        }
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
