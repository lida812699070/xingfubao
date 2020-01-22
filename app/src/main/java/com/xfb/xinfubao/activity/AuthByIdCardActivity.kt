package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_auth_by_id_card.*

/** 身份证认证 */
class AuthByIdCardActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_auth_by_id_card
    }

    override fun initView(savedInstanceState: Bundle?) {
        ivFinish.setOnClickListener { finish() }
        tvOK.setOnClickListener { startActivity(Intent(this, AuthResultActivity::class.java)) }
    }
}
