package com.example.mycomic.ui.adapter;
import android.content.Context;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;
import com.example.mycomic.utils.TextUtils;

public class SearchResultAdapter extends BaseRecyclerAdapter<Comic> {
    private String key;
    public SearchResultAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }
    //设置关键字
    public void setKey(String key){
        this.key = key;
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        holder.setHtmlText(R.id.tv_title, TextUtils.getSearchText(key,item.getTitle()));
        holder.setHtmlText(R.id.tv_author,TextUtils.getSearchText(key,item.getAuthor()));
        holder.setText(R.id.tv_update,item.getUpdates());
        holder.setText(R.id.tv_tag,item.getTags().toString());
        holder.setImageByUrl(R.id.iv_cover,item.getCover());
    }
}
