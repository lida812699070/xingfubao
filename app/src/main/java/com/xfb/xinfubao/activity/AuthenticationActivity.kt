package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_authentication.*

/** 实名认证 */
class AuthenticationActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_authentication
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }

        tvAuthByIdCard.setOnClickListener {
            startActivity(Intent(this, AuthByIdCardActivity::class.java))
        }

        tvAuthByPassport.setOnClickListener {
            startActivity(Intent(this, AuthByPassportActivity::class.java))
        }


    }

}
