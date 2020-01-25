package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
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
import com.xfb.xinfubao.adapter.BalanceAdapter.Companion.ITEM_TYPE_DATE
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setInVisible
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_balancel.*

/**
 * 银杏宝  银杏叶  银杏果  积分商城  愿力值
 */
class BalanceActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {
    val adapter = BalanceAdapter(list)
    var showBalanceDialog: AlertDialog? = null
    lateinit var balanceEnum: BalanceEnum
    var tagType: Int? = null

    companion object {
        fun toActivity(context: Context, enum: BalanceEnum) {
            val intent = Intent(context, BalanceActivity::class.java)
            intent.putExtra("data", enum)
            context.startActivity(intent)
        }
    }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<ItemBalanceModel, BaseViewHolder> {
        return adapter
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_balancel
    }

    override fun initLogic() {
        balanceEnum = intent.getSerializableExtra("data") as BalanceEnum
        adapter.balanceEnum = balanceEnum
        val headerView = LayoutInflater.from(this).inflate(R.layout.header_balance, null)
        adapter.addHeaderView(headerView)
        val userAssets = ConfigUtils.mUserInfo.userAssets
        val ivFinish = headerView.findViewById<ImageView>(R.id.ivFinish)
        val viewTop = headerView.findViewById<View>(R.id.viewTop)
        //标题
        val tvTitle = headerView.findViewById<TextView>(R.id.tvTitle)
        //内容背景图片
        val ivBg = headerView.findViewById<ImageView>(R.id.ivBg)
        //副标题
        val tvSubtitle = headerView.findViewById<TextView>(R.id.tvSubtitle)
        //余额文本
        val tvBalanceText = headerView.findViewById<TextView>(R.id.tvBalanceText)
        //余额值
        val tvBalance = headerView.findViewById<TextView>(R.id.tvBalance)
        //主按钮
        val tvCash = headerView.findViewById<TextView>(R.id.tvCash)
        //另一个主按钮
        val tvCashRight = headerView.findViewById<TextView>(R.id.tvCashRight)
        val tabLayout = headerView.findViewById<TabLayout>(R.id.tabLayout)
        //NAT金额
        val tvNATMoney = headerView.findViewById<TextView>(R.id.tvNATMoney)
        val tvLock = headerView.findViewById<TextView>(R.id.tvLock)

        //监听
        ivFinish.setOnClickListener { finish() }
        when (balanceEnum) {
            BalanceEnum.YING_XING_GUO -> {
                tvTitle.text = BalanceEnum.YING_XING_GUO.tag
                tvSubtitle.text = BalanceEnum.YING_XING_GUO.tag
                ivBg.setImageResource(R.mipmap.yxg_bg)
                tvCash.text = "申请提现"
                tvBalance.text = PriceChangeUtils.getNumKb(userAssets.ginkgoFruitNum)
                tabLayout.addTab(tabLayout.newTab().setText("全部"))
                tabLayout.addTab(tabLayout.newTab().setText("收入"))
                tabLayout.addTab(tabLayout.newTab().setText("提现"))
                //TODO 银杏果申请提现
                tvCash.setOnClickListener {

                }
            }
            BalanceEnum.YING_XING_YE -> {
                tvTitle.text = BalanceEnum.YING_XING_YE.tag
                tvSubtitle.text = BalanceEnum.YING_XING_YE.tag
                ivBg.setImageResource(R.mipmap.icon_yxy)
                tvCash.text = "转账"
                tvBalance.text = PriceChangeUtils.getNumKb(userAssets.ginkgoFruitNum)
                tabLayout.addTab(tabLayout.newTab().setText("全部"))
                tabLayout.addTab(tabLayout.newTab().setText("收入"))
                tabLayout.addTab(tabLayout.newTab().setText("转出"))
                //TODO 银杏叶转账
                tvCash.setOnClickListener {

                }
            }
            BalanceEnum.JI_FEN_SHANG_CHENG -> {
                tvTitle.text = BalanceEnum.JI_FEN_SHANG_CHENG.tag
                tvSubtitle.text = BalanceEnum.JI_FEN_SHANG_CHENG.tag
                ivBg.setImageResource(R.mipmap.icon_jfsc)
                tvCash.text = "转账"
                tvBalance.text = PriceChangeUtils.getNumKb(userAssets.integralNum)
                tabLayout.addTab(tabLayout.newTab().setText("全部"))
                tabLayout.addTab(tabLayout.newTab().setText("收入"))
                tabLayout.addTab(tabLayout.newTab().setText("转出"))
                //TODO 积分商城转账
                tvCash.setOnClickListener {

                }
            }
            BalanceEnum.YUAN_LI_ZHI -> {
                tvTitle.text = BalanceEnum.YUAN_LI_ZHI.tag
                tvSubtitle.text = "做任务免费获取愿力值"
                tvBalanceText.text = "愿力"
                ivBg.setImageResource(R.mipmap.icon_ylz)
                tvCash.text = "愿力值规则"
                tvBalance.text = PriceChangeUtils.getNumKb(userAssets.powerAvowNum)
                tabLayout.addTab(tabLayout.newTab().setText("明细"))
                //TODO 愿力值规则
                tvCash.setOnClickListener {

                }
            }
            BalanceEnum.NAT -> {
                tvTitle.text = BalanceEnum.NAT.tag
                tvSubtitle.setInVisible(false)
                tvBalanceText.text = "可流通"
                tvLock.setVisible(true)
                ivBg.setImageResource(R.mipmap.icon_nat)
                tvCash.text = "兑换商城积分"
                tvCashRight.setVisible(true)
                tvCashRight.text = "提币到钱包"
                tvNATMoney.setVisible(true)
                tvNATMoney.text = PriceChangeUtils.getNumKb(userAssets.natLockNum)
                tvBalance.text = PriceChangeUtils.getNumKb(userAssets.natTotalNum)
                tabLayout.addTab(tabLayout.newTab().setText("解锁明细"))
                tabLayout.addTab(tabLayout.newTab().setText("兑换明细"))
                tabLayout.addTab(tabLayout.newTab().setText("提币明细"))
                //TODO 兑换商城积分
                tvCash.setOnClickListener {

                }
                //TODO 提币到钱包
                tvCashRight.setOnClickListener {

                }
            }
        }
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                if ("全部" == p0?.text) {
                    tagType = null
                } else if ("收入" == p0?.text) {
                    tagType = 1
                } else if ("提现" == p0?.text) {
                    tagType = 2
                } else if ("转出" == p0?.text) {
                    tagType = 2
                } else if ("解锁明细" == p0?.text) {
                    tagType = 0
                } else if ("兑换明细" == p0?.text) {
                    tagType = 1
                } else if ("提币明细" == p0?.text) {
                    tagType = 2
                }
                showProgress("请稍候")
                onRefresh()
            }
        })

        initData()
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["assetsType"] = "${balanceEnum.key}"
        map["pageNum"] = "$page"
        tagType?.let {
            map["tagType"] = "$it"
        }
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageSize"] = "$pageSize"
        when (balanceEnum) {
            BalanceEnum.YING_XING_GUO -> {
                requestWithError(
                    RetrofitCreateHelper.createApi(BaseApi::class.java).ginkgoFruitInfo(
                        map
                    ), {
                        var dateStr = ""
                        val datas = arrayListOf<ItemBalanceModel>()
                        it.data.forEach {
                            if (it.createDate != dateStr) {
                                dateStr = it.createDate
                                val itemBalanceModel = ItemBalanceModel()
                                itemBalanceModel.itemType = ITEM_TYPE_DATE
                                itemBalanceModel.createDate = it.createDate
                                datas.add(itemBalanceModel)
                            }
                            datas.add(it)
                        }
                        loadData(datas)
                    }) {
                    showLoadError()
                }
            }
            BalanceEnum.YING_XING_YE -> {
                requestWithError(
                    RetrofitCreateHelper.createApi(BaseApi::class.java).ginkgoLeafInfo(
                        map
                    ), {
                        var dateStr = ""
                        val datas = arrayListOf<ItemBalanceModel>()
                        it.data.forEach {
                            if (it.createDate != dateStr) {
                                dateStr = it.createDate
                                val itemBalanceModel = ItemBalanceModel()
                                itemBalanceModel.itemType = ITEM_TYPE_DATE
                                itemBalanceModel.createDate = it.createDate
                                datas.add(itemBalanceModel)
                            }
                            datas.add(it)
                        }
                        loadData(datas)
                    }) {
                    showLoadError()
                }
            }
            BalanceEnum.JI_FEN_SHANG_CHENG -> {
                requestWithError(
                    RetrofitCreateHelper.createApi(BaseApi::class.java).integralInfo(
                        map
                    ), {
                        var dateStr = ""
                        val datas = arrayListOf<ItemBalanceModel>()
                        it.data.forEach {
                            if (it.createDate != dateStr) {
                                dateStr = it.createDate
                                val itemBalanceModel = ItemBalanceModel()
                                itemBalanceModel.itemType = ITEM_TYPE_DATE
                                itemBalanceModel.createDate = it.createDate
                                datas.add(itemBalanceModel)
                            }
                            datas.add(it)
                        }
                        loadData(datas)
                    }) {
                    showLoadError()
                }
            }
            BalanceEnum.YUAN_LI_ZHI -> {
                requestWithError(
                    RetrofitCreateHelper.createApi(BaseApi::class.java).findPowerAvow(
                        map
                    ), {
                        loadData(it.data)
                    }) {
                    showLoadError()
                }
            }
            BalanceEnum.NAT -> {
                requestWithError(
                    RetrofitCreateHelper.createApi(BaseApi::class.java).natAssets(
                        map
                    ), {
                        loadData(it.data)
                    }) {
                    showLoadError()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showBalanceDialog?.dismiss()
    }
}

//            showBalanceDialog = DialogUtils.showBalanceDialog(this)
//        startActivity(Intent(this, TransferActivity::class.java))