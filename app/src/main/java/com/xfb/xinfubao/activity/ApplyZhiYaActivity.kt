package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.CommentUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.CompanyModel
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.event.ZhiYaEvent
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_apply_zhi_ya.*
import org.greenrobot.eventbus.EventBus

/** 申请质押 */
class ApplyZhiYaActivity : DefaultActivity() {
    var itemBalanceModel: ItemBalanceModel? = null
    var showDiYaDialog: AlertDialog? = null
    var companyList = arrayListOf<CompanyModel>()
    var company: CompanyModel? = null

    companion object {
        fun toActivity(
            context: Context,
            itemBalanceModel: ItemBalanceModel
        ) {
            val intent = Intent(context, ApplyZhiYaActivity::class.java)
            intent.putExtra("itemBalanceModel", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_zhi_ya
    }

    override fun initView(savedInstanceState: Bundle?) {
        itemBalanceModel = intent.getSerializableExtra("itemBalanceModel") as ItemBalanceModel?
        ivFinish.setOnClickListener { finish() }
        tvCopyAddress.setOnClickListener {
            CommentUtils.copy(tvAddressInfo.text.toString())
        }
        tvOrderNoCopy.setOnClickListener {
            val primaryClip = ConfigUtils.getPrimaryClip(this)
            etOrderNo.setText(primaryClip)
        }
        tvOk.setOnClickListener {
            toApplyZhiYa()
        }
        tvCompany.setOnClickListener {
            selectCompany()
        }
        viewBg.setOnClickListener {
            gpSelector.setVisible(false)
        }

        val map = hashMapOf<String, String>()
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).returnAddress(map)) {
            tvAddressInfo.text =
                "退货地址：${it.data.address}\n 收货人：${it.data.consignee}\n 电话号码：${it.data.tel}\n"
        }
        initCompanyRecyclerView()

    }

    var companyAdapter =
        object : BaseQuickAdapter<CompanyModel, BaseViewHolder>(
            R.layout.item_pay_way,
            companyList
        ) {
            override fun convert(helper: BaseViewHolder, item: CompanyModel) {
                helper.setText(R.id.tvPayWay, "${item.logisticsCode}")
            }
        }

    private fun initCompanyRecyclerView() {
        companyRecyclerView.layoutManager = LinearLayoutManager(this)
        companyRecyclerView.adapter = companyAdapter
        companyAdapter.setOnItemClickListener { adapter, view, position ->
            tvCompany.setTextColor(resources.getColor(R.color.color_text_111))
            company = companyList[position]
            tvCompany.text = company!!.logisticsCode
            gpSelector.setVisible(false)
        }
    }

    private fun selectCompany() {
        val map = hashMapOf<String, String>()
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).logisticsList(map)) {
            companyList.clear()
            companyList.addAll(it.data)
            gpSelector.setVisible(true)
        }
    }

    /** 申请质押 */
    private fun toApplyZhiYa() {
        val companyStr = company?.logisticsCode
        val orderNoStr = etOrderNo.text.toString()
        if (TextUtils.isEmpty(companyStr)) {
            showMessage("请输入物流公司")
            return
        }
        if (TextUtils.isEmpty(orderNoStr)) {
            showMessage("请输入运单号码")
            return
        }
        showDiYaDialog = DialogUtils.showDiYaDialog(this, 3) {
            showMessage("请稍候")
            val map = hashMapOf<String, String>()
            map["userId"] = "${ConfigUtils.userId()}"
            map["orderNo"] = "${itemBalanceModel?.orderNum}"
            map["money"] = "${itemBalanceModel?.amount}"
            map["payPwd"] = it
            map["logisticsCode"] = orderNoStr
            map["logisticsId"] = "${company?.logisticsId}"
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).natPledgeApply(map)) {
                showMessage("申请质押提交成功")
                EventBus.getDefault().post(ZhiYaEvent())
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showDiYaDialog?.dismiss()
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
