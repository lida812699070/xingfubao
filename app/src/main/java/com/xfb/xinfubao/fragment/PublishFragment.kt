package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R

/** 公告 */
class PublishFragment : BaseRecyclerViewFragment<String>() {

    val adapter =
        object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_publish_message, list) {
            override fun convert(helper: BaseViewHolder?, item: String?) {

            }
        }

    override fun getLayoutId(): Int {
        return R.layout.fragment_publish_message
    }

    override fun pageRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView) as RecyclerView?
    }

    override fun pageAdapter(): BaseQuickAdapter<String, BaseViewHolder>? {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout?
    }

    override fun initData() {
        Handler().postDelayed({
            val datas = arrayListOf<String>()
            datas.add("")
            datas.add("")
            datas.add("")
            datas.add("")
            datas.add("")
            datas.add("")
            datas.add("")
            datas.add("")
            loadData(datas)
        }, 1000)
    }

    override fun initLogic() {
        initData()
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {

    }
}