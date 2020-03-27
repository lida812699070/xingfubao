package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
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
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.NatUnlockPakeageModel
import com.xfb.xinfubao.model.UserInfo
import com.xfb.xinfubao.model.event.ZhiYaEvent
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_natclub.*
import kotlinx.android.synthetic.main.header_nat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** NAT基金会 */
class NATClubActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {

    val adapter = BalanceAdapter(list)
    var showDiYaDialog: AlertDialog? = null
    var selectUnlockModel: NatUnlockPakeageModel? = null
    var showNatUnlockSelectDialog: AlertDialog? = null
    var headerView: View? = null

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
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun zhiya(event: ZhiYaEvent) {
        refreshPage()
    }

    override fun initLogic() {
        adapter.balanceEnum = BalanceEnum.NAT_CLUB
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            initHeader(it.data)
        }
    }

    private fun toZhiYa(selectPosition: Int) {
        val itemBalanceModel = list[selectPosition]
        showDiYaDialog = DialogUtils.showDiYaDialog(this, 3) {
            showDiYaDialog?.dismiss()
            val map = hashMapOf<String, String>()
            map["orderNo"] = itemBalanceModel.orderNum
            map["userId"] = "${ConfigUtils.userId()}"
            map["payPwd"] = it
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).natPledgeApply(map)) {
                showMessage(it.msg)
                refreshPage()
            }
        }
    }

    /** 去置换 */
    private fun toZhiHuan(selectPosition: Int) {
        ToExchangeActivity.toActivity(this, list[selectPosition])
    }

    private fun refreshPage() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getUserInfo(map)) {
            ConfigUtils.saveUserInfo(it.data)
            bindHeaderData(it.data)
            this.adapter.natSelector = -1
            onRefresh()
            tvToUse.isSelected = false
            tvToDiYa.isSelected = false
            tvToZhiYa.isSelected = false
        }
    }

    private fun bindHeaderData(it: UserInfo) {
        val tvTotalMoney = headerView!!.findViewById<TextView>(R.id.tvTotalMoney)
        val tvUnUseCount = headerView!!.findViewById<TextView>(R.id.tvUnUseCount)
        tvTotalMoney.text =
            PriceChangeUtils.getNumKb(it.userAssets.natMortgagedNum)
        tvUnUseCount.text = PriceChangeUtils.getNumKb(it.userAssets.pledgeMoney)
        tvUnUseCount.setOnClickListener {
            ZhiYaDetailActivity.toActivity(this)
        }
    }

    private fun initHeader(userInfo: UserInfo) {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_nat, null)
        val ivFinish = headerView!!.findViewById<ImageView>(R.id.ivFinish)
        val tvDiYaRecord = headerView!!.findViewById<TextView>(R.id.tvDiYaRecord)
        val tvSelectUnLock = headerView!!.findViewById<TextView>(R.id.tvSelectUnLock)
        val tvCashInKuangChang = headerView!!.findViewById<TextView>(R.id.tvCashInKuangChang)
        tvCashInKuangChang.setOnClickListener {
            ZhiYaDetailActivity.toActivity(this)
        }
        tvSelectUnLock.setOnClickListener {
            showProgress("请稍候")
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).getNatUnlockPackage(mapOf())) {
                showNatUnlockSelectDialog =
                    DialogUtils.showNatUnlockSelectDialog(this, it.data) {
                        selectUnlockModel = it
                        tvSelectUnLock.text = ""
                        gpUnlockInfo.setVisible(true)
                        tvNATtUnLockTitle.text = it.name
                        tvNATtUnLockInfo.text =
                            "期数：${it.lockTime}个月  兑换比例：${PriceChangeUtils.getDoubleKb(it.unlockRatio)}"
                        showNatUnlockSelectDialog?.dismiss()
                    }
            }
        }
        ivFinish.setOnClickListener {
            finish()
        }
        tvDiYaRecord.setOnClickListener {
            CashOutRecordActivity.toActivity(this, 2)
        }
        bindHeaderData(userInfo)
        adapter.addHeaderView(headerView)
        adapter.setHeaderAndEmpty(true)
        initData()

        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.tvCashOutBalance -> {
                    toZhiHuan(position)
                }
                R.id.tvToUse -> {
                    toZhiYa(position)
                }
            }
        }
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).getNatInfo(map), {
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
