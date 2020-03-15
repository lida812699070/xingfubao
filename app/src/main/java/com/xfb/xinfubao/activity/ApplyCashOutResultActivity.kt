package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.careagle.sdk.utils.PriceChangeUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.utils.downLine
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_apply_cash_out_result.*

/** 申请转出结果 */
class ApplyCashOutResultActivity : DefaultActivity() {
    /** 0 申请提现记录  1 申请转出记录  2 申请抵押记录 3银杏宝转出余额*/
    var state = 0
    var itemBalanceModel: ItemBalanceModel? = null
    private var isWait = false
    override fun getLayoutId(): Int {
        return R.layout.activity_apply_cash_out_result
    }

    companion object {
        fun toActivity(context: Context, state: Int, itemBalanceModel: ItemBalanceModel) {
            val intent = Intent(context, ApplyCashOutResultActivity::class.java)
            intent.putExtra("state", state)
            intent.putExtra("data", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView(savedInstanceState: Bundle?) {
        state = intent.getIntExtra("state", 0)
        itemBalanceModel = intent.getSerializableExtra("data") as ItemBalanceModel?
        isWait = !itemBalanceModel!!.isSuccess
        myToolbar.setClick { finish() }
        tvOk.setOnClickListener { finish() }
        tvConnectServer.setVisible(!isWait)
        tvConnectServerText.setVisible(!isWait)
        tvConnectServer.downLine()
        tvConnectServer.setOnClickListener {
            //TODO 联系客服
        }
        when (state) {
            0 -> {
                myToolbar.setTitle("申请提现")
                ivState.setImageResource(if (isWait) R.mipmap.dengdai_icon else R.mipmap.dui_icon_3)
                if (isWait) {
                    tvHint.setColorText(
                        getString(R.string.apply_cash_out_2_hint, itemBalanceModel?.stateDepict),
                        resources.getColor(R.color.color_light_org),
                        12,
                        12 + itemBalanceModel!!.stateDepict.length
                    )
                } else {
                    tvHint.setColorText(
                        getString(
                            R.string.apply_cash_out_success_hint,
                            itemBalanceModel?.stateDepict
                        ),
                        resources.getColor(R.color.color_light_org),
                        10,
                        10 + itemBalanceModel!!.stateDepict.length
                    )
                }
                tvCashOutDetailText.text = "提现详情"
                tvApplyTime.text = itemBalanceModel?.createDate
                tvApplyTime.setTextColor(resources.getColor(R.color.color_light_org))
                tvCashOutMoney.setTextColor(resources.getColor(R.color.color_light_org))
                tvCashOutMoney.text = PriceChangeUtils.getNumKb(itemBalanceModel!!.amount)
                tvOk.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_light_org_radius_13))
                tvOk.text = itemBalanceModel?.stateDepict
            }
            1 -> {
                myToolbar.setTitle("申请转出")
                ivState.setImageResource(if (isWait) R.mipmap.dengdai_icon else R.mipmap.dui_icon_org_3)
                if (isWait) {
                    tvHint.setColorText(
                        getString(R.string.apply_cash_out_hint, itemBalanceModel?.stateDepict),
                        resources.getColor(R.color.color_org),
                        12,
                        12 + itemBalanceModel!!.stateDepict.length
                    )
                } else {
                    tvHint.setColorText(
                        getString(
                            R.string.apply_cash_out_success_hint,
                            itemBalanceModel?.stateDepict
                        ),
                        resources.getColor(R.color.color_org),
                        10,
                        10 + itemBalanceModel!!.stateDepict.length
                    )
                }
                tvCashOutDetailText.text = "转出详情"
                tvApplyTime.text = itemBalanceModel?.createDate
                tvApplyTime.setTextColor(resources.getColor(R.color.color_org))
                tvCashOutMoney.setTextColor(resources.getColor(R.color.color_org))
                tvCashOutMoney.text = PriceChangeUtils.getNumKb(itemBalanceModel!!.amount)
                tvOk.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_org_radius_13))
                tvOk.text = itemBalanceModel?.stateDepict

            }
            //NAT基金会
            2 -> {
                myToolbar.setTitle("${itemBalanceModel?.typeDepict}详情")
                ivState.setImageResource(if (isWait) R.mipmap.dengdai_icon else R.mipmap.dui_icon_2)
                val strState =
                    "尊敬的用户\n您的${itemBalanceModel?.typeDepict}申请${itemBalanceModel?.stateDepict}\n请留意您的NAT总资产"
                tvHint.setColorText(
                    strState,
                    resources.getColor(R.color.color_theme),
                    12,
                    15
                )
                tvCashOutDetailText.text = "${itemBalanceModel?.typeDepict}详情"
                gpState.setVisible(true)
                tvCashOutStateText.text = "${itemBalanceModel?.typeDepict}状态"
                tvCashOutState.text =
                    "您的${itemBalanceModel?.typeDepict}${itemBalanceModel?.stateDepict}"
                tvApplyTime.text = itemBalanceModel?.createDate
                tvApplyTime.setTextColor(resources.getColor(R.color.color_theme))
                tvCashOutMoney.setTextColor(resources.getColor(R.color.color_theme))
                tvCashOutMoney.text = PriceChangeUtils.getNumKb(itemBalanceModel!!.amount)
                tvOk.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_theme_radius_13))
                tvOk.text = "查看NAT资产"
            }
            3 -> {
                myToolbar.setTitle("转换详情")
                ivState.setImageResource(R.mipmap.icon_org_dui_3)
                val strState = "已成功"
                tvHint.setColorText(
                    getString(R.string.apply_cash_out_success3_hint, strState),
                    resources.getColor(R.color.color_org),
                    12,
                    15
                )
                tvConnectServer.setTextColor(resources.getColor(R.color.color_org))
                tvCashOutDetailText.text = "转换详情"
                tvApplyTime.text = itemBalanceModel?.createDate
                tvApplyTime.setTextColor(resources.getColor(R.color.color_org))
                tvCashOutMoney.setTextColor(resources.getColor(R.color.color_org))
                tvCashOutMoney.text = PriceChangeUtils.getNumKb(itemBalanceModel!!.amount)
                tvOk.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_org_radius_13))
                tvOk.text = "返回银杏宝页面"
            }
        }
    }

}
