package com.xfb.xinfubao.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.ReceiveVo
import com.xfb.xinfubao.utils.ConfigUtils
import com.xfb.xinfubao.utils.setVisible
import kotlinx.android.synthetic.main.activity_address_manager.*

//收货地址管理
class AddressManagerActivity : BaseRecyclerViewActivity<ReceiveVo>() {
    val adapter =
        object : BaseQuickAdapter<ReceiveVo, BaseViewHolder>(R.layout.item_address_manager, list) {
            override fun convert(helper: BaseViewHolder, item: ReceiveVo) {
                helper.setText(R.id.tvAddress, "${item.address} ${item.doorplate}")
                    .setText(R.id.tvName, "${item.consignee} ${item.phone}")
                    .addOnClickListener(R.id.ivEdit)
                helper.getView<ImageView>(R.id.ivDefault).setVisible(item.isDefault)
            }
        }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_address_manager
    }

    override fun pageRecyclerView(): RecyclerView {
        return recyclerView
    }

    override fun pageAdapter(): BaseQuickAdapter<ReceiveVo, BaseViewHolder> {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefreshLayout
    }

    override fun initLogic() {
        myToolbar.setClick { finish() }
        myToolbar.setRightClickStr("新增") {
            startActivity(Intent(this, AddAddressActivity::class.java))
        }
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.ivEdit -> {
                    startActivity(
                        Intent(this, AddAddressActivity::class.java)
                            .putExtra("data", list[position])
                    )
                }
            }
        }
        adapter.setOnItemClickListener { adapter, view, position ->
            setResult(Activity.RESULT_OK, Intent().putExtra("data", list[position]))
            finish()
        }
        initData()
    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["userId"] = "${ConfigUtils.userId()}"
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).findReceive(map), {
            loadData(it.data)
        }) {
            showLoadError()
        }
    }

    override fun onRestart() {
        super.onRestart()
        onRefresh()
    }

}
