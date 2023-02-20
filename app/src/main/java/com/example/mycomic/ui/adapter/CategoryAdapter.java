package com.example.mycomic.ui.adapter;

import android.content.Context;
import android.view.View;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Type;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;

import java.util.HashMap;
import java.util.Map;

public class CategoryAdapter extends BaseRecyclerAdapter<Type> {
    private Map<String,Integer> mSelectMap;

    public void setSelectMap(Map<String, Integer> mSelectMap) {
        this.mSelectMap = mSelectMap;
        notifyDataSetChanged();
    }

    public CategoryAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        mSelectMap = new HashMap<>();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Type item, int position) {
        if(item.getValue()==mSelectMap.get(item.getType())){
            holder.setVisibility(R.id.iv_title_bg, View.VISIBLE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextPrimary);
//            }else{
//                holder.setTextViewColor(R.id.tv_title, ContextCompat.getColor(context,R.color.colorPrimary));
//            }
            holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextPrimary);
        }else{
            holder.setVisibility(R.id.iv_title_bg, View.GONE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextBlack);
//            }else{
//                holder.setTextViewColor(R.id.tv_title, ContextCompat.getColor(context,R.color.colorTextBlack));
//            }
            holder.setTextViewAppearanceColor(R.id.tv_title,R.style.colorTextBlack);
        }
        holder.setText(R.id.tv_title, item.getTitle());
    }
}
