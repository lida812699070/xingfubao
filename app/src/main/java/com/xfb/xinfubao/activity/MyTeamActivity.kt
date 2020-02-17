package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.MyTeamModel
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_my_team.*

/** 我的团队 */
class MyTeamActivity : BaseRecyclerViewActivity<MyTeamModel>() {

    val adapter =
        object : BaseQuickAdapter<MyTeamModel, BaseViewHolder>(R.layout.item_my_team, list) {
            override fun convert(helper: BaseViewHolder, item: MyTeamModel) {
                helper.setText(R.id.tvMobile, item.tel)
                    .setText(R.id.tvLevel, item.grade)
                    .setText(R.id.tvTeamNumber, "${item.teamNumber}")
                    .setText(R.id.tvTeamYeji, PriceChangeUtils.getNumKb(item.teamPerformance))
            }
        }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<MyTeamModel, BaseViewHolder> {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_team
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initLogic() {
        val headerView = LayoutInflater.from(this).inflate(R.layout.header_my_team, null)
        headerView.findViewById<ImageView>(R.id.ivFinish).setOnClickListener { finish() }
        val mUserInfo = ConfigUtils.mUserInfo
        headerView.findViewById<TextView>(R.id.tvZhiTuiNumber).text = "${mUserInfo.pushTheNumber}"
        headerView.findViewById<TextView>(R.id.tvTeamNumber).text = "${mUserInfo.teamNumber}"
        headerView.findViewById<TextView>(R.id.tvTeamTotalYeJi).text =
            "${PriceChangeUtils.getDoubleKb(mUserInfo.teamPerformance)}"
        adapter.addHeaderView(headerView)
        initData()
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).getTeam(map), {
            loadData(it.data)
            pageAdapter().loadMoreEnd()
        }) {
            showLoadError()
        }
    }

}
