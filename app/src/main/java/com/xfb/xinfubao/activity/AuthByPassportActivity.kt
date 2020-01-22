package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_auth_by_passport.*

/** 护照认证 */
class AuthByPassportActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_auth_by_passport
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivFinish.setOnClickListener { finish() }
        tvOK.setOnClickListener { Intent(this, AuthResultActivity::class.java) }
    }

}
