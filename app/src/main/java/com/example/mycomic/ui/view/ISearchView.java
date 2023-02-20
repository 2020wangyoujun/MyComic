package com.example.mycomic.ui.view;

public interface ISearchView<T> extends ILoadDataView<T>{
    void clearText();
    //动态搜索
    void fillDynamicResult(T t);
    //搜索结果
    void fillResult(T t);

    void fillTopSearch(T t);

    void setSearchText(String title);

    String getSearchText();
}
