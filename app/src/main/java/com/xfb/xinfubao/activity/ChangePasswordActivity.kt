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
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_change_password.*
import java.util.concurrent.TimeUnit

/** 修改密码 */
class ChangePasswordActivity : DefaultActivity() {
    private var changePasswordEnum = ChangePasswordEnum.CHANGE_LOGIN_PASSWORD
    var delayTime = 60
    var disposable: Disposable? = null

    companion object {
        fun toActivity(enum: ChangePasswordEnum?, context: Context) {
            val intent = Intent(context, ChangePasswordActivity::class.java)
            intent.putExtra("enum", enum)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        changePasswordEnum = intent.getSerializableExtra("enum") as ChangePasswordEnum
        when (changePasswordEnum) {
            ChangePasswordEnum.CHANGE_LOGIN_PASSWORD -> {

            }
            ChangePasswordEnum.SET_PAY_PASSWORD -> {
                etPasswordTop.hint = "请输入支付密码"
                etPasswordBottom.hint = "请再次输入密码"
                myToolbar.setTitle("设置支付密码")
            }
            ChangePasswordEnum.CHANGE_PAY_PASSWORD -> {
                etPasswordTop.hint = "请输入新的支付密码"
                etPasswordBottom.hint = "请再次输入密码"
                myToolbar.setTitle("修改支付密码")
            }
            ChangePasswordEnum.CHANGE_MOBILE -> {
                etMobile.hint = "请输入新的手机号码"
                etCheckCode.hint = "请输入验证码"
                etPasswordTop.hint = "请输入支付密码"
                etPasswordBottom.setVisible(false)
                myToolbar.setTitle("修改绑定手机")
            }
        }

        tvOK.setOnClickListener {
            resetPassword()
        }

        tvGetCheckCode.setOnClickListener {
            requestCheckCode()
        }
    }

    //获取验证码
    private fun requestCheckCode() {
        if (disposable != null) return
        val map = hashMapOf<String, String>()
        val account = etMobile.text.toString()
        if (!CommentUtils.isMobile(account)) {
            showMessage("请输入正确的手机号")
            return
        }
        map["account"] = account
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getCheckCode(map)) {
            disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe({ time ->
                    tvGetCheckCode.text = "${delayTime - time}s"
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
        tvGetCheckCode.text = "重新获取"
    }

    private fun resetPassword() {
        val map = hashMapOf<String, String>()
        val strMobile = etMobile.text.toString()
        val strCheckCode = etCheckCode.text.toString()
        val strPassword = etPasswordTop.text.toString()
        val strPasswordAgain = etPasswordBottom.text.toString()
        if (TextUtils.isEmpty(strMobile)) {
            showMessage("请输入手机号")
            return
        }
        map["phone"] = strMobile
        if (TextUtils.isEmpty(strCheckCode)) {
            showMessage("请输入验证码")
            return
        }
        map["icode"] = strCheckCode
        if (TextUtils.isEmpty(strPassword)) {
            showMessage("请输入密码")
            return
        }
        if (strPassword != strPasswordAgain && ChangePasswordEnum.CHANGE_MOBILE != changePasswordEnum) {
            showMessage("两次输入的密码不一致")
            return
        }
        map["psw"] = strPassword
        map["userid"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        when (changePasswordEnum) {
            //修改 重置登录密码
            ChangePasswordEnum.CHANGE_PAY_PASSWORD,
            ChangePasswordEnum.SET_PAY_PASSWORD -> {
                request(RetrofitCreateHelper.createApi(BaseApi::class.java).updpaypsw(map)) {
                    showMessage("操作成功")
                    finish()
                }
            }
            //修改登录密码
            ChangePasswordEnum.CHANGE_LOGIN_PASSWORD -> {
                val map = hashMapOf<String, String>()
                request(RetrofitCreateHelper.createApi(BaseApi::class.java).updpaypsw(map)) {
                    showMessage("操作成功")
                    finish()
                }
            }
            //修改手机号
            ChangePasswordEnum.CHANGE_MOBILE -> {
                val map = hashMapOf<String, String>()
                request(RetrofitCreateHelper.createApi(BaseApi::class.java).updpaypsw(map)) {
                    showMessage("操作成功")
                    finish()
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
        disposable = null
    }

}
