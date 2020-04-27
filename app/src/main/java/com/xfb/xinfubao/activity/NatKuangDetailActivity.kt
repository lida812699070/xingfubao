package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_nat_kuang_detail.*

/** 抢注矿主 矿基活动 */
class NatKuangDetailActivity : DefaultActivity() {
    //抢注矿主  矿基活动
    var type = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_nat_kuang_detail
    }

    companion object {
        fun toActivity(context: Context, type: Int = 0) {
            val intent = Intent(context, NatKuangDetailActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        type = intent.getIntExtra("type", 0)
        myToolbar.setClick { finish() }
        myToolbar.setTitle(if (type == 0) "抢注矿主" else "矿基活动")
    }


}
