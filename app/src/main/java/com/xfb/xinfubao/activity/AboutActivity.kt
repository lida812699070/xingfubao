package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.xfb.xinfubao.BuildConfig
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_about
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setOnClickListener { finish() }
        tvServerProtocol.setOnClickListener {
            WebviewActivity.newInstanceUrl(this, Constant.PRIVITE_SERVER, "服务及隐私条款")
        }
        tvVersion.text = "当前版本号:${BuildConfig.VERSION_NAME}"
    }
}
