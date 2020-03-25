package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R

/** 去置换 */
class ToExchangeActivity : DefaultActivity() {

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, ToExchangeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_to_exchange
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

}
