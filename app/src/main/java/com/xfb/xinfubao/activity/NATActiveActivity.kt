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
        tvKuangZhu.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 1)
        }
        tvKuangZhuHint.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 1)
        }
        tvKuangZhuOk.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 1)
        }

        tvKuangJi.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 2)
        }
        tvKuangJiHint.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 2)
        }
        tvKuangJiOk.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 2)
        }

    }

}
