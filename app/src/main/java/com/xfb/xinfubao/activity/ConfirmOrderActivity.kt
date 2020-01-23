package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.OrderInfo
import com.xfb.xinfubao.model.Product
import com.xfb.xinfubao.model.ReceiveVo
import com.xfb.xinfubao.model.RequestOrderModel
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_confirm_order.*

//确认订单
class ConfirmOrderActivity : DefaultActivity() {
    var catId: String? = null
    var receiveVo: ReceiveVo? = null
    private var list = arrayListOf<Product>()

    companion object {
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
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).confirmOrder(requestOrderModel)) {
            bindData(it.data)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindData(data: OrderInfo) {
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

    }

    private fun initListener() {
        myToolbar.setClick { finish() }
        tvOrderBalance.setOnClickListener {
            if (receiveVo == null) {
                showMessage("请选择收货地址")
                return@setOnClickListener
            }
            startActivity(Intent(this, CashInActivity::class.java))
        }
        //选择地址
        tvAddress.setOnClickListener {
            startActivity(Intent(this, AddressManagerActivity::class.java))
        }
    }
}
