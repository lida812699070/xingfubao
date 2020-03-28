package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
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
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.model.event.ShuHuiEvent
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_zhi_ya_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** 质押详情 */
class ZhiYaDetailActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {
    val adapter = BalanceAdapter(list)
    var selectPosition = -1
    var headerView: View? = null

    companion object {
        fun toActivity(context: Context) {
            val intent = Intent(context, ZhiYaDetailActivity::class.java)
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
        adapter.balanceEnum = BalanceEnum.NAT_ZHIYA_CLUB
        adapter.isDetail = true
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            initHeader(it.data)
        }
        //质押赎回
        tvBottom1.setOnClickListener {
            if (selectPosition == -1) {
                showMessage("请选择明细")
                return@setOnClickListener
            }
            if (!it.isSelected) {
                return@setOnClickListener
            }
            ShuHuiZhiYaActivity.toActivity(this, list[selectPosition])
        }
        //质押转出
        tvBottom2.setOnClickListener {
            if (selectPosition == -1) {
                showMessage("请选择明细")
                return@setOnClickListener
            }
            if (!it.isSelected) {
                return@setOnClickListener
            }
            DialogUtils.showDiYaDialog(this, 1) {
                val itemBalanceModel = list[selectPosition]
                val map = hashMapOf<String, String>()
                map["userId"] = "${ConfigUtils.userId()}"
                map["orderNO"] = "${itemBalanceModel.orderNum}"
                map["payPassword"] = it
                showProgress("请稍候")
                request(RetrofitCreateHelper.createApi(BaseApi::class.java).pledgeRollOut(map)) {
                    showMessage(it.msg)
                    refreshPage(ShuHuiEvent())
                }
            }
        }
        //转入矿场
        tvBottom3.setOnClickListener {
            if (selectPosition == -1) {
                showMessage("请选择明细")
                return@setOnClickListener
            }
            if (!it.isSelected) {
                return@setOnClickListener
            }
            val itemBalanceModel = list[selectPosition]
            CashInKuangChangActivity.toActivity(this, itemBalanceModel)
        }
        selectChange()
    }

    private fun shuhui(payPassword: String) {
        if (TextUtils.isEmpty(payPassword)) {
            showMessage("请输入支付密码")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["ordernum"] = list[selectPosition].orderNum
        map["orderId"] = list[selectPosition].id
        map["payPassword"] = payPassword
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).redeem(map)) {
            EventBus.getDefault().post(ShuHuiEvent())
            finish()
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
            selectChange()
            adapter.notifyDataSetChanged()
        }
    }


    private fun selectChange() {
        if (selectPosition == -1) {
            tvBottom1.isSelected = false
            tvBottom2.isSelected = false
            tvBottom3.isSelected = false
        } else {
            val itemBalanceModel = list[selectPosition]
            tvBottom1.isSelected = itemBalanceModel.redeemState == 1//赎回
            tvBottom2.isSelected = itemBalanceModel.isTurnOutPledgeState//转出
            tvBottom3.isSelected = itemBalanceModel.useState == 0//转入矿场
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
            selectChange()
            this.adapter.natSelector = selectPosition
            onRefresh()
        }
    }

    private fun bindHeadData(data: UserInfo) {
        val tvCount = headerView!!.findViewById<TextView>(R.id.tvCount)
        tvCount.text = PriceChangeUtils.getNumKb(data.userAssets.pledgeMoney)
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).pledgeList(map), {
            it.data.forEach {
                it.itemType = BalanceAdapter.ITEM_TYPE_WITH_BTN
            }
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
