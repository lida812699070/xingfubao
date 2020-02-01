package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_safe_center.*

/** 安全中心 */
class SafeCenterActivity : DefaultActivity() {

    var userInfo: UserInfo? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_safe_center
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        tvResetLoginPassword.setOnClickListener {
            ChangePasswordActivity.toActivity(
                ChangePasswordEnum.CHANGE_LOGIN_PASSWORD,
                this,
                tvResetLoginPassword.text.toString()
            )
        }
        tvResetPayPassword.setOnClickListener {
            ChangePasswordActivity.toActivity(
                ChangePasswordEnum.SET_PAY_PASSWORD,
                this,
                tvResetPayPassword.text.toString()
            )
        }
        tvResetMobile.setOnClickListener {
            ChangePasswordActivity.toActivity(
                ChangePasswordEnum.CHANGE_MOBILE,
                this,
                tvResetMobile.text.toString()
            )
        }
        showProgress("请稍候")
        loadData()
    }

    override fun onRestart() {
        super.onRestart()
        loadData()
    }

    private fun loadData() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            bindData(it.data)
        }
    }

    private fun bindData(data: UserInfo) {
        userInfo = data
        tvResetPayPassword.text = if (data.isPayPwd) "修改支付密码" else "设置支付密码"
        if (TextUtils.isEmpty(data.tel)) {
            tvResetMobile.text = "绑定手机号"
        } else {
            tvMobile.text = data.tel
        }
        if (data.authState == 2) {
            tvAuthenticationState.text = "已实名"
            tvAuthenticationState.setTextColor(resources.getColor(R.color.color_light_org))
            tvAuthentication.setOnClickListener {
                AuthResultActivity.toActivity(this, ConfigUtils.getUserInfo()!!.authState)
            }
        } else if (data.authState == 1) {
            tvAuthenticationState.text = "审核中"
            tvAuthenticationState.setTextColor(resources.getColor(R.color.color_light_org))
            tvAuthentication.setOnClickListener {
                AuthResultActivity.toActivity(this, ConfigUtils.getUserInfo()!!.authState)
            }
        } else if (data.authState == 0) {
            tvAuthenticationState.text = "未实名"
            tvAuthentication.setOnClickListener {
                startActivity(Intent(this, AuthenticationActivity::class.java))
            }
        }
    }

}
