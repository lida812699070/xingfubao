package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ConfigDates
import com.xfb.xinfubao.model.NatActiveDetail
import com.xfb.xinfubao.model.event.NatKuangEvent
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setColorTextEnd
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_nat_kuang_detail.*
import org.greenrobot.eventbus.EventBus

/** 抢注矿主 矿基活动 */
class NatKuangDetailActivity : DefaultActivity() {
    //抢注矿主  矿基活动
    var type = 0
    var activityId = 0
    val dateList = arrayListOf<ConfigDates>()
    var data: NatActiveDetail? = null
    var incrementConfig: Long = -1L
    val dateAdapter = object :
        BaseQuickAdapter<ConfigDates, BaseViewHolder>(R.layout.item_nat_active_selected, dateList) {
        override fun convert(helper: BaseViewHolder, item: ConfigDates) {
            val tvItemText = helper.getView<TextView>(R.id.tvItemText)
            tvItemText.text = item.name
            tvItemText.isSelected = (item.id == incrementConfig)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_nat_kuang_detail
    }

    companion object {
        fun toActivity(context: Context, type: Int = 0, activityId: Int) {
            val intent = Intent(context, NatKuangDetailActivity::class.java)
            intent.putExtra("type", type)
            intent.putExtra("activityId", activityId)
            context.startActivity(intent)
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        type = intent.getIntExtra("type", 0)
        activityId = intent.getIntExtra("activityId", 0)
        myToolbar.setClick { finish() }
        myToolbar.setTitle(if (type == 1) "抢注矿主" else "矿基活动")
        initSelectedDate()

        val map = hashMapOf<String, String>()
        map["activityId"] = "$activityId"
        map["userId"] = "${ConfigUtils.userId()}"
        showProgress("请稍候")
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).activityDetail(map)) {
            bindData(it.data)
        }

        tvOk.setOnClickListener {
            activityJoin()
        }
        dateAdapter.setOnItemClickListener { adapter, view, position ->
            incrementConfig = dateList[position].id
            dateAdapter.notifyDataSetChanged()
        }
    }

    var showDialog: AlertDialog? = null
    /** 参与活动 */
    private fun activityJoin() {
        checkPayPassword {
            //非Nat
            if (1 == data?.inputType) {
                var natInputNum = 0.0
                try {
                    natInputNum = data!!.natInputNum.toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                showDialog =
                    DialogUtils.showNATInputDialog(this) { strNATCount, strPayPassword ->
                        if (strNATCount.toDouble() < natInputNum) {
                            showMessage("投资数量必须大于起投数量")
                            return@showNATInputDialog
                        }
                        joinActive(strNATCount, strPayPassword)
                    }
            } else if (2 == data?.inputType) {
                val strTitle = data?.natInputNum?.toString()
                showDialog = DialogUtils.showDiYaDialog(this, 5, title = strTitle) {
                    joinActive(null, it)
                    showDialog?.dismiss()
                }
            }
        }
    }

    /** 参与活动 */
    private fun joinActive(strNATCount: String?, strPayPassword: String) {
        val map = hashMapOf<String, String>()
        map["activityId"] = "$activityId"
        map["userId"] = "${ConfigUtils.userId()}"
        map["payPwd"] = strPayPassword
        if (!TextUtils.isEmpty(strNATCount)) {
            map["inputNum"] = "$strNATCount"
        }
        if (incrementConfig != -1L) {
            map["incrementConfig"] = "$strNATCount"
        }
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).activityJoin(map)) {
            showMessage("参与活动成功")
            EventBus.getDefault().post(NatKuangEvent())
            finish()
        }
    }

    private fun initSelectedDate() {
        recyclerViewSelectDate.layoutManager = LinearLayoutManager(this)
        recyclerViewSelectDate.adapter = dateAdapter
    }

    private fun bindData(data: NatActiveDetail?) {
        this.data = data
        tvView1Title.text = data?.activityName
        val selectColor = resources.getColor(R.color.theme_color)
        tvOpenObject.setColorTextEnd("开放对象：${data?.openObjects}", selectColor, 6)
        tvOpenProtectPrice.setColorTextEnd(
            "是否开启保值功能：${data?.hedgeStateDesc}", selectColor, 10
        )
        tvOpenAddPrice.setColorTextEnd("是否开启增值功能：${data?.incrementStateDesc}", selectColor, 10)
        tvProtectPrice.setColorTextEnd("保费税率：${data?.premiumRateDesc}", selectColor, 6)
        tvNATMinPrice.setColorTextEnd("NAT起投数量：${data?.natInputNum}", selectColor, 9)
        tvActiveWay.setColorTextEnd("活动方式：${data?.activityWay}", selectColor, 6)
        loadRule(data?.activityRules)
        tvOk.text = data?.confirmButtonText
        if (1 == data?.incrementState) {
            llSelectDate.setVisible(true)
            dateList.clear()
            if (data.incrementVos != null) {
                dateList.addAll(data.incrementVos)
                dateAdapter.notifyDataSetChanged()
            }
        }
    }

    fun loadRule(html: String?) {
        val webSettings = tvActiveRule.getSettings()
        webSettings.setJavaScriptEnabled(true)
//        webSettings.setUseWideViewPort(true) //将图片调整到适合webview的大小
//        webSettings.setLoadWithOverviewMode(true) // 缩放至屏幕的大小
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK) //关闭webview中缓存
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN)
//        webSettings.setDefaultTextEncodingName("utf-8")
        val imgStyle = "<html><body><style> img{ width:100%; height:auto;}</style>";
        val htmlStr = imgStyle + html + "</body></html>";
        tvActiveRule.loadDataWithBaseURL(null, htmlStr, "text/html", "utf-8", null);
    }

    override fun onDestroy() {
        super.onDestroy()
        showDialog?.dismiss()
    }


}
