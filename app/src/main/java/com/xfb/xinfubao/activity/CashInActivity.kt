package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.*
import com.xfb.xinfubao.model.event.EventPauResult
import com.xfb.xinfubao.model.event.PaySucessEvent
import com.xfb.xinfubao.payment.PayUtils
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.onClick
import com.xfb.xinfubao.utils.setVisible
import com.xfb.xinfubao.wxapi.WXUtils
import kotlinx.android.synthetic.main.activity_cash_in.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.math.max

/** 收银台 */
class CashInActivity : DefaultActivity() {

    var list = arrayListOf<RegisterOrderVo>()
    var payWayList = arrayListOf<PayMethod>()
    var data: CashRegisterModel? = null
    var payMethod: PayMethod? = null
    var showDiYaDialog: AlertDialog? = null
    var selectBalanceModel: SelectBalanceModel? = null
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
        tvToPay.onClick {
            if (105L == payMethod?.payMethodId || 106L == payMethod?.payMethodId) {
                toPay("")
            } else {
                checkPayPassword {
                    showDiYaDialog = DialogUtils.showDiYaDialog(this, 1) {
                        showDiYaDialog?.dismiss()
                        toPay(it)
                    }
                }
            }
        }

        //选择支付方式
        tvPayWayText.setOnClickListener {
            gpSelector.setVisible(true)
        }

        viewBg.setOnClickListener {
            gpSelector.setVisible(false)
        }

        //资产抵扣
        tvBalanceDiKouText.setOnClickListener {
            if (data != null && data?.favorableVosVos != null && data!!.favorableVosVos!!.size > 0) {
                SelectBalanceDiKouActivity.toActivity(
                    this,
                    data?.favorableVosVos,
                    selectBalanceModel
                )
            }
        }
        initPayWayRecyclerView()
        EventBus.getDefault().register(this)
    }

    private fun initPayWayRecyclerView() {
        payWayRecyclerView.layoutManager = LinearLayoutManager(this)
        payWayRecyclerView.adapter = payWayAdapter
        payWayAdapter.setOnItemClickListener { adapter, view, position ->
            payMethod = payWayList[position]
            tvPayWay.text = payMethod?.payMethodName
            gpSelector.setVisible(false)
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
        if (selectBalanceModel != null) {
            requestPay.favorableId = selectBalanceModel?.id
            requestPay.favorableAmount = selectBalanceModel?.favorable
            requestPay.payAmount = "${data!!.totalAmount - selectBalanceModel!!.favorable}"
        }
        val orderNos = arrayListOf<String>()
        for (registerOrderVo in list) {
            orderNos.add(registerOrderVo.orderNumber)
        }
        requestPay.orderNO = orderNos
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).pay(requestPay)) {
            if (105L == payMethod?.payMethodId) {//微信
//                val wechatPaymentModel = WechatPaymentModel()
//                wechatPaymentModel.convert()

                val msgApi = WXAPIFactory.createWXAPI(this, WXUtils.WX_APP_ID)
                msgApi.registerApp(it.data.paySign.app_id)
                val convert = it.data.paySign.convert()
                PayUtils(this).wechatPayment(convert)
            } else if (106L == payMethod?.payMethodId) {//支付宝
                val payModel = PayModel(
                    2,
                    it.data.id,
                    it.data.orderStr,
                    null
                )
                toPay(payModel)
            } else {
                paySuccess()
            }
        }
    }

    private fun paySuccess() {
        val payResultModel = PayResultModel()
        payResultModel.payAmout = data!!.totalAmount
        payResultModel.payMethod = payMethod
        PayResultActivity.toActivity(this, payResultModel)
        finish()
        EventBus.getDefault().post(EventPauResult())
    }

    private fun bindData(data: CashRegisterModel?) {
        this.data = data
        var isCanDikou = false
        data?.favorableVosVos?.let {
            isCanDikou = it.size > 0
        }
        tvDiKouMoney.setVisible(isCanDikou && selectBalanceModel != null)
        gpCanDikou.setVisible(isCanDikou)
        data?.let {
            list.clear()
            list.addAll(it.registerOrderVos)
            adapter.notifyDataSetChanged()
            tvTotalPrice.text =
                "总金额：${getString(R.string.rmb_tag, PriceChangeUtils.getDoubleKb(it.totalAmount))}"
            tvShouldPayMoney.text =
                "应付款：${getString(R.string.rmb_tag, PriceChangeUtils.getDoubleKb(it.totalAmount))}"
            payMethod = data.payMethod?.get(0)
            tvPayWay.text = payMethod?.payMethodName
            payWayList.clear()
            payWayList.addAll(data.payMethod)
        }
        if (selectBalanceModel == null) {
            tvBalanceDiKou.setTextColor(resources.getColor(R.color.color_text_888))
            tvBalanceDiKou.text = "请选择"
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val selectBalanceModel = data?.getSerializableExtra("data") as SelectBalanceModel?
            changeSelect(selectBalanceModel)
        }
    }

    private fun changeSelect(selectBalanceModel: SelectBalanceModel?) {
        this.selectBalanceModel = selectBalanceModel
        if (selectBalanceModel == null) {
            bindData(data)
            return
        }
        tvDiKouMoney.setVisible(true)
        tvDiKouMoney.text =
            "${selectBalanceModel.assetsName}抵扣：${getString(
                R.string.rmb_tag,
                PriceChangeUtils.getDoubleKb(selectBalanceModel.favorable)
            )}"
        var shouldPayMoney = data!!.totalAmount - selectBalanceModel.favorable
        shouldPayMoney = max(shouldPayMoney, 0.0)
        tvShouldPayMoney.text =
            "应付款：${getString(
                R.string.rmb_tag,
                PriceChangeUtils.getDoubleKb(shouldPayMoney)
            )}"
        tvBalanceDiKou.setTextColor(resources.getColor(R.color.color_light_org))
        tvBalanceDiKou.text = "${selectBalanceModel.assetsName}抵扣减${getString(
            R.string.rmb_tag,
            PriceChangeUtils.getDoubleKb(selectBalanceModel.favorable)
        )}"
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        showDiYaDialog?.dismiss()
        EventBus.getDefault().unregister(this)
    }

    fun toPay(payModel: PayModel) {
        payModel.payInfo = payModel.orderStr
        PayUtils(this).toPay(payModel, true)
    }

    @Subscribe
    fun payEnd(event: PaySucessEvent) {
        if (event.code == 1) {
            paySuccess()
        } else {
            showMessage(event.msg)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && gpSelector.visibility == View.VISIBLE) {
            gpSelector.setVisible(false)
            return false
        } else {
            return super.onKeyDown(keyCode, event)
        }
    }

}
