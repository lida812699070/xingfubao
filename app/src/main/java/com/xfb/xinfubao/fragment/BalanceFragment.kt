package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.BalanceAdapter
import com.xfb.xinfubao.model.ItemBalanceModel

class BalanceFragment : BaseRecyclerViewFragment<ItemBalanceModel>() {

    var adapter = BalanceAdapter(list)
    override fun getLayoutId(): Int {
        return R.layout.fragment_balance
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {
    }

    override fun pageRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView) as RecyclerView?
    }

    override fun pageAdapter(): BaseQuickAdapter<ItemBalanceModel, BaseViewHolder>? {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout?
    }

    override fun initData() {
        Handler().postDelayed({
            val arrayListOf = arrayListOf<ItemBalanceModel>()
            arrayListOf.add(ItemBalanceModel())
            val itemBalanceModel = ItemBalanceModel()
            itemBalanceModel.itemType = 1
            arrayListOf.add(itemBalanceModel)
            arrayListOf.add(ItemBalanceModel())
            arrayListOf.add(ItemBalanceModel())
            arrayListOf.add(ItemBalanceModel())
            arrayListOf.add(ItemBalanceModel())
            arrayListOf.add(ItemBalanceModel())
            arrayListOf.add(ItemBalanceModel())
            loadData(arrayListOf)
        }, 2000)
    }

    override fun initLogic() {
        initData()
    }
}