package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.careagle.sdk.utils.PriceChangeUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.PayResultModel
import kotlinx.android.synthetic.main.activity_pay_result.*

/** 付款详情 */
class PayResultActivity : DefaultActivity() {

    companion object {
        fun toActivity(context: Context, payResultModel: PayResultModel) {
            val intent = Intent(context, PayResultActivity::class.java)
            intent.putExtra("data", payResultModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_pay_result
    }

    override fun initView(savedInstanceState: Bundle?) {
        val payResultModel = intent.getSerializableExtra("data") as PayResultModel

        tvOrderMoney.text =
            getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(payResultModel.payAmout))
        tvPayWay.text = payResultModel.payMethod.payMethodName
        myToolbar.setClick { finish() }
        tvOrderDetail.setOnClickListener {
            startActivity(Intent(this, MyOrderActivity::class.java))
            finish()
        }
    }

}
