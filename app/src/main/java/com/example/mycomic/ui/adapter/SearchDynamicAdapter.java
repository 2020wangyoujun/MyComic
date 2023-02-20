package com.example.mycomic.ui.adapter;
import android.content.Context;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;
import com.example.mycomic.utils.TextUtils;

public class SearchDynamicAdapter extends BaseRecyclerAdapter<Comic> {
    private String key;
    public void setKey(String key){
        this.key = key;
    }
    public SearchDynamicAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setHtmlText(R.id.tv_dynamic_search, TextUtils.getSearchText(key,item.getTitle()));
    }
}
