package com.example.mycomic.ui.adapter;
import android.content.Context;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;
import com.example.mycomic.utils.TextUtils;

public class SearchTopAdapter extends BaseRecyclerAdapter<Comic> {
    private String key;
    public SearchTopAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_top_search, TextUtils.getSearchText(key,item.getTitle()));
    }
}
