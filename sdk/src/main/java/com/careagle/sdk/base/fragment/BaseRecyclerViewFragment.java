package com.careagle.sdk.base.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.careagle.sdk.R;
import com.careagle.sdk.base.IBaseRecyclerView;
import com.careagle.sdk.weight.EmptyView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lida on 2018/1/15.
 */

public abstract class BaseRecyclerViewFragment<T> extends BaseCompatFragment implements IBaseRecyclerView<T>, SwipeRefreshLayout.OnRefreshListener, EmptyView.ReloadListener, BaseQuickAdapter.RequestLoadMoreListener {

    private int initialPage = 1;
    protected int page = initialPage;
    protected int pageSize = 10;
    protected EmptyView emptyView;
    protected ArrayList<T> list;

    public abstract RecyclerView getPageRecyclerView();

    public abstract BaseQuickAdapter getAdapter();

    public abstract SwipeRefreshLayout getPageSwipeRefreshLayout();

    public EmptyView getEmptyView() {
        return emptyView;
    }

    public List<T> getList() {
        return list;
    }

    public abstract void initData();

    public abstract void initLogic();

    @Override
    public void initUI(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
    }

    public void initRecyclerView() {
        list = new ArrayList<>();
        initLogic();
        getPageSwipeRefreshLayout().setColorSchemeColors(getResources().getColor(R.color.theme_color));
        getPageSwipeRefreshLayout().setOnRefreshListener(this);
        getPageRecyclerView().setLayoutManager(getLinearLayoutManager() == null ? new LinearLayoutManager(getActivity()) : getLinearLayoutManager());
        getPageRecyclerView().setAdapter(getAdapter());
        emptyView = new EmptyView(getActivity());
        getAdapter().setEmptyView(getEmptyView());
        getAdapter().setHeaderAndEmpty(isEmptyAndHeader());
        if (isCanLoadMore()) {
            getAdapter().setOnLoadMoreListener(this, getPageRecyclerView());
        }
        getEmptyView().setLoadState(EmptyView.LoadState.LOAD_STATE_LOADING);
        getEmptyView().setReloadListener(this);
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return null;
    }

    public boolean isEmptyAndHeader() {
        return false;
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = initialPage;
        initData();
    }

    /**
     * 是否可以上啦加载更多
     *
     * @return
     */
    public boolean isCanLoadMore() {
        return true;
    }

    /**
     * 加载数据
     *
     * @param totalPage 总页数
     * @param data      数据
     */
    public void setData(int totalPage, List<T> data) {
        if (page == initialPage) getList().clear();
        if (data != null) {
            getList().addAll(data);
        }
        //page < totalPage &&
        if (isCanLoadMore() && (data != null && data.size() == pageSize)) {
            getAdapter().loadMoreComplete();
        } else {
            getAdapter().loadMoreEnd();
        }
        if (getPageSwipeRefreshLayout() != null)
            getPageSwipeRefreshLayout().setRefreshing(false);
        if (list.size() == 0) {
            getEmptyView().setLoadState(EmptyView.LoadState.LOAD_STATE_EMPTY);
        }
        getAdapter().notifyDataSetChanged();
    }

    /**
     * 加载失败  如果第一页就失败  那就显示用布局的错误
     */
    @Override
    public void showLoadError() {
        getAdapter().loadMoreFail();
        if (getPageSwipeRefreshLayout() != null) {
            getPageSwipeRefreshLayout().setRefreshing(false);
        }
        if (page > initialPage) {
            page--;
        }
        if (getList().size() == 0) {
            getEmptyView().setLoadState(EmptyView.LoadState.LOAD_STATE_ERROR);
        }
    }

    /**
     * 上拉加载更多
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        initData();
    }

    /**
     * 点击空布局加载失败
     */
    @Override
    public void onReload() {
        onRefresh();
    }
}
