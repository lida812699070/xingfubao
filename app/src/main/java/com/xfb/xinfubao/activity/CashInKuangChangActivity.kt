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
import com.xfb.xinfubao.model.event.ShuHuiEvent
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_cash_in_kuang_chang.*
import org.greenrobot.eventbus.EventBus

/** 转入矿场 */
class CashInKuangChangActivity : DefaultActivity() {

    private var itemBalanceModel: ItemBalanceModel? = null

    companion object {
        fun toActivity(context: Context, itemBalanceModel: ItemBalanceModel?) {
            val intent = Intent(context, CashInKuangChangActivity::class.java)
            intent.putExtra("itemBalanceModel", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_in_kuang_chang
    }

    override fun initView(savedInstanceState: Bundle?) {
        itemBalanceModel = intent.getSerializableExtra("itemBalanceModel") as ItemBalanceModel?
        ivFinish.setOnClickListener { finish() }

        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            tvBalance.text = PriceChangeUtils.getNumKb(it.data.userAssets.orePoolMoney)
        }
        tvAll.setOnClickListener {
            etCashInBalance.setText("${itemBalanceModel?.amount}")
        }
        etCashInBalance.setText("${itemBalanceModel?.amount}")

        tvOk.setOnClickListener {
            cashinOk()
        }
    }

    private fun cashinOk() {
        val strCashinBalance = etCashInBalance.text.toString()
        val strPassword = etPayPassword.text.toString()
        if (TextUtils.isEmpty(strCashinBalance)) {
            showMessage("请输入转入数量")
            return
        }
        if (TextUtils.isEmpty(strPassword)) {
            showMessage("请输入支付密码")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["orderNo"] = "${itemBalanceModel?.orderNum}"
        map["yxbabyId"] = "${itemBalanceModel?.id}"
        map["quantityTransferred"] = strCashinBalance
        map["payPassword"] = strPassword
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).transferOrePond(map)) {
            showMessage(it.msg)
            EventBus.getDefault().post(ShuHuiEvent())
            finish()
        }
    }

}
