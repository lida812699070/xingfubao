package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_auth_result.*

/** 认证结果 */
class AuthResultActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_auth_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivFinish.setOnClickListener { finish() }
        tvOK.setOnClickListener { finish() }
    }
}
