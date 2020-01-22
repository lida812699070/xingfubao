package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import kotlinx.android.synthetic.main.activity_safe_center.*

/** 安全中心 */
class SafeCenterActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_safe_center
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        tvResetLoginPassword.setOnClickListener {
            ChangePasswordActivity.toActivity(ChangePasswordEnum.CHANGE_LOGIN_PASSWORD, this)
        }
        tvResetPayPassword.setOnClickListener {
            ChangePasswordActivity.toActivity(ChangePasswordEnum.CHANGE_PAY_PASSWORD, this)
        }
        tvResetMobile.setOnClickListener {
            ChangePasswordActivity.toActivity(ChangePasswordEnum.CHANGE_MOBILE, this)
        }
    }

}
