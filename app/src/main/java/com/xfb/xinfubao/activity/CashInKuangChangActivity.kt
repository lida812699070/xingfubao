package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R

/** 转入矿场 */
class CashInKuangChangActivity : DefaultActivity() {

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, CashInKuangChangActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_in_kuang_chang
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

}
