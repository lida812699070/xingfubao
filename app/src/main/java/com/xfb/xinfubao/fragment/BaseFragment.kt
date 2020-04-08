package com.xfb.xinfubao.fragment

import com.careagle.sdk.Config
import com.careagle.sdk.base.fragment.BaseCompatFragment
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.MyToast
import com.careagle.sdk.utils.NetworkConnectionUtils
import com.xfb.xinfubao.activity.ChangePasswordActivity
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import com.xfb.xinfubao.utils.ConfigUtils
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
        onerror: (msg:String) -> Unit,
        callBack: (com.xfb.xinfubao.model.Result<T>) -> Unit
    ) {
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { hideProgress() }
            .subscribe({
                if (it.code == 0) {
                    callBack(it)
                } else {
                    onerror(it.msg)
                }
            }, {
                onError(it)
                onerror("请求失败")
            }, {
            })
    }

    fun checkPayPassword(method: () -> Unit) {
        if (true == ConfigUtils.mUserInfo?.isPayPwd) {
            method()
        } else {
            showProgress("请稍候")
            val params = hashMapOf<String, String>()
            params["userId"] = "${ConfigUtils.userId()}"
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(params)) {
                ConfigUtils.saveUserInfo(it.data)
                if (it.data.isPayPwd) {
                    method()
                } else {
                    showMessage("请先设置支付密码")
                    ChangePasswordActivity.toActivity(
                        ChangePasswordEnum.SET_PAY_PASSWORD,
                        activity,
                        "设置支付密码"
                    )
                }
            }
        }
    }

}