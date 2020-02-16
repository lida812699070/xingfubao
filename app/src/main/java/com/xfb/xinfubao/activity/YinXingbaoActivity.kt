package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.widget.SwipeRefreshLayout
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
import com.xfb.xinfubao.adapter.BalanceAdapter
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.model.event.EventTransfer
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_yin_xingbao.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** 银杏宝 */
class YinXingbaoActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {

    val adapter = BalanceAdapter(list)
    var selectPosition = -1
    var headerView: View? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_yin_xingbao
    }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<ItemBalanceModel, BaseViewHolder> {
        return adapter
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun initLogic() {
        adapter.balanceEnum = BalanceEnum.YIN_XING_BAO
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            initHeader(it.data)
        }
        EventBus.getDefault().register(this)
    }

    private fun initHeader(data: UserInfo) {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_yxb, null)
        adapter.addHeaderView(headerView)
        adapter.setHeaderAndEmpty(true)
        val ivFinish = headerView!!.findViewById<ImageView>(R.id.ivFinish)
        val tvSubTitle = headerView!!.findViewById<TextView>(R.id.tvSubTitle)
        val tvToCashOut = headerView!!.findViewById<TextView>(R.id.tvToCashOut)
        val tabLayout = headerView!!.findViewById<TabLayout>(R.id.tabLayout)
        ivFinish.setOnClickListener {
            finish()
        }
        tvSubTitle.setOnClickListener {
            WebviewActivity.newInstanceUrl(this, Constant.YIN_XING_BAO_RULE_URL, "银杏宝规则")
        }
        initHeaderData(data)
        tvToCashOut.setOnClickListener {
            if (selectPosition == -1) {
                showMessage("请选择转出记录")
                return@setOnClickListener
            }
            ApplyCashOutActivity.toActivity(
                this,
                1,
                list[selectPosition].id,
                list[selectPosition].amount
            )
        }
        tabLayout.addTab(tabLayout.newTab().setText("明细"))
        initData()

        adapter.setOnItemClickListener { adapter, view, position ->
            selectPosition = position
            this.adapter.natSelector = selectPosition
            adapter.notifyDataSetChanged()
        }
    }

    private fun initHeaderData(data: UserInfo) {
        if (headerView == null) return
        val tvTotalMoney = headerView!!.findViewById<TextView>(R.id.tvTotalMoney)
        val tvTotalCashIn = headerView!!.findViewById<TextView>(R.id.tvTotalCashIn)
        val tvTotalCashInYXY = headerView!!.findViewById<TextView>(R.id.tvTotalCashInYXY)
        tvTotalMoney?.text = PriceChangeUtils.getNumKb(data.userAssets.ginkgoTreasureNum)
        tvTotalCashIn?.text = PriceChangeUtils.getNumKb(data.userAssets.ginkgoFruitNum)
        tvTotalCashInYXY?.text = PriceChangeUtils.getNumKb(data.userAssets.ginkgoLeafNum)
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
//        map["tagType"] = "$it"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).ginkgoTreasureInfo(map),
            {
                loadData(it.data)
            }) {
            showLoadError()
        }
    }

    @Subscribe
    fun transfer(event: EventTransfer) {
        selectPosition = -1
        this.adapter.natSelector = selectPosition
        onRefresh()
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            ConfigUtils.saveUserInfo(it.data)
            initHeaderData(it.data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
