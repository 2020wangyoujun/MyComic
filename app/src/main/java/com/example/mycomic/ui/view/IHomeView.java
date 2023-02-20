package com.example.mycomic.ui.view;

import com.example.mycomic.data.entity.BaseBean;

import java.util.List;

public interface IHomeView<T extends BaseBean> extends IBaseView {
    /**
     * 添加更多数据（用于刷新）
     * @param data
     */
    void appendMoreDataToView(List<T> data);

    /**
     * 没有更多
     */
    void hasNoMoreData();

    void showErrorView(String throwable);

    void fillData(List<T> data);

    void getDataFinish();

    void onRefreshFinish();

    void fillBanner(List<T> data);

    void fillRecent(String str);

}
