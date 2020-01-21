package com.xfb.xinfubao.activity

import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.utils.downLine
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_apply_cash_out_result.*

/** 申请转出结果 */
class ApplyCashOutResultActivity : DefaultActivity() {
    private var isWait = false
    override fun getLayoutId(): Int {
        return R.layout.activity_apply_cash_out_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        if (isWait) {
            tvHint.setColorText(
                getString(R.string.apply_cash_out_hint),
                resources.getColor(R.color.color_org),
                12,
                15
            )
        } else {
            tvHint.setColorText(
                getString(R.string.apply_cash_out_success_hint),
                resources.getColor(R.color.color_org),
                10,
                13
            )
        }

        tvConnectServer.setVisible(!isWait)
        tvConnectServerText.setVisible(!isWait)
        tvConnectServer.downLine()
        tvConnectServer.setOnClickListener {
            //联系客服
        }
    }

}
