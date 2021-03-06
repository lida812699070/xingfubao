package com.xfb.xinfubao.activity

import com.careagle.sdk.Config
import com.careagle.sdk.base.activity.BaseActivity
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.MyToast
import com.careagle.sdk.utils.NetworkConnectionUtils
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.Result
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import com.xfb.xinfubao.utils.ConfigUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class DefaultActivity : BaseActivity() {

    open fun <T> request(
        observable: Observable<Result<T>>,
        callBack: (Result<T>) -> Unit
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


    open fun <T> requestWithError(
        observable: Observable<Result<T>>,
        callBack: (Result<T>) -> Unit,
        onerror: (msg: String?) -> Unit
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
                onerror("")
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
                        this,
                        "设置支付密码"
                    )
                }
            }
        }
    }

}
