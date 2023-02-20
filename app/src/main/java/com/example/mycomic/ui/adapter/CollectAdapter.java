package com.example.mycomic.ui.adapter;
import android.content.Context;
import android.view.View;

import com.example.mycomic.R;
import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.ui.activity.base.BookShelfAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;


public class CollectAdapter extends BookShelfAdapter<Comic> {

    public CollectAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        if(item!=null){
            if(!isEditing){
                holder.setVisibility(R.id.iv_image_select, View.GONE);
            }else{
                if(mMap.size()!=0&&mMap.get(position) == Constants.CHAPTER_SELECTED){
                    holder.setImageResource(R.id.iv_image_select,R.mipmap.item_selected);
                }else{
                    holder.setImageResource(R.id.iv_image_select,R.mipmap.item_select);
                }
                holder.setVisibility(R.id.iv_image_select, View.VISIBLE);
            }
            holder.setText(R.id.tv_title,item.getTitle());
            holder.setImageByUrlNone(R.id.iv_image,item.getCover());
            if(item.getChapters()!=null&&item.getChapters().size()!=0){
                holder.setText(R.id.tv_index_reader,item.getChapters().size()+"话"+"/"+(item.getCurrentChapter()+1)+"话");
            }
        }
    }
}
