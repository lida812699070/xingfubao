package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.NatUnlockPakeageModel
import com.xfb.xinfubao.model.event.ZhiYaEvent
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setColorText
import kotlinx.android.synthetic.main.activity_to_exchange.*
import org.greenrobot.eventbus.EventBus

/** 去置换 */
class ToExchangeActivity : DefaultActivity() {
    val list = arrayListOf<NatUnlockPakeageModel>()
    private var itemBalanceModel: ItemBalanceModel? = null
    var selectPosition = 0
    val adapter = object :
        BaseQuickAdapter<NatUnlockPakeageModel, BaseViewHolder>(R.layout.item_to_exchange, list) {
        override fun convert(helper: BaseViewHolder, item: NatUnlockPakeageModel) {
            val tvSelect = helper.getView<TextView>(R.id.tvSelect60)
            tvSelect.text = item.name
            tvSelect.isSelected = selectPosition == helper.adapterPosition
            val str = "可获得的NAT数量：${PriceChangeUtils.getNumKb(list[selectPosition].unlockNum)}"
            tvCanGetNatCount.setColorText(
                str,
                resources.getColor(R.color.color_light_org),
                10,
                str.length
            )
        }
    }

    companion object {
        fun toActivity(
            context: Context,
            itemBalanceModel: ItemBalanceModel
        ) {
            val intent = Intent(context, ToExchangeActivity::class.java)
            intent.putExtra("itemBalanceModel", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_to_exchange
    }

    override fun initView(savedInstanceState: Bundle?) {
        itemBalanceModel = intent.getSerializableExtra("itemBalanceModel") as ItemBalanceModel?
        ivFinish.setOnClickListener { finish() }
        initRecyclerView()
        tvOk.setOnClickListener {
            toExchange()
        }
        loadData()
    }

    private fun loadData() {
        val map = hashMapOf<String, String>()
        map["recordId"] = "${itemBalanceModel?.id}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).getNatUnlockPackage(map)) {
            list.clear()
            list.addAll(it.data)
            adapter.notifyDataSetChanged()
        }
    }

    //去置换
    private fun toExchange() {
        if (list.isEmpty()) {
            return
        }
        val natUnlockPakeageModel = list[selectPosition]
        val strPassword = etPayPassword.text.toString()
        if (TextUtils.isEmpty(strPassword)) {
            showMessage("请输入支付密码")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["unlockPackageId"] = "${natUnlockPakeageModel.id}"
        map["ordernum"] = "${itemBalanceModel?.orderNum}"
        map["orderId"] = "${itemBalanceModel?.id}"
        map["payPassword"] = strPassword
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).mortgageNat(map)) {
            showMessage(it.msg)
            EventBus.getDefault().post(ZhiYaEvent())
            finish()
        }

    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter.setOnItemClickListener { adapter, view, position ->
            selectPosition = position
            adapter.notifyDataSetChanged()
        }
        recyclerView.adapter = adapter
    }

}
