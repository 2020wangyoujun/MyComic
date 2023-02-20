package com.example.mycomic.ui.view;


import com.example.mycomic.data.entity.Type;

import java.util.List;
import java.util.Map;


public interface ICategoryView<T> extends ILoadDataView<T>{
    void fillSelectData(List<Type> mList, Map<String,Integer> mMap);
    void setMap(Map<String,Integer> mMap);
}
