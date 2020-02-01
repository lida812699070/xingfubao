package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.careagle.sdk.utils.SPUtils
import com.xfb.xinfubao.BuildConfig
import com.xfb.xinfubao.MyApplication
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.event.EventExitApp
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_about.*
import org.greenrobot.eventbus.EventBus

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
        tvExitApp.setOnClickListener {
            DialogUtils.showSelect(this, "是否退出应用？") {
                SPUtils.clear(MyApplication.getInstance())
                ConfigUtils.mUserInfo = null
                EventBus.getDefault().post(EventExitApp())
                finish()
            }
        }
    }
}
