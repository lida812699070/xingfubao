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
            NatKuangActiveActivity.toActivity(this, 0)
        }
        tvKuangZhuHint.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 0)
        }
        tvKuangZhuOk.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 0)
        }

        tvKuangJi.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 1)
        }
        tvKuangJiHint.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 1)
        }
        tvKuangJiOk.setOnClickListener {
            NatKuangActiveActivity.toActivity(this, 1)
        }

    }

}
