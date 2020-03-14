package com.xfb.xinfubao.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.xfb.xinfubao.R
import com.xfb.xinfubao.model.ItemBalanceModel

/** 申请质押 */
class ApplyZhiYaActivity : DefaultActivity() {
    var itemBalanceModel: ItemBalanceModel? = null

    companion object {
        fun toActivity(
            context: Context,
            itemBalanceModel: ItemBalanceModel
        ) {
            val intent = Intent(context, ApplyZhiYaActivity::class.java)
            intent.putExtra("itemBalanceModel", itemBalanceModel)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_zhi_ya
    }

    override fun initView(savedInstanceState: Bundle?) {
        itemBalanceModel = intent.getSerializableExtra("itemBalanceModel") as ItemBalanceModel?

    }
}
