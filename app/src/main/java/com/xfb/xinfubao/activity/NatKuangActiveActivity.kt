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
import android.widget.ImageView
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.NatActiveItem
import com.xfb.xinfubao.model.NatActiveModel
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_nat_kuang_active.*

/** 抢注矿主列表  矿机活动列表  */
class NatKuangActiveActivity : BaseRecyclerViewActivity<NatActiveItem>() {


    val headList = arrayListOf<NatActiveModel>()
    //抢注矿主  矿基活动
    var type = 0
    //明细类型 0-投入 1-收益
    var itemType = 0
    var headerView: View? = null
    var headerRecyclerView: RecyclerView? = null
    var tabLayout: TabLayout? = null
    var currentActiveIndex = 0

    val headerAdapter = object :
        BaseQuickAdapter<NatActiveModel, BaseViewHolder>(
            R.layout.item_header_nat_active,
            headList
        ) {
        override fun convert(helper: BaseViewHolder, item: NatActiveModel) {
            helper.setText(R.id.tvTitle, item.activityName)
                .setText(R.id.tvContent, item.thawTime)
                .addOnClickListener(R.id.tvAction)
        }
    }
    val adapter =
        object :
            BaseQuickAdapter<NatActiveItem, BaseViewHolder>(R.layout.item_balance_money, list) {
            override fun convert(helper: BaseViewHolder, item: NatActiveItem) {
                val tvTitle = helper.getView<TextView>(R.id.tvTitle)
                val ivPoint = helper.getView<ImageView>(R.id.ivPoint)
                val tvMoney = helper.getView<TextView>(R.id.tvMoney)
                ivPoint.setVisible(false)
                val strText = "${item.createTime}    ${item.remark}"
                tvMoney.text = PriceChangeUtils.getNumKb(item.num)
                tvTitle.setColorText(strText, resources.getColor(R.color.color_text_888), 10, 16)
            }
        }

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

    override fun pageAdapter(): BaseQuickAdapter<NatActiveItem, BaseViewHolder> {
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
        headerAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tvAction) {
                NatKuangDetailActivity.toActivity(this, type, headList[position].id)
            }
        }
        headerAdapter.setOnItemClickListener { adapter, view, position ->
            if (currentActiveIndex != position) {
                currentActiveIndex = position
                adapter.notifyDataSetChanged()
            }
        }
        requestHeadDateHeader()
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
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if ("投入明细" == p0?.text) {
                    itemType = 0
                } else if ("收益明细" == p0?.text) {
                    itemType = 1
                }
                showProgress("请稍候")
                onRefresh()
            }
        })
    }

    override fun onRefresh() {
        page = initialPage
        requestHeadDateHeader()
    }

    /**
     * 刷新头部详细
     */
    private fun requestHeadDateHeader() {
        val map = hashMapOf<String, String>()
        map["activityType"] = "$type"
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        requestWithError(
            RetrofitCreateHelper.createApi(BaseApi::class.java)
                .activityList(map)
            , {
                headList.clear()
                headList.addAll(it.data)
                headerAdapter.notifyDataSetChanged()
                initData()
            }, {
                showLoadError()
            })

    }

    /**
     * 加载数据
     */
    override fun initData() {
        val map = hashMapOf<String, String>()
        map["activityId"] = "${headList[currentActiveIndex].id}"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        map["type"] = "$itemType"
        requestWithError(
            RetrofitCreateHelper.createApi(BaseApi::class.java)
                .activityJoinRecord(map), {
                loadData(it.data)
            }, {
                showLoadError()
            })
    }
}
