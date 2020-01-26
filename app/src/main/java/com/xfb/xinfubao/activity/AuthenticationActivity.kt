package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.event.EventAuth
import kotlinx.android.synthetic.main.activity_authentication.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** 实名认证 */
class AuthenticationActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_authentication
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }

        EventBus.getDefault().register(this)
        tvAuthByIdCard.setOnClickListener {
            startActivity(Intent(this, AuthByIdCardActivity::class.java))
        }

        tvAuthByPassport.setOnClickListener {
            startActivity(Intent(this, AuthByPassportActivity::class.java))
        }
    }

    @Subscribe
    fun auth(auth: EventAuth) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
