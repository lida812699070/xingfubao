package com.xfb.xinfubao.activity

import com.careagle.sdk.Config
import com.careagle.sdk.base.activity.BaseActivity
import com.careagle.sdk.utils.MyToast
import com.careagle.sdk.utils.NetworkConnectionUtils
import com.xfb.xinfubao.model.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class DefaultActivity : BaseActivity() {

    open fun <T> request(
        observable: Observable<Result<T>>,
        callBack: (com.xfb.xinfubao.model.Result<T>) -> Unit
    ) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideProgress() }
            .subscribe({
                if (it.code == 0) {
                    callBack(it)
                } else {
                    MyToast.toast(it.msg)
                }
            }, {
                onError(it)
            }, {

            })
    }

    fun onError(e: Throwable) {
        if (NetworkConnectionUtils.isNetworkAvailable(Config.getContext())) {
            MyToast.toast("请求失败")
        } else {
            MyToast.toast("请求失败,请检查网络")
        }
        e.printStackTrace()
    }
}
