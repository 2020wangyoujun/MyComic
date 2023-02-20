package com.example.mycomic.ui.activity.base;
import android.content.Context;

import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;

import java.util.HashMap;
import java.util.List;

public abstract class BookShelfAdapter<T> extends BaseRecyclerAdapter<T> {
    protected boolean isEditing;
    protected HashMap<Integer,Integer> mMap;

    public HashMap<Integer, Integer> getmMap() {
        return mMap;
    }

    public void setmMap(HashMap<Integer, Integer> mMap) {
        this.mMap = mMap;
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }

    public BookShelfAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mMap = new HashMap<>();
    }

    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position, List<Object> payloads) {
        if(payloads.isEmpty()){
            onBindViewHolder(holder,position);
        }else{
            convert(holder, list.get(position), position);
        }
    }
}
