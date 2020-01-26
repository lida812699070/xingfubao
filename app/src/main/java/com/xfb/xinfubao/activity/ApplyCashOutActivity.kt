package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_apply_cash_out.*

/** 申请转出 */
class ApplyCashOutActivity : DefaultActivity() {
    //提现申请 0  转出申请 1
    var state = 0

    companion object {
        fun toActivity(context: Context, state: Int) {
            val intent = Intent(context, ApplyCashOutActivity::class.java)
            intent.putExtra("state", state)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_cash_out
    }

    override fun initView(savedInstanceState: Bundle?) {
        state = intent.getIntExtra("state", 0)
        initListener()
        resetView()
    }

    private fun resetView() {

        if (state == 0) {
            tvTitle.text = "申请提现"
            tvApplyRecord.text = "申请记录"
            tvSubTitle.text = "银杏果余额(个/元)"
            tvTotalMoney.text = ""

        }
    }

    private fun initListener() {
        tvOK.setOnClickListener {
            startActivity(Intent(this, ApplyCashOutResultActivity::class.java))
        }
        ivFinish.setOnClickListener { finish() }

        tvApplyRecord.setOnClickListener {
            startActivity(Intent(this, CashOutRecordActivity::class.java))
        }
    }

}
