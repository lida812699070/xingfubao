package com.xfb.xinfubao.fragment

import com.careagle.sdk.Config
import com.careagle.sdk.base.fragment.BaseCompatFragment
import com.careagle.sdk.utils.MyToast
import com.careagle.sdk.utils.NetworkConnectionUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseFragment : BaseCompatFragment() {

    fun onError(e: Throwable) {
        if (NetworkConnectionUtils.isNetworkAvailable(Config.getContext())) {
            MyToast.toast("请求失败")
        } else {
            MyToast.toast("请求失败,请检查网络")
        }
        e.printStackTrace()
    }

    open fun <T> request(
        observable: Observable<com.xfb.xinfubao.model.Result<T>>,
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

    open fun <T> requestWithError(
        observable: Observable<com.xfb.xinfubao.model.Result<T>>,
        onerror: () -> Unit,
        callBack: (com.xfb.xinfubao.model.Result<T>) -> Unit
    ) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideProgress() }
            .subscribe({
                if (it.code == 0) {
                    callBack(it)
                } else {
                    onerror()
                }
            }, {
                onError(it)
                onerror()
            }, {
            })
    }

}