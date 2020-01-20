package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setInVisible
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : DefaultActivity() {

    //是否是手机登录
    private var isMobileLogin = true

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView(savedInstanceState: Bundle?) {
        changeLoginState()

        tvLoginMobile.setOnClickListener {
            isMobileLogin = true
            changeLoginState()
        }

        tvLoginEmail.setOnClickListener {
            isMobileLogin = false
            changeLoginState()
        }

        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        tvForgetPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        tvLogin.setOnClickListener {
            toLogin()
        }
    }

    private fun toLogin() {
        val map = hashMapOf<String, String>()
        var account = ""
        if (isMobileLogin) {
            account = edLoginMobile.text.toString()
        } else {
            account = edLoginEmail.text.toString()
        }
        val password = edLoginPassword.text.toString()
        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            showMessage("请输入账号和密码")
            return
        }
        map["loginWay"] = if (isMobileLogin) "0" else "1"
        map["account"] = account
        map["password"] = password
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).toLogin(map)) {
            ConfigUtils.saveUserInfo(it.data)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun changeLoginState() {
        tvLoginMobile.isSelected = isMobileLogin
        edLoginMobile.setInVisible(isMobileLogin)
        tvLoginEmail.isSelected = !isMobileLogin
        edLoginEmail.setInVisible(!isMobileLogin)
    }
}
