package com.example.mycomic.ui.adapter;
import android.content.Context;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;

public class SearchHistoryAdapter extends BaseRecyclerAdapter<Comic> {
    public SearchHistoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    public void onClear(){
        this.list.clear();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setText(R.id.tv_history_search, item.getTitle());
    }
}
