package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.model.HelpBottomModel
import kotlinx.android.synthetic.main.activity_help.*

/** 帮助 */
class HelpActivity : DefaultActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_help
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        myToolbar.setRightClickStr("反馈") {
            WebviewActivity.newInstanceUrl(this, Constant.FEEDBACK, "意见反馈")
        }
        initTopRecyclerView()
        initBottomRecyclerView()
    }

    private fun initBottomRecyclerView() {
        val bottomTitles = arrayListOf<HelpBottomModel>()
        bottomTitles.add(HelpBottomModel("基础操作", "身份／钱包／交易"))
        bottomTitles.add(HelpBottomModel("助记词、私钥和keystore", "导入／导出／备份"))
        bottomTitles.add(HelpBottomModel("转账收款", "未打包／未到帐／失败"))
        bottomTitles.add(HelpBottomModel("钱包安全", "丢币／盗币／安全提示"))
        recyclerViewBottom.layoutManager = LinearLayoutManager(this)
        val adapter = object :
            BaseQuickAdapter<HelpBottomModel, BaseViewHolder>(
                R.layout.item_problem_bottom,
                bottomTitles
            ) {
            override fun convert(helper: BaseViewHolder, item: HelpBottomModel) {
                helper.setText(R.id.tvTitle, item.title)
                    .setText(R.id.tvSubTitle, item.subtitle)
            }
        }
        recyclerViewBottom.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(this, HelpDetailActivity::class.java))
        }
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
