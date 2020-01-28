package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.event.EventTransfer
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_transfer.*
import org.greenrobot.eventbus.EventBus

/** 转账 */
class TransferActivity : DefaultActivity() {
    var type = TRANSFER_TYPE_YXY
    var mAmount = 0.0
    var showDiYaDialog: AlertDialog? = null

    companion object {
        const val TRANSFER_TYPE_YXY = 0
        const val TRANSFER_TYPE_JF = 1
        fun toActivity(context: Context, type: Int) {
            val intent = Intent(context, TransferActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_transfer
    }

    override fun initView(savedInstanceState: Bundle?) {
        type = intent.getIntExtra("type", 0)
        tvUserName.text = if (type == TRANSFER_TYPE_YXY) "银杏叶" else "积分"
        showProgress("请稍候")
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            ConfigUtils.saveUserInfo(it.data)
            if (type == TRANSFER_TYPE_YXY)
                mAmount = it.data.userAssets.ginkgoLeafNum
            else
                mAmount = it.data.userAssets.integralNum
            tvLessBalance.text = getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(mAmount))
            tvOk.setOnClickListener {
                onCashChange()
            }
        }
    }

    private fun onCashChange() {
        val userId = "${ConfigUtils.userId()}"
        val transferType = if (type == TRANSFER_TYPE_YXY) 2 else 3
        val payeePhone = etCashInAccount.text.toString()
        val amount = etCashOutNumber.text.toString()
        val etRemark = etRemark.text.toString()
        if (TextUtils.isEmpty(payeePhone)) {
            showMessage("请输入收款账号")
            return
        }
        if (TextUtils.isEmpty(amount)) {
            showMessage("请输入转账数量")
            return
        }
        if (mAmount < amount.toDouble()) {
            showMessage("余额不足")
            return
        }
        showDiYaDialog = DialogUtils.showDiYaDialog(this, 1) {
            showDiYaDialog?.dismiss()
            val map = hashMapOf<String, String>()
            map["userId"] = userId
            map["transferType"] = "$transferType"
            map["payeePhone"] = payeePhone
            map["amount"] = amount
            map["remark"] = etRemark
            map["password"] = it
            showProgress("请稍候")
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).transfer(map)) {
                showMessage("转账成功")
                EventBus.getDefault().post(EventTransfer())
            }
        }
    }

}
