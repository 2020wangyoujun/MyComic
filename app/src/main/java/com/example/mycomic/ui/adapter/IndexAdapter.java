package com.example.mycomic.ui.adapter;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.example.mycomic.R;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;

public class IndexAdapter extends BaseRecyclerAdapter<String> {
    private Context mContext;
    public boolean isOrder = true;
    private int mCurrent;

    public IndexAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
        this.mContext = context;
    }

    public boolean isOrder() {
        return isOrder;
    }

    public void setOrder(boolean order) {
        isOrder = order;
        notifyDataSetChanged();
    }

    public void setCurrent(int current){
        this.mCurrent = current;
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        if(!isOrder){
            position = list.size() - position -1;
        }
        holder.setText(R.id.tv_title,list.get(position));
        holder.setText(R.id.tv_position,(position+1)+" - ");
        if(position == mCurrent){
            holder.setVisibility(R.id.iv_location, View.VISIBLE);
            holder.setTextViewColor(R.id.tv_title, ContextCompat.getColor(mContext,R.color.colorPrimary));
            holder.setTextViewColor(R.id.tv_position,ContextCompat.getColor(mContext,R.color.colorPrimary));
        }else{
            holder.setVisibility(R.id.iv_location, View.GONE);
            holder.setTextViewColor(R.id.tv_title, ContextCompat.getColor(mContext,R.color.colorWhite));
            holder.setTextViewColor(R.id.tv_position,ContextCompat.getColor(mContext,R.color.colorWhite));
        }

    }
}
