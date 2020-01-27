package com.xfb.xinfubao.activity

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.careagle.sdk.R
import com.careagle.sdk.weight.EmptyView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import java.util.*

/**
 * 默认的RecyclerView
 */
abstract class BaseRecyclerViewActivity<T> : DefaultActivity(),
    SwipeRefreshLayout.OnRefreshListener,
    BaseQuickAdapter.RequestLoadMoreListener, EmptyView.ReloadListener {

    //初始页数
    protected val initialPage = 0
    protected var page = initialPage
    protected var pageSize = 10
    protected var emptyView: EmptyView? = null
    protected var list = ArrayList<T>()

    abstract fun pageRecyclerView(): RecyclerView

    abstract fun pageAdapter(): BaseQuickAdapter<T, BaseViewHolder>

    abstract fun pageSwipeRefreshLayout(): SwipeRefreshLayout?

    abstract fun initLogic()

    open fun pageEmptyView(): EmptyView? {
        if (emptyView == null) {
            emptyView = EmptyView(this)
        }
        return emptyView
    }

    fun pageList(): ArrayList<T> {
        return list
    }

    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        initLogic()
        emptyView = pageEmptyView()
        pageSwipeRefreshLayout()?.setColorSchemeColors(resources.getColor(R.color.theme_color))
        pageSwipeRefreshLayout()?.setOnRefreshListener(this)
        pageRecyclerView().layoutManager = pageLayoutManager()
        pageRecyclerView().adapter = pageAdapter()
        pageAdapter().emptyView = emptyView
        pageAdapter().setHeaderAndEmpty(true)
        if (isCanLoadMore()) {
            pageAdapter().setOnLoadMoreListener(this, pageRecyclerView())
        }
        emptyView?.setLoadState(EmptyView.LoadState.LOAD_STATE_LOADING)
        emptyView?.setReloadListener(this)
    }

    fun loadData(data: List<T>, isEndPage: Boolean? = null) {
        if (page == initialPage) list.clear()
        list.addAll(data)
        var canLoadMore = isCanLoadMore() && (isEndPage ?: (data.size >= pageSize))
        if (isEndPage != null) {
            canLoadMore = !isEndPage
        }
        if (canLoadMore) {
            pageAdapter().loadMoreComplete()
        } else {
            pageAdapter().loadMoreEnd()
        }
        pageSwipeRefreshLayout()?.isRefreshing = false
        if (list.size == 0) {
            emptyView?.setLoadState(EmptyView.LoadState.LOAD_STATE_EMPTY)
        }
        pageAdapter().notifyDataSetChanged()
        if (page == 0) {
            pageRecyclerView().scrollToPosition(0)
        }
    }

    /**
     * 是否可以上啦加载更多
     *
     * @return
     */
    open fun isCanLoadMore(): Boolean {
        return true
    }

    /**
     * 加载失败  如果第一页就失败  那就显示用布局的错误
     */
    fun showLoadError() {
        pageAdapter().loadMoreFail()
        if (pageSwipeRefreshLayout() != null) {
            pageSwipeRefreshLayout()?.isRefreshing = false
        }
        if (page > 0) {
            page--
        }
        if (list.size == 0) {
            emptyView?.setLoadState(EmptyView.LoadState.LOAD_STATE_ERROR)
        }
    }

    override fun onRefresh() {
        page = initialPage
        initData()
    }

    override fun onLoadMoreRequested() {
        page++
        initData()
    }

    override fun onReload() {
        onRefresh()
    }

    open fun pageLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

}