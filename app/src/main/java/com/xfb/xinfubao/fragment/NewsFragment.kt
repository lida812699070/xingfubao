package com.xfb.xinfubao.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.careagle.sdk.helper.RetrofitCreateHelper
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xfb.xinfubao.R
import com.xfb.xinfubao.activity.WebviewActivity
import com.xfb.xinfubao.api.BaseApi
import com.xfb.xinfubao.constant.Constant
import com.xfb.xinfubao.model.NewsModel
import com.xfb.xinfubao.utils.DateUtils
import com.xfb.xinfubao.utils.loadUri

class NewsFragment : BaseRecyclerViewFragment<NewsModel>() {
    var noticeID: String? = null

    companion object {
        fun newInstance(noticeID: String?): NewsFragment {
            val newsFragment = NewsFragment()
            val bundle = Bundle()
            bundle.putString("noticeID", noticeID)
            newsFragment.arguments = bundle
            return newsFragment
        }
    }

    val adapter =
        object : BaseQuickAdapter<NewsModel, BaseViewHolder>(R.layout.item_find_news, list) {
            override fun convert(helper: BaseViewHolder, item: NewsModel) {
                val ivImages = helper.getView<ImageView>(R.id.ivImages)
                helper.setText(R.id.tvTitle, item.title)
                    .setText(R.id.tvNewsSource, item.source)
                    .setText(
                        R.id.tvDate,
                        DateUtils.stringToString(
                            item.createtime,
                            DateUtils.yyyy_MM_ddSFM,
                            DateUtils.YYYY_MM
                        )
                    )
                ivImages.loadUri(Constant.PIC_URL + item.iocurl)
            }
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

    override fun getLayoutId(): Int {
        return R.layout.fragment_news
    }


    override fun initData() {
        val map = hashMapOf<String, String>()
        map["noticeID"] = "$noticeID"
        map["pageNum"] = "$page"
        map["pageSize"] = "$pageSize"
        requestWithError(RetrofitCreateHelper.createApi(BaseApi::class.java).newslist(map), {
            showLoadError()
        }) {
            loadData(it.data)
        }
    }

    override fun initLogic() {
        noticeID = arguments?.getString("noticeID")
        initData()
        adapter.setOnItemClickListener { adapter, view, position ->
            activity?.let {
                WebviewActivity.newInstanceHtml(
                    it,
                    list[position].content,
                    list[position].title,
                    rightImage = R.mipmap.fenxiang_icon,
                    isNeedPadding = true
                )
            }
        }
    }


    override fun initUI(view: View?, savedInstanceState: Bundle?) {

    }

}