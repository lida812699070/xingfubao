package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_cash_out_record.*

/** 转出记录 */
class CashOutRecordActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_cash_out_record
    }

    override fun initView(savedInstanceState: Bundle?) {
        val list = arrayListOf<String>()
        list.add("")
        list.add("")
        list.add("")
        list.add("")
        myToolbar.setClick { finish() }
        tvBack.setOnClickListener { finish() }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter=object :BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_cash_out_record,list){
            override fun convert(helper: BaseViewHolder?, item: String?) {

            }
        }
    }

}
