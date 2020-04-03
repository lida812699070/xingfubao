package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import android.widget.TextView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.YinXinBaoUseProductModel
import com.xfb.xinfubao.model.event.EventTransfer
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_use_product.*
import org.greenrobot.eventbus.EventBus

/** 使用产品 */
class UseProductActivity : DefaultActivity() {

    private var itemBalanceModel: ItemBalanceModel? = null
    private var showDiYaDialog: AlertDialog? = null
    val list = arrayListOf<YinXinBaoUseProductModel>()
    var selectIndex = -1
    private val adapter =
        object : BaseQuickAdapter<YinXinBaoUseProductModel, BaseViewHolder>(
            R.layout.item_use_product,
            list
        ) {
            override fun convert(helper: BaseViewHolder, item: YinXinBaoUseProductModel) {
                val tvProductName = helper.getView<TextView>(R.id.tvProductName)
                tvProductName.isSelected = helper.adapterPosition == selectIndex
                tvProductName.text = item.name
            }
        }

    companion object {
        fun toActivity(context: Context, itemBalanceModel: ItemBalanceModel?) {
            val intent = Intent(context, UseProductActivity::class.java)
            intent.putExtra("itemBalanceModel", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_use_product
    }

    override fun initView(savedInstanceState: Bundle?) {
        itemBalanceModel = intent.getSerializableExtra("itemBalanceModel") as ItemBalanceModel?
        myToolbar.setClick { finish() }
        tvOk.setOnClickListener {
            toUse()
        }
        initRecyclerView()
        val map = hashMapOf<String, String>()
        request(RetrofitCreateHelper.createApi(BaseApi::class.java).natMakeUseOfProduct(map)) {
            list.clear()
            list.addAll(it.data)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener { adapter, view, position ->
            selectIndex = position
            adapter.notifyDataSetChanged()
        }
    }

    private fun toUse() {
        val userName = etName.text.toString()
        val idNo = etIDCard.text.toString()
        val etContent = etGongFenContent.text.toString()
        if (TextUtils.isEmpty(userName)) {
            showMessage("请输入供奉人名字")
            return
        }
        if (TextUtils.isEmpty(etContent)) {
            showMessage("请输入供奉内容")
            return
        }
        if (selectIndex == -1) {
            showMessage("请选择使用产品")
            return
        }
        showDiYaDialog = DialogUtils.showDiYaDialog(this, 4) {
            showDiYaDialog?.dismiss()
            val map = hashMapOf<String, String>()
            map["orderNo"] = "${itemBalanceModel?.orderNum}"
            map["userId"] = "${ConfigUtils.userId()}"
            map["worshipProduct"] = "${list[selectIndex].id}"
            map["payPwd"] = it
            map["userName"] = userName
            if (!TextUtils.isEmpty(idNo)) {
                map["idNo"] = idNo
            }
            map["worshipContent"] = etContent
            request(RetrofitCreateHelper.createApi(BaseApi::class.java).natMakeUseOf(map)) {
                val showUseApply = DialogUtils.showUseApply(this)
                showUseApply.setOnDismissListener {
                    EventBus.getDefault().post(EventTransfer())
                    finish()
                }
            }
        }
    }


}
