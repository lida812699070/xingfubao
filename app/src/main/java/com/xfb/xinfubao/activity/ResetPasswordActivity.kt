package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.utils.setInVisible
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : DefaultActivity() {

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
    }


}
