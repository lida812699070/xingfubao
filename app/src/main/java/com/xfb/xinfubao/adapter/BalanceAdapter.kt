package com.xfb.xinfubao.adapter

import android.widget.ImageView
import android.widget.TextView
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible

class BalanceAdapter(data: List<ItemBalanceModel>) :
    BaseMultiItemQuickAdapter<ItemBalanceModel, BaseViewHolder>(data) {
    var balanceEnum = BalanceEnum.YING_XING_GUO

    companion object {
        //普通
        var ITEM_TYPE_MONEY = 0
        //时间
        var ITEM_TYPE_DATE = 1
    }

    init {
        addItemType(ITEM_TYPE_MONEY, R.layout.item_balance_money)
        addItemType(ITEM_TYPE_DATE, R.layout.item_balance_date)
    }

    override fun convert(helper: BaseViewHolder, data: ItemBalanceModel) {
        val tvTitle = helper.getView<TextView>(R.id.tvTitle)
        val ivPoint = helper.getView<ImageView>(R.id.ivPoint)
        val tvMoney = helper.getView<TextView>(R.id.tvMoney)

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
                        tvTitle.setColorText(
                            strTitle,
                            mContext.resources.getColor(R.color.color_text_888),
                            11,
                            16
                        )
                        ivPoint.setVisible(false)
                        tvMoney.isSelected = data.isCashIn
                        tvMoney.text = PriceChangeUtils.getNumKb(data.amount)
                    }
                    BalanceEnum.YUAN_LI_ZHI -> {
                        val strTitle = "${data.createDate}   ${data.name}"
                        tvTitle.setColorText(
                            strTitle,
                            mContext.resources.getColor(R.color.color_text_888),
                            11,
                            16
                        )
                        ivPoint.setVisible(false)
                        tvMoney.isSelected = data.isCashIn
                        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}分"
                    }
                }
            }
            ITEM_TYPE_DATE -> {
                helper.setText(R.id.tvDate, data.createDate)
            }
        }
    }
}