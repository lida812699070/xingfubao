package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.BalanceAdapter
import com.xfb.xinfubao.model.ItemBalanceModel
import kotlinx.android.synthetic.main.activity_nat_kuang_active.*

/** 抢注矿主列表  矿机活动列表  */
class NatKuangActiveActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {

    val adapter = BalanceAdapter(list)
    val headList = arrayListOf<String>("", "", "")
    val headerAdapter = object :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_header_nat_active, headList) {
        override fun convert(helper: BaseViewHolder, item: String) {

        }
    }
    //抢注矿主  矿基活动
    var type = 0
    var headerView: View? = null
    var headerRecyclerView: RecyclerView? = null
    var tabLayout: TabLayout? = null


    override fun getLayoutId(): Int {
        return R.layout.activity_nat_kuang_active
    }

    companion object {
        fun toActivity(context: Context, type: Int = 0) {
            val intent = Intent(context, NatKuangActiveActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<ItemBalanceModel, BaseViewHolder> {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun initLogic() {
        type = intent.getIntExtra("type", 0)
        myToolbar.setClick { finish() }
        myToolbar.setTitle(if (type == 0) "抢注矿主" else "矿基活动")
        adapter.setHeaderAndEmpty(false)
        initHeader()
        initData()
    }

    private fun initHeader() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_nat_kuang_active, null)
        headerRecyclerView = headerView?.findViewById<RecyclerView>(R.id.headerRecyclerView)
        tabLayout = headerView!!.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("投入明细"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("收益明细"))
        headerRecyclerView!!.layoutManager = LinearLayoutManager(this)
        headerRecyclerView!!.adapter = headerAdapter
        adapter.addHeaderView(headerView)
        headerAdapter.setOnItemClickListener { adapter, view, position ->
            NatKuangDetailActivity.toActivity(this, type)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        requestHeadDateHeader()
    }

    /**
     * 刷新头部详细
     */
    private fun requestHeadDateHeader() {
        initHeader()
    }

    /**
     * 加载数据
     */
    override fun initData() {

    }
}
