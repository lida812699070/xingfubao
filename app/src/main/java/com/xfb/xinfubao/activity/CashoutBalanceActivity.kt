package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ItemBalanceModel
import kotlinx.android.synthetic.main.activity_cashout_balance.*

/** 转为余额 */
class CashoutBalanceActivity : DefaultActivity() {

    private var itemBalanceModel: ItemBalanceModel? = null

    companion object {
        fun toActivity(context: Context, itemBalanceModel: ItemBalanceModel?) {
            val intent = Intent(context, CashoutBalanceActivity::class.java)
            intent.putExtra("itemBalanceModel", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cashout_balance
    }

    override fun initView(savedInstanceState: Bundle?) {
        itemBalanceModel = intent.getSerializableExtra("itemBalanceModel") as ItemBalanceModel?
        tvSubTitles.setOnClickListener {
            CashOutRecordActivity.toActivity(this, 3)
        }

    }
}
