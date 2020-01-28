package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ImageView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ExchangeModel
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setInVisible
import kotlinx.android.synthetic.main.activity_money_exchange.*

/** 资产互兑 */
class MoneyExchangeActivity : DefaultActivity() {
    var leftSelect: ExchangeModel? = null
    var rightSelect: ExchangeModel? = null
    val leftList = arrayListOf<ExchangeModel>()
    val rightList = arrayListOf<ExchangeModel>()
    val leftAdapter = object :
        BaseQuickAdapter<ExchangeModel, BaseViewHolder>(R.layout.item_exchange, leftList) {
        override fun convert(helper: BaseViewHolder, item: ExchangeModel) {
            helper.setText(R.id.tvYXY, item.assetsName)
            val ivRight = helper.getView<ImageView>(R.id.ivRight)
        }
    }
    val rightAdapter = object :
        BaseQuickAdapter<ExchangeModel, BaseViewHolder>(R.layout.item_exchange, rightList) {
        override fun convert(helper: BaseViewHolder, item: ExchangeModel) {
            helper.setText(R.id.tvYXY, item.assetsName)
            val ivRight = helper.getView<ImageView>(R.id.ivRight)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_money_exchange
    }

    override fun initView(savedInstanceState: Bundle?) {
        myToolbar.setClick { finish() }
        //TODO 帮助
        myToolbar.setRightClickStr("帮助") {

        }

        val map = hashMapOf<String, String>()
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).findAssetsType(map)) {
            leftSelect = it.data[0]
            leftList.clear()
            leftList.addAll(it.data)
            requestRight()
        }

        etExchangeOutCount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                countExchange()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })

        tvToExchange.setOnClickListener {
            toExchange()
        }

        initRecyclerView()

        tvYXG.setOnClickListener {
            recyclerViewLeft.setInVisible(true)
        }

        tvYXY.setOnClickListener {
            recyclerViewRight.setInVisible(true)
        }

        tvPleaseSelect.setOnClickListener {
            recyclerViewRight.setInVisible(true)
        }
    }

    private fun requestRight() {
        val params = hashMapOf<String, String>()
        params["assetsId"] = "${leftSelect?.assetsId}"
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).findExchangeConfig(params)) {
            if (it.data.isEmpty()) {
                rightSelect = null
                tvPleaseSelect.setInVisible(true)
                tvYXY.setInVisible(false)
            } else {
                rightSelect = it.data[0]
                bindData()
                tvPleaseSelect.setInVisible(false)
                tvYXY.setInVisible(true)
            }
        }
    }

    private fun initRecyclerView() {
        recyclerViewLeft.layoutManager = LinearLayoutManager(this)
        recyclerViewLeft.adapter = leftAdapter
        leftAdapter.setOnItemClickListener { adapter, view, position ->
            leftSelect = leftList[position]
            tvYXG.text = leftSelect?.assetsName
            requestRight()
            recyclerViewLeft.setInVisible(false)
        }
        recyclerViewRight.layoutManager = LinearLayoutManager(this)
        recyclerViewRight.adapter = rightAdapter
        rightAdapter.setOnItemClickListener { adapter, view, position ->
            rightSelect = rightList[position]
            bindData()
            recyclerViewRight.setInVisible(false)
            tvPleaseSelect.setInVisible(false)
            tvYXY.setInVisible(true)
        }

    }

    private fun countExchange() {
        if (leftSelect == null || rightSelect == null) {
            return
        }
        val strCashOutCount = etExchangeOutCount.text.toString()
        if (TextUtils.isEmpty(strCashOutCount)) {
            tvExchangeInCount.text = "收到数量"
            tvExchangeInCount.setTextColor(resources.getColor(R.color.color_text_888))
        } else {
            tvExchangeInCount.setTextColor(resources.getColor(R.color.color_text_111))
            tvExchangeInCount.text =
                PriceChangeUtils.getDoubleKb(strCashOutCount.toDouble() * rightSelect!!.ratio)
        }
    }

    @SuppressLint("SetTextI18n")
    fun bindData() {
        if (leftSelect == null || rightSelect == null) {
            return
        }
        tvYXG.text = leftSelect?.assetsName
        tvYXY.text = rightSelect?.assetsName
        tvExchangeRatioText.text = "${leftSelect?.assetsName}兑换${rightSelect?.assetsName}的比例为"
        tvExchangeRatio.text = "1:${PriceChangeUtils.getDoubleKb(rightSelect!!.ratio)}"
        countExchange()

    }

    private fun toExchange() {
        if (leftSelect == null || rightSelect == null) {
            return
        }
        if (TextUtils.isEmpty(etExchangeOutCount.text.toString())) {
            showMessage("请输入转出数量")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["assetsConfigId"] = "${rightSelect!!.assetsConfigId}"
        map["turnOutQuantity"] = "${etExchangeOutCount.text}"
        map["intoQuantity"] = "${tvExchangeInCount.text}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).assetsExchange(map)) {
            showMessage("兑换成功")
            etExchangeOutCount.setText("")
            bindData()
        }
    }

}
