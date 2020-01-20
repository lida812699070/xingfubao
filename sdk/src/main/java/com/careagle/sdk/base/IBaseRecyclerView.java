package com.careagle.sdk.base;


import java.util.List;

/**
 * Created by lida on 2018/1/15.
 */

public interface IBaseRecyclerView<T> extends IBaseView {
    int getPage();

    void showLoadError();

    void setData(int totalPage, List<T> list);
}
