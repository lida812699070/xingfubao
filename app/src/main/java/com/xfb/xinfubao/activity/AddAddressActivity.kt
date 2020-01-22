package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_add_address.*

/** 新增收货地址 */
class AddAddressActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_add_address
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
    }
}
