package com.xfb.xinfubao.view

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.view_product_list.view.*

class ProductListView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val list = arrayListOf<String>()
    var adapter: BaseQuickAdapter<String, BaseViewHolder>? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_product_list, this)
        itemRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter =
            object :
                BaseQuickAdapter<String, BaseViewHolder>(
                    R.layout.item_my_order_product_item,
                    list
                ) {
                override fun convert(helper: BaseViewHolder?, item: String?) {

                }
            }
        itemRecyclerView.adapter = adapter
    }

    fun bindData(datas: List<String>) {
        list.clear()
        list.addAll(datas)
        adapter?.notifyDataSetChanged()
    }
}