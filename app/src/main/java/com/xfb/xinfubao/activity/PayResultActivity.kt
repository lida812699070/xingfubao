package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_pay_result.*

/** 付款详情 */
class PayResultActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_pay_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        tvOrderDetail.setOnClickListener {
            startActivity(Intent(this, OrderDetailActivity::class.java))
        }
    }

}
