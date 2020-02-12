package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.WebviewActivity
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.model.NewsModel

/** 公告 */
class PublishFragment : BaseRecyclerViewFragment<NewsModel>() {
    private var typeId: Int = 0
    val adapter =
        object : BaseQuickAdapter<NewsModel, BaseViewHolder>(R.layout.item_publish_message, list) {
            override fun convert(helper: BaseViewHolder, item: NewsModel) {
                helper.setText(R.id.tvTitle, item.title)
                    .setText(R.id.tvTime, item.createtime)
                    .setText(R.id.tvContent, item.content)
            }
        }

    companion object {
        fun newInstance(typeId: Int): PublishFragment {
            val fragmentLogin = PublishFragment()
            val bundle = Bundle()
            bundle.putInt("typeId", typeId)
            fragmentLogin.arguments = bundle
            return fragmentLogin
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_publish_message
    }

    override fun pageRecyclerView(): RecyclerView? {
        return findViewById(R.id.recyclerView) as RecyclerView?
    }

    override fun pageAdapter(): BaseQuickAdapter<NewsModel, BaseViewHolder>? {
        return adapter
    }

    override fun pageSwipeRefreshLayout(): SwipeRefreshLayout? {
        return findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout?
    }

    override fun initUI(view: View?, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        val map = hashMapOf<String, String>()
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        map["typeID"] = "$typeId"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).noticelistxfb(map), {
            showLoadError()
        }, {
            loadData(it.data)
        })
    }

    override fun initLogic() {
        typeId = arguments!!.getInt("typeId", 0)
        adapter.setOnItemClickListener { adapter, view, position ->
            activity?.let {
                val map = hashMapOf<String, String>()
                map["id"] = "${list[position].id}"
                request(RetrofitCreateHelper.createApi(BaseApi::class.java).noticedetails(map)) {
                    WebviewActivity.newInstanceHtml(
                        activity!!,
                        it.data.content,
                        it.data.title,
                        isNeedPadding = true,
                        isPublish = true
                    )
                }
            }
        }
        initData()
    }
}