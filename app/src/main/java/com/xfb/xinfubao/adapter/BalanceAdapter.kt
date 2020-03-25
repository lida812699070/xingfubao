package com.xfb.xinfubao.adapter

import android.widget.ImageView
import android.widget.TextView
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.setVisible

class BalanceAdapter(data: List<ItemBalanceModel>) :
    BaseMultiItemQuickAdapter<ItemBalanceModel, BaseViewHolder>(data) {
    var balanceEnum = BalanceEnum.YING_XING_GUO
    var natSelector = -1
    var isDetail = false

    companion object {
        //普通
        var ITEM_TYPE_MONEY = 0
        //时间
        var ITEM_TYPE_DATE = 1
        //银杏宝 NAT
        var ITEM_TYPE_WITH_BTN = 2
    }

    init {
        addItemType(ITEM_TYPE_MONEY, R.layout.item_balance_money)
        addItemType(ITEM_TYPE_DATE, R.layout.item_balance_date)
        addItemType(ITEM_TYPE_WITH_BTN, R.layout.item_balance_money_with_btn)
    }

    override fun convert(helper: BaseViewHolder, data: ItemBalanceModel) {
        val tvTitle = helper.getView<TextView>(R.id.tvTitle)
        val ivPoint = helper.getView<ImageView>(R.id.ivPoint)
        val tvMoney = helper.getView<TextView>(R.id.tvMoney)
        val ivNatClubRight = helper.getView<ImageView>(R.id.ivNatClubRight)

        when (data.itemType) {
            ITEM_TYPE_MONEY -> {
                when (balanceEnum) {
                    BalanceEnum.YING_XING_GUO,
                    BalanceEnum.YING_XING_YE,
                    BalanceEnum.JI_FEN_SHANG_CHENG -> {
                        tvTitle.text = data.name
                        ivPoint.isSelected = data.isCashIn
                        tvMoney.isSelected = data.isCashIn
                        tvMoney.text = PriceChangeUtils.getNumKb(data.amount)
                    }
                    BalanceEnum.NAT -> {
                        val strTitle = "${data.createDate}   ${data.name}"
                        tvTitle.text = strTitle
                        ivPoint.isSelected = data.isCashIn
                        tvMoney.isSelected = data.isCashIn
                        tvMoney.text = PriceChangeUtils.getNumKb(data.amount)
                    }
                    BalanceEnum.YUAN_LI_ZHI -> {
                        val strTitle = "${data.createDate}   ${data.name}"
                        tvTitle.text = strTitle
                        ivPoint.setVisible(false)
                        tvMoney.isSelected = data.isCashIn
                        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}分"
                    }
                    BalanceEnum.NAT_CLUB -> {
                        if (isDetail) {
                            val strTitle = "${data.createDate}   ${data.stateDepict}"
                            tvTitle.text = strTitle
                        } else {
                            val strTitle = "${data.createDate}   ${data.name}"
                            tvTitle.text = strTitle
                        }
                        ivPoint.setVisible(false)
                        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}"
                        ivNatClubRight.setVisible((helper.adapterPosition - 1) == natSelector)
                    }
                    BalanceEnum.YIN_XING_BAO -> {
                        val strTitle = "${data.createDate}   ${data.name}"
                        tvTitle.text = strTitle
                        ivPoint.setVisible(false)
                        tvMoney.setTextColor(mContext.resources.getColor(R.color.color_org))
                        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}"
                        ivNatClubRight.setImageResource(R.mipmap.dui_icon_3_red)
                        ivNatClubRight.setVisible((helper.adapterPosition - 1) == natSelector)
                    }
                }
            }
            ITEM_TYPE_WITH_BTN -> {
                val tvProductName = helper.getView<TextView>(R.id.tvProductName)
                val tvCashOutBalance = helper.getView<TextView>(R.id.tvCashOutBalance)
                val tvToUse = helper.getView<TextView>(R.id.tvToUse)
                helper.addOnClickListener(R.id.tvCashOutBalance)
                    .addOnClickListener(R.id.tvToUse)
                when (balanceEnum) {
                    BalanceEnum.YIN_XING_BAO -> {
                        val strTitle = "${data.createDate}   ${data.name}"
                        tvTitle.text = strTitle
                        ivPoint.setVisible(false)
                        tvMoney.setTextColor(mContext.resources.getColor(R.color.color_org))
                        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}"
                        ivNatClubRight.setImageResource(R.mipmap.dui_icon_3_red)
                        ivNatClubRight.setVisible((helper.adapterPosition - 1) == natSelector)
                        tvProductName.text = data.productName
                    }
                    BalanceEnum.NAT_CLUB -> {
                        if (isDetail) {
                            val strTitle = "${data.createDate}   ${data.stateDepict}"
                            tvTitle.text = strTitle
                        } else {
                            val strTitle = "${data.createDate}   ${data.name}"
                            tvTitle.text = strTitle
                        }
                        tvCashOutBalance.setTextColor(mContext.resources.getColor(R.color.theme_color))
                        tvCashOutBalance.text = "去置换"
                        tvCashOutBalance.background =
                            mContext.resources.getDrawable(R.drawable.shape_theme_stroke_radius_11)
                        tvToUse.setTextColor(mContext.resources.getColor(R.color.theme_color))
                        tvToUse.text = "去质押"
                        tvToUse.background =
                            mContext.resources.getDrawable(R.drawable.shape_theme_stroke_radius_11)
                        ivPoint.setVisible(false)
                        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}"
                        ivNatClubRight.setVisible((helper.adapterPosition - 1) == natSelector)
                        tvProductName.text = data.productName
                    }
                    BalanceEnum.NAT_ZHIYA_CLUB -> {
                        if (isDetail) {
                            val strTitle = "${data.createDate}   ${data.stateDepict}"
                            tvTitle.text = strTitle
                        } else {
                            val strTitle = "${data.createDate}   ${data.name}"
                            tvTitle.text = strTitle
                        }
                        tvCashOutBalance.setVisible(false)
                        tvToUse.setVisible(false)
                        ivPoint.setVisible(false)
                        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}枚"
                        ivNatClubRight.setVisible((helper.adapterPosition - 1) == natSelector)
                        tvProductName.text = data.productName
                    }
                }
            }
            ITEM_TYPE_DATE -> {
                helper.setText(R.id.tvDate, data.createDate)
            }
        }
    }
}