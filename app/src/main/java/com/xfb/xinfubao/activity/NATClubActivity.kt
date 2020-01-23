package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.adapter.BalanceAdapter
import com.xfb.xinfubao.dialog.DialogUtils
import com.xfb.xinfubao.model.ItemBalanceModel
import kotlinx.android.synthetic.main.activity_natclub.*

/** NAT基金会 */
class NATClubActivity : BaseRecyclerViewActivity<ItemBalanceModel>() {

    val adapter = BalanceAdapter(list)

    var showDiYaDialog: AlertDialog? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_natclub
    }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<ItemBalanceModel, BaseViewHolder> {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initLogic() {
        //去抵押
        tvToDiYa.setOnClickListener {
            showDiYaDialog = DialogUtils.showDiYaDialog(this) {
                startActivity(Intent(this, ApplyCashOutResultActivity::class.java))
                showDiYaDialog?.dismiss()
            }
        }

        adapter.addHeaderView(LayoutInflater.from(this).inflate(R.layout.header_nat, null))
        initData()
    }

    override fun initData() {
        Handler().postDelayed({
            val datas = arrayListOf<ItemBalanceModel>()
            datas.add(ItemBalanceModel())
            datas.add(ItemBalanceModel())
            datas.add(ItemBalanceModel())
            datas.add(ItemBalanceModel())
            datas.add(ItemBalanceModel())
            datas.add(ItemBalanceModel())
            datas.add(ItemBalanceModel())
            loadData(datas)
        }, 1000)
    }

}
