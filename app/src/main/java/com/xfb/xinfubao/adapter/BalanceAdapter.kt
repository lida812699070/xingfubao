package com.xfb.xinfubao.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ItemBalanceModel

class BalanceAdapter(data: List<ItemBalanceModel>) :
    BaseMultiItemQuickAdapter<ItemBalanceModel, BaseViewHolder>(data) {
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

    }
}