package com.example.mycomic.ui.view;


public interface IDetailView<T> extends  ILoadDataView<T>{
    void OrderData(int ResId);
    void setCollect(boolean isCollect);
    void setCurrent(int current);
}
