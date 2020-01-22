package com.xfb.xinfubao.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import kotlinx.android.synthetic.main.activity_address_manager.*

//收货地址管理
class AddressManagerActivity : BaseRecyclerViewActivity<String>() {
    val adapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_address_manager, list) {
            override fun convert(helper: BaseViewHolder?, item: String?) {

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

    override fun pageAdapter(): BaseQuickAdapter<String, BaseViewHolder> {
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
        initData()
    }

    override fun initData() {
        Handler().postDelayed({
            val list = arrayListOf<String>()
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            list.add("")
            loadData(list)
        }, 1000)
    }


}
