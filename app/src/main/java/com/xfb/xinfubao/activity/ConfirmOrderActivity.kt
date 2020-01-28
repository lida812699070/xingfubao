package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.careagle.sdk.weight.EmptyView
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.*
import com.xfb.xinfubao.model.event.EventPauResult
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_confirm_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

//确认订单
class ConfirmOrderActivity : DefaultActivity() {
    var catId: String? = null
    var receiveVo: ReceiveVo? = null
    var data: OrderInfo? = null
    private var list = arrayListOf<Product>()

    companion object {
        const val INTENT_RESULT_ADDRESS = 2
        fun toActivity(
            context: Context,
            productList: ArrayList<Product>,
            catId: String? = null
        ) {
            val intent = Intent(context, ConfirmOrderActivity::class.java)
            intent.putExtra("productList", productList)
            intent.putExtra("catId", catId)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_confirm_order
    }

    override fun initView(savedInstanceState: Bundle?) {
        list = intent.getSerializableExtra("productList") as ArrayList<Product>
        catId = intent.getStringExtra("catId")
        initListener()
        val requestOrderModel = RequestOrderModel()
        requestOrderModel.userId = ConfigUtils.userId()
        requestOrderModel.productDtoList = list
        emptyView.setLoadState(EmptyView.LoadState.LOAD_STATE_LOADING)
        emptyView.setReloadListener {
            requestComfirmData(requestOrderModel)
        }
        requestComfirmData(requestOrderModel)
        EventBus.getDefault().register(this)
    }

    /** 请求确认订单接口 */
    private fun requestComfirmData(requestOrderModel: RequestOrderModel) {
        requestWithError(
            RetrofitCreateHelper.createApi(BaseApi::class.java).confirmOrder(
                requestOrderModel
            ), {
                emptyView.setVisible(false)
                bindData(it.data)
            }) {
            emptyView.setLoadState(EmptyView.LoadState.LOAD_STATE_ERROR)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(data: OrderInfo) {
        this.data = data
        receiveVo = data.receiveVo
        tvAddress.bindData(data.receiveVo)
        productList.bindData(list)
        var totalCount = 0
        var totalPrice = 0.0
        list.forEach {
            totalCount += it.num
            totalPrice += (it.productPrice * it.num)
        }
        tvTotalPrice.text = "共 $totalCount 件商品 合计：${PriceChangeUtils.getDoubleKb(totalPrice)}"
        tvSendPrice.text = getString(R.string.rmb_tag, PriceChangeUtils.getDoubleKb(data.freight))
        tvVipLevelSaveMoney.text =
            getString(R.string._rmb_tag, PriceChangeUtils.getDoubleKb(data.discount))
        tvProductTotalPrice.text =
            getString(R.string.rmb_tag, PriceChangeUtils.getDoubleKb(data.orderTotalAmount))
        tvOrderPrice.text =
            "应付款：${getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(data.payAmount))}"
        tvOrderVipSaveMoney.text =
            "VIP折扣已节省${getString(R.string.rmb_tag, PriceChangeUtils.getNumKb(data.discount))}"
        bindAddress()

    }

    private fun initListener() {
        myToolbar.setClick { finish() }
        //立即结算
        tvOrderBalance.setOnClickListener {
            toCreateOrder()
        }
        //选择地址
        tvAddress.setOnClickListener {
            startActivityForResult(
                Intent(this, AddressManagerActivity::class.java),
                INTENT_RESULT_ADDRESS
            )
        }
    }

    /** 立即结算生成订单 */
    private fun toCreateOrder() {
        if (receiveVo == null) {
            showMessage("请选择收货地址")
            return
        }
        if (data == null) {
            return
        }
        val requestSaveOrderModel = RequestSaveOrderModel()
        requestSaveOrderModel.userId = "${ConfigUtils.userId()}"
        requestSaveOrderModel.receiveId = "${receiveVo?.receiveId}"
        requestSaveOrderModel.remark = "${etRemark.text}"
        requestSaveOrderModel.freight = "${data?.freight}"
        requestSaveOrderModel.totalAmount = "${data?.orderTotalAmount}"
        requestSaveOrderModel.discount = "${data?.discount}"
        requestSaveOrderModel.payAmount = "${data?.payAmount}"
        requestSaveOrderModel.productDtoList = list
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).saveOrder(requestSaveOrderModel)) {
            startActivity(
                Intent(this, CashInActivity::class.java)
                    .putExtra("data", it.data)
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INTENT_RESULT_ADDRESS && resultCode == Activity.RESULT_OK && data != null) {
            receiveVo = data.getSerializableExtra("data") as ReceiveVo?
            bindAddress()
        }
    }

    private fun bindAddress() {
        tvAddress.bindData(receiveVo)
    }

    @Subscribe
    fun payResult(event: EventPauResult) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
