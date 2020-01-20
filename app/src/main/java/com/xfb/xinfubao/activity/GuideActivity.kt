package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_guide.*

class GuideActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_guide
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}
