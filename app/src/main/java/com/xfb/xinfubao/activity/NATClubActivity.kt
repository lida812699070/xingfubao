package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
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
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_natclub.*

/** NAT基金会 */
class NATClubActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {

    val adapter = BalanceAdapter(list)
    var showDiYaDialog: AlertDialog? = null
    var selectPosition = -1

    override fun getLayoutId(): Int {
        return R.layout.activity_natclub
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

    }

    override fun initLogic() {
        adapter.balanceEnum = BalanceEnum.NAT_CLUB
        tvToDiYa.setOnClickListener {
            toDiya()
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            initHeader(it.data)
        }
    }

    private fun toDiya() {
        if (selectPosition == -1) {
            showMessage("请选择抵押产品")
            return
        }
        showDiYaDialog = DialogUtils.showDiYaDialog(this) {
            showDiYaDialog?.dismiss()
            val map = hashMapOf<String, String>()
            val itemBalanceModel = list[selectPosition]
            map["ordernum"] = itemBalanceModel.id
            map["orderId"] = itemBalanceModel.orderNum
            map["userId"] = "${ConfigUtils.userId()}"
            map["payPassword"] = it
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).mortgageNat(map)) {
                showMessage("操作成功")
                finish()
            }
        }
    }

    private fun initHeader(userInfo: UserInfo) {
        val headerView = LayoutInflater.from(this).inflate(R.layout.header_nat, null)
        val ivFinish = headerView.findViewById<ImageView>(R.id.ivFinish)
        val tvDiYaRecord = headerView.findViewById<TextView>(R.id.tvDiYaRecord)
        val tvTotalMoney = headerView.findViewById<TextView>(R.id.tvTotalMoney)
        ivFinish.setOnClickListener {
            finish()
        }
        tvDiYaRecord.setOnClickListener {
            CashOutRecordActivity.toActivity(this, 2)
        }
        tvTotalMoney.text = PriceChangeUtils.getNumKb(userInfo.userAssets.natMortgagedNum)
        adapter.addHeaderView(headerView)
        adapter.setHeaderAndEmpty(true)
        initData()

        adapter.setOnItemClickListener { adapter, view, position ->
            selectPosition = position
            adapter.notifyDataSetChanged()
        }
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).getNatInfo(map), {
            loadData(it.data)
        }) {
            showLoadError()
        }
    }

}
