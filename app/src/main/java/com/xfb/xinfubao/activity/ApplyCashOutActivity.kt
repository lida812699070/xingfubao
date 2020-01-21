package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_apply_cash_out.*
/** 申请转出 */
class ApplyCashOutActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_apply_cash_out
    }

    override fun initView(savedInstanceState: Bundle?) {
        tvOK.setOnClickListener {
            startActivity(Intent(this, ApplyCashOutResultActivity::class.java))
        }
        ivFinish.setOnClickListener { finish() }

        tvApplyRecord.setOnClickListener {
            startActivity(Intent(this, CashOutRecordActivity::class.java))
        }
    }

}
