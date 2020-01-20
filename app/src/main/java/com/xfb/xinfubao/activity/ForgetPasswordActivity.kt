package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_forget_password.*

class ForgetPasswordActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_forget_password
    }

    override fun initView(savedInstanceState: Bundle?) {

        ivFinish.setOnClickListener {
            finish()
        }

        tvRestPasswordMobile.setOnClickListener {
            ResetPasswordActivity.startActivityForWay(this, true)
        }

        tvRestPasswordEmail.setOnClickListener {
            ResetPasswordActivity.startActivityForWay(this, false)
        }
    }

}
