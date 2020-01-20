package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_confirm_order.*

//确认订单
class ConfirmOrderActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_confirm_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick({ finish() })
    }

}
