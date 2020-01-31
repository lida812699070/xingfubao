package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setInVisible
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_auth_result.*

/** 认证结果 */
class AuthResultActivity : DefaultActivity() {
    //0未认证，1，审核中，2认证成功，
    var authState: Int = 0

    companion object {
        fun toActivity(context: Context, authState: Int) {
            val intent = Intent(context, AuthResultActivity::class.java)
            intent.putExtra("authState", authState)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_auth_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        authState = intent.getIntExtra("authState", 0)

        showProgress("请稍候")
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            ConfigUtils.saveUserInfo(it.data)
            authState = it.data.authState
            if (authState == 1) {
                tvAuthInfo.text = getString(R.string.str_auth_wait)
            } else {
                tvTitle.text = "认证成功"
                ivAuthState.setImageResource(R.mipmap.icon_auth_success)
                tvAuthInfo.setInVisible(false)
                clAuthSuccess.setVisible(true)
                queryAuthInfo()
            }
        }
        ivFinish.setOnClickListener { finish() }
        tvOK.setOnClickListener { finish() }

    }

    private fun queryAuthInfo() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).querycertificationdetails(map)) {
            tvName.text = it.data.name
            tvCardNumber.text = it.data.cardid
        }
    }
}
