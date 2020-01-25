package com.xfb.xinfubao.fragment

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.helper.RxHelper
import com.careagle.sdk.utils.CommentUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.LoginActivity
import com.xfb.xinfubao.activity.WebviewActivity
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.utils.changePasswordState
import com.xfb.xinfubao.utils.downLine
import com.xfb.xinfubao.utils.setInVisible
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_register_mobile.*
import java.util.concurrent.TimeUnit

class RegisterFragment : BaseFragment() {
    //是否是手机注册
    var isMobileRegister = true
    var delayTime = 60
    var disposable: Disposable? = null

    companion object {
        fun newInstance(isMobileRegister: Boolean = true): RegisterFragment {
            val fragmentLogin = RegisterFragment()
            val bundle = Bundle()
            bundle.putBoolean("isMobileRegister", isMobileRegister)
            fragmentLogin.arguments = bundle
            return fragmentLogin
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_register_mobile
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
        arguments?.getBoolean("isMobileRegister", true)?.let { isMobileRegister = it }
        gpEmailRegister.setInVisible(!isMobileRegister)
        gpMobileRegister.setInVisible(isMobileRegister)
        tvMobileRegisterProtocols.downLine()
        tvToLogin.downLine()
        initListener()
        tvMobileRegisterProtocols.setOnClickListener {
            WebviewActivity.newInstanceUrl(activity!!, Constant.USER_PROTCT, "用户协议")
        }
    }

    private fun initListener() {
        //隐私协议
        tvMobileRegisterProtocols.setOnClickListener {

        }
        //去登陆
        tvToLogin.setOnClickListener {
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
        //可见密码
        ivMobileInputPsdEye.setOnClickListener {
            ivMobileInputPsdEye.isSelected = !ivMobileInputPsdEye.isSelected
            etMobileInputPsd.changePasswordState(ivMobileInputPsdEye.isSelected)
        }
        //可见密码
        ivMobileInputPsdEyeAgain.setOnClickListener {
            ivMobileInputPsdEyeAgain.isSelected = !ivMobileInputPsdEyeAgain.isSelected
            etMobileInputPsdAgain.changePasswordState(ivMobileInputPsdEyeAgain.isSelected)
        }
        //获取验证码
        tvMobileGetCheckCode.setOnClickListener {
            if (disposable != null) {
                return@setOnClickListener
            }
            requestCheckCode()
        }
        //同意隐私协议
        ivMobileLoginCheckBox.setOnClickListener {
            ivMobileLoginCheckBox.isSelected = !ivMobileLoginCheckBox.isSelected
        }
        //注册
        tvMobileRegister.setOnClickListener {
            toRegister()
        }
    }

    private fun toRegister() {
        if (!ivMobileLoginCheckBox.isSelected) {
            showMessage("请同意用户隐私协议")
            return
        }
        val account =
            if (isMobileRegister) etMobileInputMobile.text.toString() else etMobileInputMobile.text.toString()
        if (isMobileRegister) {
            if (!CommentUtils.isMobile(account)) {
                showMessage("请输入正确的手机号")
                return
            }
        } else {
            if (!CommentUtils.isEmail(account)) {
                showMessage("请输入正确的邮箱")
                return
            }
        }
        val strPassword = etMobileInputPsd.text.toString()
        val strPasswordAgain = etMobileInputPsdAgain.text.toString()
        val strCheckCode = etMobileInputCheckCode.text.toString()
        val strInviteCode = etMobileInputInviteCode.text.toString()

        if (TextUtils.isEmpty(strPassword) || TextUtils.isEmpty(strPasswordAgain) || TextUtils.isEmpty(
                strCheckCode
            ) || TextUtils.isEmpty(strInviteCode)
        ) {
            showMessage("请填写完整信息")
            return
        }
        if (strPassword != strPasswordAgain) {
            showMessage("两次输入的密码不一致")
            return
        }
        if (strPassword.length < 8 || strPassword.length > 20) {
            showMessage("密码为8-20位字符")
            return
        }
        showProgress("请稍候")
        val map = hashMapOf<String, String>()
        map["registerWay"] = if (isMobileRegister) "0" else "1"
        map["account"] = account
        map["password"] = strPassword
        map["verificationCode"] = strCheckCode
        map["invitationCode"] = strInviteCode
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).toRegister(map)) {
            showMessage("注册成功")
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }

    //获取验证码
    private fun requestCheckCode() {
        if (disposable != null) {
            return
        }
        val map = hashMapOf<String, String>()
        val account =
            if (isMobileRegister) etMobileInputMobile.text.toString() else etMobileInputMobile.text.toString()
        if (isMobileRegister) {
            if (!CommentUtils.isMobile(account)) {
                showMessage("请输入正确的手机号")
                return
            }
        } else {
            if (!CommentUtils.isEmail(account)) {
                showMessage("请输入正确的邮箱")
                return
            }
        }
        showProgress("请稍候")
        map["account"] = account
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getCheckCode(map)) {
            disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe({ time ->
                    tvMobileGetCheckCode.text = "${delayTime - time}s"
                    if (time >= delayTime) {
                        disposable?.dispose()
                        disposable = null
                        toRestCheckCode()
                    }
                }, {
                    toRestCheckCode()
                })
        }
    }

    private fun toRestCheckCode() {
        tvMobileGetCheckCode.text = "重新获取"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable?.dispose()
        disposable = null
    }
}