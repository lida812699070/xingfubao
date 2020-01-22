package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_cart.*

/** 购物车 */
class CartActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_cart
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        val list = arrayListOf<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_cart, list) {
            override fun convert(helper: BaseViewHolder?, item: String?) {

            }

        }
        recyclerView.adapter = adapter
    }

}
