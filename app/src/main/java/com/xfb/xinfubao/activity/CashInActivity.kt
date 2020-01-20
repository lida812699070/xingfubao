package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_cash_in.*

/** 收银台 */
class CashInActivity : DefaultActivity() {

    var list = arrayListOf<String>()
    var adapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_cash_in_order_price, list) {
            override fun convert(helper: BaseViewHolder?, item: String?) {

            }
        }

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_in
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick({ finish() })
        
        initRecyclerView()
        list.add("")
        list.add("")
        list.add("")
        adapter.notifyDataSetChanged()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

}
