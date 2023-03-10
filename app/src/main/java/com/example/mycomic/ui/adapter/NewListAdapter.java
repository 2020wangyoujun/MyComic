package com.example.mycomic.ui.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.LoadingItem;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;

public class NewListAdapter extends BaseRecyclerAdapter<Comic> {
    public static final int ITEM_FULL = 1;
    public static final int ITEM_LOADING = 2;
    private int itemLoadingLayoutId;
    public NewListAdapter(Context context, int itemLayoutId, int itemLoadingLayoutId) {
        super(context, itemLayoutId);
        this.itemLoadingLayoutId = itemLoadingLayoutId;
    }

    public BaseRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case ITEM_LOADING:
                view = inflater.inflate(itemLoadingLayoutId, parent, false);
                break;
            default:
                view = inflater.inflate(itemLayoutId, parent, false);
                break;
        }

        return BaseRecyclerHolder.getRecyclerHolder(context, view);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, Comic item, int position) {
        switch (getItemViewType(position)){
            case ITEM_FULL:
                holder.setText(R.id.tv_title,item.getTitle());
                holder.setText(R.id.tv_author,item.getAuthor());
                holder.setText(R.id.tv_new,item.getDescribe());
                holder.setText(R.id.tv_update,item.getUpdates());

                holder.setImageByUrl(R.id.iv_cover,item.getCover());
                break;
            case ITEM_LOADING:
                LoadingItem loading = (LoadingItem) item;
                if(loading.isLoading()){
                    holder.startAnimation(R.id.iv_loading);
                    holder.setText(R.id.tv_loading,"正在加载");
                }else{
                    holder.setImageResource(R.id.iv_loading,R.mipmap.loading_finish);
                    holder.setText(R.id.tv_loading,"已全部加载完毕");
                }
                break;
        }
    }

    public int getItemViewType(int position) {
        Comic comic = list.get(position);
        if((comic instanceof LoadingItem)){
            return ITEM_LOADING;
        } else{
            return ITEM_FULL;
        }
    }
}

