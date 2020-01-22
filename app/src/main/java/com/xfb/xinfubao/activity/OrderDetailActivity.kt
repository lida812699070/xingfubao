package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_order_detail.*

/** 订单详情 */
class OrderDetailActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_order_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        productList.bindData(arrayListOf("1"))
    }
}
