package com.example.mycomic.ui.view;

import java.util.HashMap;

public interface ISelectDataView<T> extends ILoadDataView<T>{
    void updateList(HashMap map);
    void updateListItem(HashMap map,int position);
    void addAll();
    void removeAll();
    void quitEdit();
}
