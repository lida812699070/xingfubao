package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ItemBalanceModel
import com.xfb.xinfubao.model.event.EventTransfer
import com.xfb.xinfubao.utils.ConfigUtils
import kotlinx.android.synthetic.main.activity_use_product.*
import org.greenrobot.eventbus.EventBus

/** 使用产品 */
class UseProductActivity : DefaultActivity() {

    private var itemBalanceModel: ItemBalanceModel? = null
    private var showDiYaDialog: AlertDialog? = null

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
    }

    private fun toUse() {
        val userName = etName.text.toString()
        val idNo = etIDCard.text.toString()
        val etContent = etGongFenContent.text.toString()
        if (TextUtils.isEmpty(userName)) {
            showMessage("请输入供奉人名字")
            return
        }
        if (TextUtils.isEmpty(idNo)) {
            showMessage("请输入供奉人身份证号码")
            return
        }
        if (TextUtils.isEmpty(etContent)) {
            showMessage("请输入供奉内容")
            return
        }
        showDiYaDialog = DialogUtils.showDiYaDialog(this, 4) {
            showDiYaDialog?.dismiss()
            val map = hashMapOf<String, String>()
            map["orderNo"] = "${itemBalanceModel?.orderNum}"
            map["userId"] = "${ConfigUtils.userId()}"
            map["payPwd"] = it
            map["userName"] = userName
            map["idNo"] = idNo
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
