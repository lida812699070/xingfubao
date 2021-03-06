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
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.model.event.ShuHuiEvent
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_shu_hui_zhi_ya.*
import org.greenrobot.eventbus.EventBus

/** 赎回质押 */
class ShuHuiZhiYaActivity : DefaultActivity() {
    var itemBalanceModel: ItemBalanceModel? = null
    var userInfo: UserInfo? = null

    companion object {
        fun toActivity(context: Context, itemBalanceModel: ItemBalanceModel) {
            val intent = Intent(context, ShuHuiZhiYaActivity::class.java)
            intent.putExtra("itemBalanceModel", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_shu_hui_zhi_ya
    }

    override fun initView(savedInstanceState: Bundle?) {
        itemBalanceModel = intent.getSerializableExtra("itemBalanceModel") as ItemBalanceModel?
        ivFinish.setOnClickListener { finish() }
        //财务记录
        tvDiYaRecord.setOnClickListener {
            CashOutRecordActivity.toActivity(this, 2)
        }
        tvCashInNat.setOnClickListener {
            CashInNatActivity.toActivity(this)
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            ConfigUtils.saveUserInfo(it.data)
            userInfo = it.data
            tvBalance.text = PriceChangeUtils.getDoubleKb(it.data.userAssets.natFlowNum)
        }

        tvOk.setOnClickListener {
            if (userInfo == null) {
                return@setOnClickListener
            }
            if (itemBalanceModel!!.redeemMoney > userInfo!!.userAssets.natFlowNum) {
                showMessage("请转入NAT")
                return@setOnClickListener
            }
            checkPayPassword {
                shuhui()
            }
        }

        val params = hashMapOf<String, String>()
        params["userId"] = "${ConfigUtils.userId()}"
        params["ordernum"] = "${itemBalanceModel?.orderNum}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).redeemDeduct(params)) {
            tvCutNatCount.text = PriceChangeUtils.getDoubleKb(it.data.redeemDeductNum)
        }
    }

    private fun shuhui() {
        val payPassword = etPayPassword.text.toString()
        if (TextUtils.isEmpty(payPassword)) {
            showMessage("请输入支付密码")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["ordernum"] = "${itemBalanceModel?.orderNum}"
        map["orderId"] = "${itemBalanceModel?.id}"
        map["payPassword"] = payPassword
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).redeem(map)) {
            EventBus.getDefault().post(ShuHuiEvent())
            finish()
        }
    }

}
