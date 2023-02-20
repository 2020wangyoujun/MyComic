package com.example.mycomic.ui.view;

public interface ILoadDataView<T> extends IBaseView{
    void showErrorView(String throwable);

    void fillData(T data);

    void getDataFinish();

}
