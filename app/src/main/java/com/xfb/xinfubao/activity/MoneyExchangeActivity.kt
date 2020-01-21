package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_money_exchange.*

/** 资产互兑 */
class MoneyExchangeActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_money_exchange
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        myToolbar.setRightClickStr("帮助") {

        }
    }

}
