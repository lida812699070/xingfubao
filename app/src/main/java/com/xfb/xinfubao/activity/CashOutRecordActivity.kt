package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setColorText
import kotlinx.android.synthetic.main.activity_cash_out_record.*

/** 转出记录 */
class CashOutRecordActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {
    /** 0 申请提现记录  1 申请转出记录  2 财务记录 */
    var state = 0

    companion object {
        fun toActivity(context: Context, state: Int) {
            val intent = Intent(context, CashOutRecordActivity::class.java)
            intent.putExtra("state", state)
            context.startActivity(intent)
        }
    }

    val adapter =
        object : BaseQuickAdapter<ItemBalanceModel, BaseViewHolder>(
            R.layout.item_cash_out_record,
            list
        ) {
            override fun convert(helper: BaseViewHolder, data: ItemBalanceModel) {
                val tvTitle = helper.getView<TextView>(R.id.tvTitle)
                val tvState = helper.getView<TextView>(R.id.tvState)
                val tvMoney = helper.getView<TextView>(R.id.tvMoney)
                val strTitle = "${data.createDate}   ${data.name}"
                tvTitle.setColorText(
                    strTitle,
                    mContext.resources.getColor(R.color.color_text_888),
                    11,
                    16
                )
                tvMoney.text = "${PriceChangeUtils.getNumKbs(data.amount)}"
                if (state == 0) {
                    tvState.text = data.stateDepict
                    tvState.setBackgroundDrawable(
                        if (data.isSuccess)
                            mContext.resources.getDrawable(R.drawable.shape_light_org_radius_13)
                        else
                            mContext.resources.getDrawable(R.drawable.shape_888_radius_13)
                    )
                } else if (state == 1) {
                    tvState.text = data.stateDepict
                    tvState.setBackgroundDrawable(
                        if (data.isSuccess)
                            mContext.resources.getDrawable(R.drawable.shape_org_radius_13)
                        else
                            mContext.resources.getDrawable(R.drawable.shape_888_radius_13)
                    )
                } else if (state == 2) {
                    tvState.text = data.stateDepict
                    tvState.setBackgroundDrawable(
                        if (data.isSuccess)
                            mContext.resources.getDrawable(R.drawable.shape_theme_radius_13)
                        else
                            mContext.resources.getDrawable(R.drawable.shape_888_radius_13)
                    )
                    tvTitle.text = strTitle
                }
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
        myToolbar.setClick { finish() }
        var title = "转出记录"
        if (state == 0) {
            title = "提现记录"
        } else if (state == 1) {
            title = "转出记录"
        } else if (state == 2) {
            title = "财务记录"
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
        } else if (state == 2) {//NAT抵押
            requestWithError(
                RetrofitCreateHelper.createApi(BaseApi::class.java).financialRecord(
                    map
                ), {
                    loadData(it.data)
                }) {
                showLoadError()
            }
        }


    }


}
