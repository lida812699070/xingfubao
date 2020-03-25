package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ItemBalanceModel
import kotlinx.android.synthetic.main.activity_use_product.*

/** 使用产品 */
class UseProductActivity : DefaultActivity() {

    private var itemBalanceModel: ItemBalanceModel? = null

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
            DialogUtils.showUseApply(this)
        }
    }


}
