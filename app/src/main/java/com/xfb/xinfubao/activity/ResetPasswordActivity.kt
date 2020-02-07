package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.helper.RxHelper
import com.careagle.sdk.utils.CommentUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.utils.setInVisible
import com.xfb.xinfubao.utils.setVisible
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_reset_password.*
import java.util.concurrent.TimeUnit

class ResetPasswordActivity : DefaultActivity() {
    var delayTime = 60
    var disposable: Disposable? = null
    //通过什么方式重置密码
    private var isMobile = true

    companion object {
        fun startActivityForWay(context: Context, isMobile: Boolean) {
            val intent = Intent(context, ResetPasswordActivity::class.java)
            intent.putExtra("isMobile", isMobile)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_reset_password
    }

    override fun initView(savedInstanceState: Bundle?) {
        isMobile = intent.getBooleanExtra("isMobile", true)
        gpMobile.setInVisible(isMobile)
        etInputEmail.setInVisible(!isMobile)
        tvChangeMobile.setOnClickListener {
            tvChangeMobile.setVisible(false)
            isMobile = !isMobile
            gpMobile.setInVisible(isMobile)
            etInputEmail.setInVisible(!isMobile)
        }

        ivFinish.setOnClickListener { finish() }

        tvOK.setOnClickListener {
            resetPassword()
        }
        tvCheckCode.setOnClickListener {
            requestCheckCode()
        }
    }

    //获取验证码
    private fun requestCheckCode() {
        if (disposable != null) return
        val map = hashMapOf<String, String>()
        var account = ""
        if (isMobile) {
            account = etInputMobile.testValue()
            if (!CommentUtils.isMobile(account)) {
                showMessage("请输入正确的手机号")
                return
            }
        } else {
            account = etInputEmail.testValue()
            if (!CommentUtils.isEmail(account)) {
                showMessage("请输入正确的邮箱地址")
                return
            }
        }
        map["account"] = account
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getCheckCode(map)) {
            disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe({ time ->
                    tvCheckCode.text = "${delayTime - time}s"
                    if (time >= delayTime) {
                        toRestCheckCode()
                    }
                }, {
                    toRestCheckCode()
                })
        }
    }

    private fun toRestCheckCode() {
        disposable?.dispose()
        disposable = null
        tvCheckCode.text = "重新获取"
    }

    private fun resetPassword() {
        val map = hashMapOf<String, String>()
        var account = ""
        if (isMobile) {
            account = etInputMobile.testValue()
            if (!CommentUtils.isMobile(account)) {
                showMessage("请输入正确的手机号")
                return
            }
        } else {
            account = etInputEmail.testValue()
            if (!CommentUtils.isEmail(account)) {
                showMessage("请输入正确的邮箱地址")
                return
            }
        }
        val strCheckCode = etCheckCode.testValue()
        val strPassword = etInputNewPassword.testValue()
        val strPasswordAgain = etInputNewPasswordAgain.testValue()
        map["account"] = account
        if (TextUtils.isEmpty(strCheckCode)) {
            showMessage("请输入验证码")
            return
        }
        map["icode"] = strCheckCode
        if (TextUtils.isEmpty(strPassword)) {
            showMessage("请输入密码")
            return
        }
        if (strPassword != strPasswordAgain) {
            showMessage("两次输入的密码不一致")
            return
        }
        map["pwd"] = strPassword
        map["restType"] = if (isMobile) "0" else "1"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).resetPwd(map)) {
            showMessage("操作成功")
            finish()
        }
    }
}
