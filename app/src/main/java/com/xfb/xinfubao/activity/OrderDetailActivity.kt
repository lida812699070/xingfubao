package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.CommentUtils
import com.careagle.sdk.utils.PriceChangeUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.OrderDetailModel
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setDrawLeft
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_order_detail.*

/** 订单详情 */
class OrderDetailActivity : DefaultActivity() {
    var orderNO: String? = null
    var data: OrderDetailModel? = null

    companion object {
        fun toActivity(context: Context, orderNO: String?) {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("orderNO", orderNO)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_order_detail
    }

    override fun initView(savedInstanceState: Bundle?) {
        orderNO = intent.getStringExtra("orderNO")
        myToolbar.setClick { finish() }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["orderNumber"] = "$orderNO"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getRealOrder(map)) {
            it.data?.let {
                bindData(it)
            }
        }
    }

    private fun bindData(data: OrderDetailModel) {
        this.data = data
        addressTakeView.bindData(data.receive)
        productList.bindData(data.productBase)
        var totalCount = 0
        var totalPrice = 0.0
        data.productBase.forEach {
            totalCount += it.num
            totalPrice += (it.productPrice * it.num)
        }
        tvTotalCount.text = "共 $totalCount 件商品 合计："
        tvTotalMoney.text = getString(R.string.rmb_tag, PriceChangeUtils.getDoubleKb(totalPrice))
        tvSendPrice.text = getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(data.freight))
        //小计
        tvProductTotalPrice.text =
            getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(data.totalAmount))
        tvVipSaveMoney.setVisible(data.discountAmount != 0.0)
        tvVipSaveMoney.text = "VIP折扣已节省¥${PriceChangeUtils.getNumKb(data.discountAmount)}"
        tvVipLevelSaveMoney.text = getString(
            R.string.rmb_tag,
            PriceChangeUtils.getNumKb(data.paymentInfo.paymentAmount)
        )
        tvRemark.text = if (TextUtils.isEmpty(data.orderDesc)) "无" else data.orderDesc
        //配送信息
        if (!TextUtils.isEmpty(data.deliveryNumber)) {
            val strSendWay = "${data.deliveryStateDesc} ${data.deliveryNumber}"
            tvSendWay.setColorText(
                strSendWay,
                resources.getColor(R.color.color_text_888),
                data.deliveryStateDesc.length,
                strSendWay.length
            )
            tvRealSendWayGoods.setTextColor(resources.getColor(R.color.theme_color))
            tvRealSendWayGoods.text = "复制"
            tvRealSendWayGoods.setOnClickListener {
                CommentUtils.copy(data.deliveryNumber)
            }
        }
        tvPayWay.text = data.paymentInfo.paymentMethod
        val strOrderNo = "订单编号：${data.orderNumber}"
        tvOrderNo.setColorText(
            strOrderNo,
            resources.getColor(R.color.color_text_888),
            5,
            strOrderNo.length
        )

        tvCopyOrderNo.setOnClickListener {
            CommentUtils.copy(data.orderNumber)
        }
        val tvOrderCreateTimeStr = "创建时间：${data.createDate}"
        val tvOrderPayTimeStr = "付款时间：${data.createDate}"
        val tvOrderSendTimeStr = "发货时间：${data.createDate}"
        val tvOrderFinishTimeStr = "成交时间：${data.createDate}"
        tvOrderCreateTime.setColorText(
            tvOrderCreateTimeStr,
            resources.getColor(R.color.color_text_888),
            5,
            tvOrderCreateTimeStr.length
        )
        tvOrderPayTime.setColorText(
            tvOrderPayTimeStr,
            resources.getColor(R.color.color_text_888),
            5,
            tvOrderPayTimeStr.length
        )
        tvOrderSendTime.setColorText(
            tvOrderSendTimeStr,
            resources.getColor(R.color.color_text_888),
            5,
            tvOrderSendTimeStr.length
        )
        tvOrderFinishTime.setColorText(
            tvOrderFinishTimeStr,
            resources.getColor(R.color.color_text_888),
            5,
            tvOrderFinishTimeStr.length
        )

        gpWait.setVisible(data.orderState == 100L)
        tvOrderStateBig.setVisible(data.orderState != 100L)
        clBottom.setVisible(data.orderState == 100L)
        llBottom.setVisible(data.orderState != 100L)
        if (data.orderState == 100L) {
            tvVipLevelSaveMoneyText.text = "VIP等级折扣"
            tvVipSaveMoney.setVisible(false)
            tvVipLevelSaveMoney.setTextColor(resources.getColor(R.color.color_light_org))
            tvVipLevelSaveMoney.text =
                getString(R.string._rmb_tag, PriceChangeUtils.getDoubleKb(data.discountAmount))
            tvOrderPrice.text =
                "应付款：${getString(
                    R.string.rmb_tag,
                    PriceChangeUtils.getNumKb(data.totalAmount - data.discountAmount)
                )}"
            tvOrderVipSaveMoney.text =
                "VIP折扣已节省${getString(
                    R.string.rmb_tag,
                    PriceChangeUtils.getNumKb(data.discountAmount)
                )}"
            tvOrderBalance.setOnClickListener {
                toCreateOrder()
            }
        }

        /**
         * 100-待付款，102-待发货，112-待收货，106-已完成，107-已取消
         */
        when (data.orderState) {
            100L -> {
                tvOrderNo.setVisible(false)
                tvOrderCreateTime.setVisible(false)
                tvOrderPayTime.setVisible(false)
                tvOrderSendTime.setVisible(false)
                tvOrderFinishTime.setVisible(false)
                tvPayWayText.setVisible(false)
                tvPayWay.setVisible(false)
                tvCopyOrderNo.setVisible(false)
            }
            102L -> {
                tvOrderSendTime.setVisible(false)
                tvOrderFinishTime.setVisible(false)
                tvOrderStateBig.text = "等待卖家发货"
                tvBottomYellow.text = "返回"
                tvBottomYellow.setOnClickListener { finish() }
                tvBottomTheme.setVisible(false)
            }
            112L -> {
                tvOrderFinishTime.setVisible(false)
                tvBottomYellow.text = "物流查询"
                tvBottomYellow.setVisible(false)
                tvOrderStateBig.text = "卖家已发货"
                tvBottomTheme.text = "确认收货"
                tvBottomTheme.setOnClickListener {
                    val map = hashMapOf<String, String>()
                    map["userId"] = "${ConfigUtils.userId()}"
                    map["orderNumber"] = data.orderNumber
                    request(RetrofitCreateHelper.createApi(BaseApi::class.java).confirmReceipt(map)) {
                        showMessage("确认收货成功")
                        finish()
                    }
                }
            }
            106L -> {
                tvBottomYellow.text = "返回"
                tvBottomYellow.setOnClickListener { finish() }
                tvOrderStateBig.text = "订单已完成"
                tvOrderStateBig.setDrawLeft(R.mipmap.icon_order_finish)
                tvBottomTheme.setVisible(false)
            }
            107L -> {
                tvOrderPayTime.setVisible(false)
                tvOrderSendTime.setVisible(false)
                tvOrderFinishTime.setVisible(false)
                tvPayWayText.setVisible(false)
                tvPayWay.setVisible(false)
                tvBottomYellow.text = "返回"
                tvBottomYellow.setOnClickListener { finish() }
                tvBottomTheme.setVisible(false)
                tvOrderStateBig.text = "交易已关闭"
                tvOrderStateBig.setDrawLeft(R.mipmap.jiaoyiguanbi_icon)
            }
        }

    }

    /** 立即结算生成订单 */
    private fun toCreateOrder() {
        orderNO?.let {
            val orderIds = arrayListOf<String>()
            orderIds.add(it)
            startActivity(
                Intent(this, CashInActivity::class.java)
                    .putExtra("data", orderIds)
            )
        }
    }
}
