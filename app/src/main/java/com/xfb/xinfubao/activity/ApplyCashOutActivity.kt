package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.event.EventTransfer
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_apply_cash_out.*
import org.greenrobot.eventbus.EventBus

/** 申请转出 */
class ApplyCashOutActivity : DefaultActivity() {
    //银杏果提现申请 0  银杏宝转出申请 1
    var state = 0
    var mAmout = 0.0
    var recordId: String? = null

    companion object {
        fun toActivity(
            context: Context,
            state: Int,
            recordId: String? = null,
            amount: Double? = 0.0
        ) {
            val intent = Intent(context, ApplyCashOutActivity::class.java)
            intent.putExtra("state", state)
            intent.putExtra("recordId", recordId)
            intent.putExtra("amount", amount)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_cash_out
    }

    override fun initView(savedInstanceState: Bundle?) {
        state = intent.getIntExtra("state", 0)
        recordId = intent.getStringExtra("recordId")
        initListener()
        if (state == 1) {
            tvAll.setVisible(false)
            val params = hashMapOf<String, String>()
            params["userId"] = "${ConfigUtils.userId()}"
            params["recordId"] = "$recordId"
            showProgress("请稍候")
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).findTurnOutInfo(params)) {
                mAmout = intent.getDoubleExtra("amount", 0.0)
                tvTotalMoney.text = PriceChangeUtils.getNumKb(mAmout)
                etApplyCashOutMoney.setText(PriceChangeUtils.getDoubleKb(mAmout))
                etApplyCashOutMoney.isFocusable = false
                if (it.data.isTransferredOut) {
                    tvDateYear.text = "0"
                    tvDateMonth.text = "0"
                    tvDateDay.text = "0"
                    tvDateHour.text = "0"
                    tvDateMin.text = "0"
                } else {
                    tvDateYear.text = "${it.data.thawTime.year}"
                    tvDateMonth.text = "${it.data.thawTime.month}"
                    tvDateDay.text = "${it.data.thawTime.day}"
                    tvDateHour.text = "${it.data.thawTime.hours}"
                    tvDateMin.text = "${it.data.thawTime.minutes}"
                }
            }
        } else {
            val map = hashMapOf<String, String>()
            map["userId"] = "${ConfigUtils.userId()}"
            showProgress("请稍候")
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
                mAmout = it.data.userAssets.ginkgoFruitNum
                tvTitle.text = "申请提现"
                tvApplyRecord.text = "申请记录"
                tvSubTitle.text = "银杏果余额(个/元)"
                clDate.setVisible(false)
                tvTimeLessOutText.setVisible(false)
                viewTop.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_org_light_white_ga))
                tvAll.setTextColor(resources.getColor(R.color.color_light_org))
                tvOK.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_org_light_radius_8))
                etApplyCashOutMoney.hint = "申请提现金额"
                tvTotalMoney.text = PriceChangeUtils.getNumKb(mAmout)
            }
        }
    }

    private fun initListener() {
        tvOK.setOnClickListener {
            okApply()
        }
        ivFinish.setOnClickListener { finish() }
        tvApplyRecord.setOnClickListener {
            CashOutRecordActivity.toActivity(this, state)
        }
        tvAll.setOnClickListener {
            etApplyCashOutMoney.setText("$mAmout")
        }
    }

    private fun okApply() {
        val strBankNo = etBankCardNo.text.toString()
        val strBankName = etBankName.text.toString()
        val strBankUserName = etBankCardUserName.text.toString()
        val strAmount = etApplyCashOutMoney.text.toString()
        val strPayPassword = etPayPassword.text.toString()
        if (TextUtils.isEmpty(strBankNo)) {
            showMessage("请输入银行卡账号")
            return
        }
        if (TextUtils.isEmpty(strBankName)) {
            showMessage("请输入开户行名称")
            return
        }
        if (TextUtils.isEmpty(strBankUserName)) {
            showMessage("请输入开户姓名")
            return
        }
        if (TextUtils.isEmpty(strAmount)) {
            showMessage("请输入金额")
            return
        }
        if (TextUtils.isEmpty(strPayPassword)) {
            showMessage("请输入支付密码")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["amount"] = strAmount
        map["bankCards"] = strBankNo
        map["bank"] = strBankName
        map["accountName"] = strBankUserName
        map["payPwd"] = strPayPassword
        //银杏果提现申请 0  银杏宝转出申请 1
        if (state == 0) {
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).ginkgoFruitTurnOut(map)) {
                applySuccess()
            }
        } else {
            //        记录ID；银杏宝转出时为银杏宝记录ID，银杏果提现时不传
            map["recordId"] = "$recordId"
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).ginkgoTreasureTurnOut(map)) {
                applySuccess()
            }
        }
    }

    private fun applySuccess() {
        showMessage("操作成功")
        EventBus.getDefault().post(EventTransfer())
        finish()
    }

}
