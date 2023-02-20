package com.example.mycomic.ui.view;
import java.util.HashMap;

public interface IDownloadlistView<T> extends ILoadDataView<T>{
    void onLoadMoreData(T t);
    void updateView(int postion);
    void onDownloadFinished();
    void onPauseOrStartAll();


    void updateList(HashMap map);
    void updateListItem(HashMap map,int position);
    void addAll();
    void removeAll();
    void quitEdit();
}
