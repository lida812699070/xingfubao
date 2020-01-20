package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.xfb.xinfubao.R
import com.xfb.xinfubao.utils.ConfigUtils

class WelcomeActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_welcome
    }

    val mHandler = Handler()
    override fun initView(savedInsMtanceState: Bundle?) {
        mHandler.postDelayed({
            if (ConfigUtils.getUserInfo() != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, GuideActivity::class.java))
            }
            finish()
        }, 3000)

    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
