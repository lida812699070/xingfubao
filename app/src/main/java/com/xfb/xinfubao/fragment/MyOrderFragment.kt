package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.view.ProductListView

/** 我的订单Fragment */
class MyOrderFragment : BaseRecyclerViewFragment<String>() {

    val adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_my_order, list) {
        override fun convert(helper: BaseViewHolder, item: String) {
            val productListView = helper.getView<ProductListView>(R.id.productList)
            val data = arrayListOf<String>()
            data.add("")
            data.add("")
            data.add("")
//            productListView.bindData(data)
        }
    }

    override fun pageAdapter(): BaseQuickAdapter<String, BaseViewHolder>? {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout?
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_order
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {

    }

    override fun pageRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView) as RecyclerView?
    }

    override fun initData() {
        Handler().postDelayed({
            val datas = listOf("", "", "", "", "", "", "", "", "")
            loadData(datas)
        }, 1000)
    }

    override fun initLogic() {
        initData()
    }


}