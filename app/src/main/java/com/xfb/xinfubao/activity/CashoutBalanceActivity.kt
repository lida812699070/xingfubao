package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.event.EventTransfer
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_cashout_balance.*
import org.greenrobot.eventbus.EventBus

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

        tvYinXingBaoBalance.text = PriceChangeUtils.getNumKb(itemBalanceModel!!.amount)


        setListener()
    }

    private fun setListener() {
        ivFinish.setOnClickListener { finish() }
        //全部
        tvAll.setOnClickListener {
            etCashInBalance.setText("${itemBalanceModel?.amount}")
        }
        //确认转入
        tvOk.setOnClickListener {
            cashOutBalance()
        }
        //转入明细
        tvSubTitles.setOnClickListener {
            CashOutRecordActivity.toActivity(this, 3)
        }
    }

    //转为余额
    private fun cashOutBalance() {
        val balance = etCashInBalance.text.toString()
        val password = etPayPassword.text.toString()
        if (TextUtils.isEmpty(balance)) {
            showMessage("请输入金额")
            return
        }
        if (TextUtils.isEmpty(password)) {
            showMessage("请输入支付密码")
            return
        }
        if (balance.toDouble() > itemBalanceModel!!.amount) {
            showMessage("输入金额不得大于银杏宝金额")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["payPwd"] = password
        map["orderNo"] = "${itemBalanceModel?.orderNum}"
        map["amount"] = balance
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).exchangeBalance(map)) {
            showMessage(it.msg)
            EventBus.getDefault().post(EventTransfer())
            finish()
        }
    }

}
