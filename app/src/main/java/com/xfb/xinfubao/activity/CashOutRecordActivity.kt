package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.RecordAdapter
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_cash_out_record.*

/** 转出记录 */
class CashOutRecordActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {
    /** 0 申请提现记录  1 申请转出记录  2 财务记录 3转入明细 4导入明细*/
    var state = 0
    val adapter = RecordAdapter(list)

    companion object {
        fun toActivity(context: Context, state: Int) {
            val intent = Intent(context, CashOutRecordActivity::class.java)
            intent.putExtra("state", state)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_cash_out_record
    }

    override fun initView(savedInstanceState: Bundle?) {
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

    override fun initLogic() {
        state = intent.getIntExtra("state", 0)
        adapter.state = state
        myToolbar.setClick { finish() }
        var title = "转出记录"
        if (state == 0) {
            title = "提现记录"
        } else if (state == 1) {
            title = "转出记录"
        } else if (state == 2) {
            title = "财务记录"
        } else if (state == 3) {
            title = "转入明细"
        } else if (state == 4) {
            title = "导入明细"
        }
        myToolbar.setTitle(title)
        val footerView = LayoutInflater.from(this).inflate(R.layout.footer_record, null)
        val tvBack = footerView.findViewById<TextView>(R.id.tvBack)
        tvBack.setOnClickListener { finish() }

        if (state == 0) {
            tvBack.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_org_light_radius_8))
        } else if (state == 1) {
            tvBack.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_org_radius_8))
        } else if (state == 2) {
            tvBack.setBackgroundDrawable(resources.getDrawable(R.drawable.shape_theme_btn_bg))
        } else if (state == 3) {
            tvBack.setVisible(false)
        } else if (state == 4) {
            tvBack.setVisible(false)
        }
        adapter.addFooterView(footerView)

        initData()

        adapter.setOnItemClickListener { adapter, view, position ->
            ApplyCashOutResultActivity.toActivity(this, state, list[position])
        }
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        map["userId"] = "${ConfigUtils.userId()}"
        if (state == 0) {//银杏果提现
            map["assetsCode"] = "yxg"
            requestWithError(
                RetrofitCreateHelper.createApi(BaseApi::class.java).getWithdrawalsRecord(
                    map
                ), {
                    loadData(it.data)
                }) {
                showLoadError()
            }
        } else if (state == 1) {//银杏宝转出
            map["assetsCode"] = "yxb"
            requestWithError(
                RetrofitCreateHelper.createApi(BaseApi::class.java).getWithdrawalsRecord(
                    map
                ), {
                    loadData(it.data)
                }) {
                showLoadError()
            }
        } else if (state == 2) {//财务记录
            requestWithError(
                RetrofitCreateHelper.createApi(BaseApi::class.java).financialRecord(
                    map
                ), {
                    it.data.forEach {
                        it.itemType = RecordAdapter.ITEM_TYPE_CAI_WU
                    }
                    loadData(it.data)
                }) {
                showLoadError()
            }
        } else if (state == 3) {//转入明细
            requestWithError(
                RetrofitCreateHelper.createApi(BaseApi::class.java).exchangeBalanceRecord(
                    map
                ), {
                    loadData(it.data)
                }) {
                showLoadError()
            }
        } else if (state == 4) {//导入明细
            requestWithError(
                RetrofitCreateHelper.createApi(BaseApi::class.java).importInfo(
                    map
                ), {
                    loadData(it.data)
                }) {
                showLoadError()
            }
        }


    }


}
