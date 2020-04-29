package com.xfb.xinfubao.activity

import android.os.Bundle

import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_natactive.*

/**
 * NAT活动
 */
class NATActiveActivity : DefaultActivity() {

    override fun getLayoutId(): Int {
        return R.layout.activity_natactive
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        clKuangZhu.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 1)
        }

        clKuangJi.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 2)
        }
    }

}
