package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.NatActiveItem
import com.xfb.xinfubao.model.NatActiveModel
import com.xfb.xinfubao.model.event.NatKuangEvent
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.loadRound
import com.xfb.xinfubao.utils.setColorText
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_nat_kuang_active.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/** 抢注矿主列表  矿机活动列表  */
class NatKuangActiveActivity : BaseRecyclerViewActivity<NatActiveItem>() {


    val headList = arrayListOf<NatActiveModel>()
    //抢注矿主  矿基活动
    var type = 0
    //明细类型 0-投入 1-收益
    var itemType = 0
    var headerView: View? = null
    var headerRecyclerView: RecyclerView? = null
    var tabLayout: TabLayout? = null

    val headerAdapter = object :
        BaseQuickAdapter<NatActiveModel, BaseViewHolder>(
            R.layout.item_header_nat_active,
            headList
        ) {
        override fun convert(helper: BaseViewHolder, item: NatActiveModel) {
            helper.setText(R.id.tvTitle, item.activityName)
                .addOnClickListener(R.id.tvAction)
            val tvContent = helper.getView<TextView>(R.id.tvContent)
            val ivImage = helper.getView<ImageView>(R.id.ivImage)
            val tvAction = helper.getView<TextView>(R.id.tvAction)
            ivImage.loadRound(item.activityIcon, 6f)
            val color = mContext.getColor(R.color.theme_color)
            tvAction.text = if (type == 0) "抢注" else "参与"
            tvAction.isSelected = item.isButtonState
            if (item.activityWay == 1) {
                if (item.thawTime == null) return
                val day = item.thawTime.day
                val hours = item.thawTime.hours
                val minutes = item.thawTime.minutes
                val seconds = item.thawTime.seconds
                val text = "结束还有${day}天${hours}小时${minutes}分${seconds}秒"
                val spannableStringCoupon = SpannableString(text)
                try {
                    spannableStringCoupon.setSpan(
                        ForegroundColorSpan(color),
                        4, "结束还有${day}".length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableStringCoupon.setSpan(
                        ForegroundColorSpan(color),
                        "结束还有${day}天".length,
                        "结束还有${day}天${hours}".length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableStringCoupon.setSpan(
                        ForegroundColorSpan(color),
                        "结束还有${day}天${hours}小时".length,
                        "结束还有${day}天${hours}小时${minutes}".length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    spannableStringCoupon.setSpan(
                        ForegroundColorSpan(color),
                        "结束还有${day}天${hours}小时${minutes}分".length,
                        "结束还有${day}天${hours}小时${minutes}分${seconds}".length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    tvContent.setText(spannableStringCoupon)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (item.activityWay == 2) {
                val strContent = "名额还剩${item.joinNumber}个"
                tvContent.setColorText(strContent, color, 4, strContent.length - 1)
            }
        }

    }
    val adapter =
        object :
            BaseQuickAdapter<NatActiveItem, BaseViewHolder>(R.layout.item_balance_money, list) {
            override fun convert(helper: BaseViewHolder, item: NatActiveItem) {
                val tvTitle = helper.getView<TextView>(R.id.tvTitle)
                val ivPoint = helper.getView<ImageView>(R.id.ivPoint)
                val tvMoney = helper.getView<TextView>(R.id.tvMoney)
                ivPoint.setVisible(false)
                val strText = "${item.createTime}    ${item.remark}"
                tvMoney.text = PriceChangeUtils.getNumKb(item.num)
                tvTitle.setColorText(strText, resources.getColor(R.color.color_text_888), 10, 16)
            }
        }

    override fun getLayoutId(): Int {
        return R.layout.activity_nat_kuang_active
    }

    companion object {
        fun toActivity(context: Context, type: Int = 0) {
            val intent = Intent(context, NatKuangActiveActivity::class.java)
            intent.putExtra("type", type)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        EventBus.getDefault().register(this)
    }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<NatActiveItem, BaseViewHolder> {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun initLogic() {
        type = intent.getIntExtra("type", 0)
        myToolbar.setClick { finish() }
        myToolbar.setTitle(if (type == 1) "抢注矿主" else "矿基活动")
        adapter.setHeaderAndEmpty(false)
        initHeader()
        headerAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.tvAction && headList[position].isButtonState) {
                NatKuangDetailActivity.toActivity(this, type, headList[position].id)
            }
        }
        headerAdapter.setOnItemClickListener { adapter, view, position ->
            //            if (currentActiveIndex != position) {
//                currentActiveIndex = position
//                adapter.notifyDataSetChanged()
//            }
        }
        requestHeadDateHeader()
    }

    private fun initHeader() {
        headerView = LayoutInflater.from(this).inflate(R.layout.header_nat_kuang_active, null)
        headerRecyclerView = headerView?.findViewById<RecyclerView>(R.id.headerRecyclerView)
        tabLayout = headerView!!.findViewById<TabLayout>(R.id.tabLayout)
        tabLayout!!.addTab(tabLayout!!.newTab().setText("投入明细"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("收益明细"))
        headerRecyclerView!!.layoutManager = LinearLayoutManager(this)
        headerRecyclerView!!.adapter = headerAdapter
        adapter.addHeaderView(headerView)
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if ("投入明细" == p0?.text) {
                    itemType = 0
                } else if ("收益明细" == p0?.text) {
                    itemType = 1
                }
                showProgress("请稍候")
                onRefresh()
            }
        })
    }

    override fun onRefresh() {
        page = initialPage
        requestHeadDateHeader()
    }

    /**
     * 刷新头部详细
     */
    private fun requestHeadDateHeader() {
        val map = hashMapOf<String, String>()
        map["activityType"] = "$type"
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        requestWithError(
            RetrofitCreateHelper.createApi(BaseApi::class.java)
                .activityList(map)
            , {
                headList.clear()
                headList.addAll(it.data)
                headerAdapter.notifyDataSetChanged()
                initData()
            }, {
                showLoadError()
            })

    }

    /**
     * 加载数据
     */
    override fun initData() {
        val map = hashMapOf<String, String>()
        map["activityType"] = "$type"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        map["type"] = "$itemType"
        requestWithError(
            RetrofitCreateHelper.createApi(BaseApi::class.java)
                .activityJoinRecord(map), {
                loadData(it.data)
            }, {
                showLoadError()
            })
    }

    @Subscribe
    fun eventAction(event: NatKuangEvent) {
        onRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
