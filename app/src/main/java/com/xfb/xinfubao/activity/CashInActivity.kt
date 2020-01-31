package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.*
import com.xfb.xinfubao.model.event.EventPauResult
import com.xfb.xinfubao.myenum.ChangePasswordEnum
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_cash_in.*
import kotlinx.android.synthetic.main.activity_cash_in.myToolbar
import kotlinx.android.synthetic.main.activity_safe_center.*
import org.greenrobot.eventbus.EventBus

/** 收银台 */
class CashInActivity : DefaultActivity() {

    var list = arrayListOf<RegisterOrderVo>()
    var payWayList = arrayListOf<PayMethod>()
    var data: CashRegisterModel? = null
    var payMethod: PayMethod? = null
    var showDiYaDialog: AlertDialog? = null
    var adapter =
        object : BaseQuickAdapter<RegisterOrderVo, BaseViewHolder>(
            R.layout.item_cash_in_order_price,
            list
        ) {
            override fun convert(helper: BaseViewHolder, item: RegisterOrderVo) {
                helper.setText(R.id.tvTitle, "订单${helper.adapterPosition + 1}")
                    .setText(R.id.tvOrderNO, item.orderNumber)
                    .setText(
                        R.id.tvPrice,
                        getString(R.string.rmb_tag, PriceChangeUtils.getDoubleKb(item.payAmount))
                    )
            }
        }
    var payWayAdapter =
        object : BaseQuickAdapter<PayMethod, BaseViewHolder>(
            R.layout.item_pay_way,
            payWayList
        ) {
            override fun convert(helper: BaseViewHolder, item: PayMethod) {
                helper.setText(R.id.tvPayWay, "${item.payMethodName}")
            }
        }

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_in
    }

    override fun initView(savedInstanceState: Bundle?) {
        val orderIds = intent.getSerializableExtra("data") as ArrayList<String>
        myToolbar.setClick { finish() }
        initRecyclerView()

        val map = RequestCashRegisterDto()
        map.userId = "${ConfigUtils.userId()}"
        map.orderNumber = orderIds
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).cashRegister(map)) {
            bindData(it.data)
        }

        //立即支付
        tvToPay.setOnClickListener {
            if (true == ConfigUtils.mUserInfo?.isPayPwd) {
                showDiYaDialog = DialogUtils.showDiYaDialog(this, 1) {
                    showDiYaDialog?.hide()
                    toPay(it)
                }
            } else {
                showProgress("请稍候")
                val params = hashMapOf<String, String>()
                params["userId"] = "${ConfigUtils.userId()}"
                request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(params)) {
                    ConfigUtils.saveUserInfo(it.data)
                    if (it.data.isPayPwd) {
                        showDiYaDialog = DialogUtils.showDiYaDialog(this, 1) {
                            showDiYaDialog?.hide()
                            toPay(it)
                        }
                    } else {
                        showMessage("请先设置支付密码")
                        ChangePasswordActivity.toActivity(
                            ChangePasswordEnum.SET_PAY_PASSWORD,
                            this,
                            tvResetPayPassword.text.toString()
                        )
                    }
                }
            }

        }

        //选择支付方式
        tvPayWayText.setOnClickListener {
            payWayRecyclerView.setVisible(true)
        }

        initPayWayRecyclerView()
    }

    private fun initPayWayRecyclerView() {
        payWayRecyclerView.layoutManager = LinearLayoutManager(this)
        payWayRecyclerView.adapter = payWayAdapter
        payWayAdapter.setOnItemClickListener { adapter, view, position ->
            payMethod = payWayList[position]
            tvPayWay.text = payMethod?.payMethodName
            payWayRecyclerView.setVisible(false)
        }
    }

    private fun toPay(payPwd: String) {
        if (data == null) {
            return
        }
        showProgress("请稍候")
        val requestPay = RequestPay()
        requestPay.userId = "${ConfigUtils.userId()}"
        requestPay.payWay = "${payMethod?.payMethodId}"
        requestPay.payAmount = "${data?.totalAmount}"
        requestPay.payPwd = payPwd
        val orderNos = arrayListOf<String>()
        for (registerOrderVo in list) {
            orderNos.add(registerOrderVo.orderNumber)
        }
        requestPay.orderNO = orderNos
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).pay(requestPay)) {
            val payResultModel = PayResultModel()
            payResultModel.payAmout = data!!.totalAmount
            payResultModel.payMethod = payMethod
            PayResultActivity.toActivity(this, payResultModel)
            finish()
            EventBus.getDefault().post(EventPauResult())
        }
    }

    private fun bindData(data: CashRegisterModel?) {
        this.data = data
        data?.let {
            list.clear()
            list.addAll(it.registerOrderVos)
            adapter.notifyDataSetChanged()
            tvTotalPrice.text =
                "总金额：${getString(R.string.rmb_tag, PriceChangeUtils.getDoubleKb(it.totalAmount))}"
            payMethod = data.payMethod?.get(0)
            tvPayWay.text = payMethod?.payMethodName
            payWayList.clear()
            payWayList.addAll(data.payMethod)
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        showDiYaDialog?.dismiss()
    }
}
