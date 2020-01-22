package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_help_detail.*

/** 帮助详情 */
class HelpDetailActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_help_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        initTopRecyclerView()
    }

    private fun initTopRecyclerView() {
        val topTitles = arrayListOf<String>()
        topTitles.add("为什么一定要备份助记词！")
        topTitles.add("如何创建ETH钱包？")
        topTitles.add("忘记钱包密码怎么办？")
        topTitles.add("ETH转账／收款教程")
        topTitles.add("如何找回没有映射的ETH钱包中的代币?")
        recyclerViewTop.layoutManager = LinearLayoutManager(this)
        recyclerViewTop.adapter = object :
            BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_problem_normal, topTitles) {
            override fun convert(helper: BaseViewHolder, item: String) {
                helper.setText(R.id.tvTitle, item)
            }
        }
    }
}
