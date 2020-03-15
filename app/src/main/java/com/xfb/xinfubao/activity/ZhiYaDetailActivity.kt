package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.model.event.ShuHuiEvent
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_zhi_ya_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** 质押详情 */
class ZhiYaDetailActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {
    val adapter = BalanceAdapter(list)
    //使用  未使用
    var isUse = true
    var selectPosition = -1
    var headerView: View? = null

    companion object {
        fun toActivity(context: Context, isUse: Boolean = true) {
            val intent = Intent(context, ZhiYaDetailActivity::class.java)
            intent.putExtra("isUse", isUse)
            context.startActivity(intent)
        }
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

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_zhi_ya_detail
    }

    override fun initLogic() {
        isUse = intent.getBooleanExtra("isUse", true)
        adapter.balanceEnum = BalanceEnum.NAT_CLUB
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            initHeader(it.data)
        }
        if (isUse) {
            tvBottom1.setVisible(false)
            tvBottom2.text = "申请赎回"
        }

        tvBottom1.setOnClickListener {
            if (selectPosition == -1) {
                showMessage("请先选择产品")
                return@setOnClickListener
            }
            ShuHuiZhiYaActivity.toActivity(this, list[selectPosition])
        }
        tvBottom2.setOnClickListener {
            if (selectPosition == -1) {
                showMessage("请先选择产品")
                return@setOnClickListener
            }
            if (isUse) {
                ShuHuiZhiYaActivity.toActivity(this, list[selectPosition])
            } else {
                //TODO 转出
            }
        }
    }

    private fun initHeader(data: UserInfo) {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_zhiya_detail, null)
        val ivFinish = headerView!!.findViewById<ImageView>(R.id.ivFinish)
        ivFinish.setOnClickListener { finish() }
        bindHeadData(data)
        adapter.addHeaderView(headerView)
        adapter.setHeaderAndEmpty(true)
        initData()
        adapter.setOnItemClickListener { adapter, view, position ->
            selectPosition = position
            this.adapter.natSelector = selectPosition
            adapter.notifyDataSetChanged()
        }
    }

    @Subscribe
    fun refreshPage(event: ShuHuiEvent) {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            ConfigUtils.saveUserInfo(it.data)
            bindHeadData(it.data)
            selectPosition = -1
            this.adapter.natSelector = selectPosition
            onRefresh()
        }
    }

    private fun bindHeadData(data: UserInfo) {
        val tvCount = headerView!!.findViewById<TextView>(R.id.tvCount)
        val tvCountText = headerView!!.findViewById<TextView>(R.id.tvCountText)
        tvCount.text =
            if (isUse) PriceChangeUtils.getNumKb(data.userAssets.pledgeUseMoney) else
                PriceChangeUtils.getNumKb(data.userAssets.pledgeMoney)
        tvCountText.text = if (isUse) "质押产品获得的NAT数量（已使用）" else "质押产品获得的NAT数量（未使用）"
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageSize"] = "$pageSize"
        map["type"] = "${if (isUse) 1 else 2}"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).pledgeList(map), {
            loadData(it.data)
        }) {
            showLoadError()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
