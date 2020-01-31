package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.OrderDetailActivity
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.MyOrderModel
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible
import com.xfb.xinfubao.view.ProductListView

/** 我的订单Fragment */
class MyOrderFragment : BaseRecyclerViewFragment<MyOrderModel>() {
    private var state = 0

    /**
     * 100-待付款，102-待发货，112-待收货，106-已完成，不传查全部
     */
    companion object {
        fun newInstance(state: Int = 0): MyOrderFragment {
            val fragmentLogin = MyOrderFragment()
            val bundle = Bundle()
            bundle.putInt("state", state)
            fragmentLogin.arguments = bundle
            return fragmentLogin
        }
    }

    val adapter =
        object : BaseQuickAdapter<MyOrderModel, BaseViewHolder>(R.layout.item_my_order, list) {
            override fun convert(helper: BaseViewHolder, item: MyOrderModel) {
                val productListView = helper.getView<ProductListView>(R.id.productList)
                val tvOrderNo = helper.getView<TextView>(R.id.tvOrderNo)
                val tvTotalMoney = helper.getView<TextView>(R.id.tvTotalMoney)
                val tvTotalCount = helper.getView<TextView>(R.id.tvTotalCount)
                val tvCancelOrder = helper.getView<TextView>(R.id.tvCancelOrder)
                val tvOrderState = helper.getView<TextView>(R.id.tvOrderState)
                helper.addOnClickListener(R.id.tvToOrderDetail)
                    .addOnClickListener(R.id.tvCancelOrder)

                productListView.bindData(item.productBase)
                val strOrderNo = "订单编号：${item.orderNumber}"
                tvOrderNo.setColorText(
                    strOrderNo,
                    mContext.resources.getColor(R.color.color_text_888),
                    5,
                    strOrderNo.length
                )
                tvTotalMoney.text = mContext.getString(
                    R.string.rmb_tag,
                    PriceChangeUtils.getNumKb(item.totalAmount)
                )
                tvCancelOrder.setVisible(item.orderState == 100)
                var totalCount = 0
                item.productBase.forEach {
                    totalCount += it.num
                }
                tvTotalCount.text = "共${totalCount}件商品  合计："
                var strState = ""
                when (item.orderState) {
                    100 -> {
                        strState = "等待买家付款"
                    }
                    102 -> {
                        strState = "等待卖家发货"
                    }
                    112 -> {
                        strState = "等待卖家已发货"
                    }
                    106 -> {
                        strState = "订单已完成"
                    }
                }
                tvOrderState.text = strState
            }
        }

    override fun pageAdapter(): BaseQuickAdapter<MyOrderModel, BaseViewHolder>? {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout?
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_order
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {

    }

    override fun pageRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView) as RecyclerView?
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        map["userId"] = "${ConfigUtils.userId()}"
        if (state != 0) {
            map["orderState"] = "$state"
        }
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).listRealOrder(map), {
            showLoadError()
        }, {
            loadData(it.data)
        })
    }

    override fun initLogic() {
        state = arguments!!.getInt("state")
        initData()

        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tvToOrderDetail -> {
                    OrderDetailActivity.toActivity(activity!!, list[position].orderNumber)
                }
                R.id.tvCancelOrder -> {
                    cancelOrder(position)
                }
            }
        }

    }

    private fun cancelOrder(position: Int) {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["orderNumber"] = "${list[position].orderNumber}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).cancelOrder(map)) {
            showMessage("取消成功")
            onRefresh()
        }
    }


}