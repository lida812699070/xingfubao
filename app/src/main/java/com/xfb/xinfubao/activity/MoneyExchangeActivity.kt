package com.xfb.xinfubao.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.ImageView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.careagle.sdk.utils.PriceChangeUtils
import com.careagle.sdk.weight.EmptyView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ExchangeModel
import com.xfb.xinfubao.myenum.BalanceEnum
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.loadUri
import com.xfb.xinfubao.utils.setInVisible
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_money_exchange.*

/** 资产互兑 */
class MoneyExchangeActivity : DefaultActivity() {
    var leftSelect: ExchangeModel? = null
    var rightSelect: ExchangeModel? = null
    val leftList = arrayListOf<ExchangeModel>()
    val rightList = arrayListOf<ExchangeModel>()
    var showDiYaDialog: AlertDialog? = null
    var balanceEnum: BalanceEnum? = null
    val leftAdapter = object :
        BaseQuickAdapter<ExchangeModel, BaseViewHolder>(R.layout.item_exchange, leftList) {
        override fun convert(helper: BaseViewHolder, item: ExchangeModel) {
            helper.setText(R.id.tvYXY, item.assetsName)
            val ivRight = helper.getView<ImageView>(R.id.ivRight)
            ivRight.loadUri(Constant.PIC_URL + item.assetsIcon)
        }
    }
    val rightAdapter = object :
        BaseQuickAdapter<ExchangeModel, BaseViewHolder>(R.layout.item_exchange, rightList) {
        override fun convert(helper: BaseViewHolder, item: ExchangeModel) {
            helper.setText(R.id.tvYXY, item.assetsName)
            val ivRight = helper.getView<ImageView>(R.id.ivRight)
            ivRight.loadUri(Constant.PIC_URL + item.assetsIcon)
        }
    }

    companion object {
        fun toActivity(context: Context, enum: BalanceEnum) {
            val intent = Intent(context, MoneyExchangeActivity::class.java)
            intent.putExtra("enum", enum)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_money_exchange
    }

    override fun initView(savedInstanceState: Bundle?) {
        balanceEnum = intent.getSerializableExtra("enum") as BalanceEnum?
        myToolbar.setClick { finish() }

        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).findAssetsType(map)) {
            if (it.data.isEmpty()) {
                emptyView.setVisible(true)
                emptyView.setLoadState(EmptyView.LoadState.LOAD_STATE_EMPTY)
                emptyView.setEmptyStr("没有可兑换的资产")
            } else {
                leftSelect = it.data[0]
                if (balanceEnum != null) {
                    it.data.forEach {
                        if (it.assetsId == 104) {
                            leftSelect = it
                        }
                    }
                }
                leftList.clear()
                leftList.addAll(it.data)
                bindLeft()
                requestRight()
                emptyView.setVisible(false)
            }

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
            checkPayPassword {
                showDiYaDialog = DialogUtils.showDiYaDialog(this, 1) {
                    showDiYaDialog?.hide()
                    toExchange(it)
                }
            }
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
                ivIRight.setInVisible(false)
                tvPleaseSelect.setInVisible(true)
                tvYXY.setInVisible(false)
                tvExchangeRatio.setInVisible(false)
                tvExchangeRatioText.setInVisible(false)
            } else {
                rightSelect = it.data[0]
                rightList.clear()
                rightList.addAll(it.data)
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
            bindLeft()
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
            tvExchangeRatioText.setInVisible(false)
            tvExchangeRatio.setInVisible(false)
            return
        }
        tvExchangeRatioText.setVisible(true)
        tvExchangeRatio.setVisible(true)
        bindLeft()
        tvYXY.text = rightSelect?.assetsName
        ivIRight.loadUri(Constant.PIC_URL + rightSelect?.assetsIcon)
        ivIRight.setVisible(true)
        tvExchangeRatioText.text = "${leftSelect?.assetsName}兑换${rightSelect?.assetsName}的比例为"
        tvExchangeRatio.text = "1:${PriceChangeUtils.getDoubleKb(rightSelect!!.ratio)}"
        countExchange()

    }

    private fun bindLeft() {
        tvYXG.text = leftSelect?.assetsName
        ivLeft.loadUri(Constant.PIC_URL + leftSelect?.assetsIcon)
        ivLeft.setVisible(true)
    }

    private fun toExchange(pwd: String) {
        if (leftSelect == null || rightSelect == null) {
            return
        }
        if (TextUtils.isEmpty(etExchangeOutCount.text.toString())) {
            showMessage("请输入转出数量")
            return
        }
        if (leftSelect!!.assetsQuantity < etExchangeOutCount.text.toString().toDouble()) {
            showMessage("余额不足")
            return
        }
        val map = hashMapOf<String, String>()
        map["userId"] = "${ConfigUtils.userId()}"
        map["assetsConfigId"] = "${rightSelect!!.assetsConfigId}"
        map["turnOutQuantity"] = "${etExchangeOutCount.text}"
        map["intoQuantity"] = "${tvExchangeInCount.text}"
        map["payPwd"] = pwd
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).assetsExchange(map)) {
            showMessage("兑换成功")
            etExchangeOutCount.setText("")
            bindData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        showDiYaDialog?.dismiss()
    }
}
