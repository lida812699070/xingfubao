package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.event.EventAuth
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_safe_center.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** 安全中心 */
class SafeCenterActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_safe_center
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
        myToolbar.setClick { finish() }
        tvResetLoginPassword.setOnClickListener {
            ChangePasswordActivity.toActivity(ChangePasswordEnum.CHANGE_LOGIN_PASSWORD, this)
        }
        tvResetPayPassword.setOnClickListener {
            ChangePasswordActivity.toActivity(ChangePasswordEnum.SET_PAY_PASSWORD, this)
        }
        tvResetMobile.setOnClickListener {
            ChangePasswordActivity.toActivity(ChangePasswordEnum.CHANGE_MOBILE, this)
        }
        tvAuthentication.setOnClickListener {
            AuthResultActivity.toActivity(this, 2)
        }
        loadData()
    }


    @Subscribe
    fun auth(auth: EventAuth) {
        loadData()
    }

    private fun loadData() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        //TODO 获取用户信息
//        request(RetrofitCreateHelper.createApi(BaseApi::class.java).queryUserInfo(map)) {
//
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}
