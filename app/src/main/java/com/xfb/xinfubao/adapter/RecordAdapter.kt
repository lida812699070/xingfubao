package com.xfb.xinfubao.adapter

import android.widget.TextView
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible

class RecordAdapter(data: List<ItemBalanceModel>) :
    BaseMultiItemQuickAdapter<ItemBalanceModel, BaseViewHolder>(data) {

    var state = 0

    companion object {
        //普通
        var ITEM_TYPE_NORMAL = 0
        //财务记录
        var ITEM_TYPE_CAI_WU = 1
    }

    init {
        addItemType(ITEM_TYPE_NORMAL, R.layout.item_cash_out_record)
        addItemType(ITEM_TYPE_CAI_WU, R.layout.item_cash_out_record_cai_wu)
    }

    override fun convert(helper: BaseViewHolder, data: ItemBalanceModel) {
        val tvTitle = helper.getView<TextView>(R.id.tvTitle)
        val tvState = helper.getView<TextView>(R.id.tvState)
        val tvMoney = helper.getView<TextView>(R.id.tvMoney)
        val strTitle = "${data.createDate}   ${data.name}"
        tvTitle.setColorText(
            strTitle,
            mContext.resources.getColor(R.color.color_text_888),
            11,
            16
        )
        tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}"
        when (data.itemType) {
            ITEM_TYPE_NORMAL -> {
                if (state == 0) {
                    tvState.text = data.stateDepict
                    tvState.setBackgroundDrawable(
                        if (data.isSuccess)
                            mContext.resources.getDrawable(R.drawable.shape_light_org_radius_13)
                        else
                            mContext.resources.getDrawable(R.drawable.shape_888_radius_13)
                    )
                } else if (state == 1) {
                    tvState.text = data.stateDepict
                    tvState.setBackgroundDrawable(
                        if (data.isSuccess)
                            mContext.resources.getDrawable(R.drawable.shape_org_radius_13)
                        else
                            mContext.resources.getDrawable(R.drawable.shape_888_radius_13)
                    )
                } else if (state == 2) {
                    tvState.text = data.stateDepict
                    tvState.setBackgroundDrawable(
                        if (data.isSuccess)
                            mContext.resources.getDrawable(R.drawable.shape_theme_radius_13)
                        else
                            mContext.resources.getDrawable(R.drawable.shape_888_radius_13)
                    )
                    tvTitle.text = strTitle
                } else if (state == 3) {
                    tvState.text = data.stateDepict
                    tvState.setVisible(false)
                    tvTitle.text = strTitle
                }
            }
            ITEM_TYPE_CAI_WU -> {
                val tvProductName = helper.getView<TextView>(R.id.tvProductName)
                tvState.text = data.stateDepict
                tvTitle.text = strTitle
                tvProductName.text = data.productName
            }
        }

    }

}