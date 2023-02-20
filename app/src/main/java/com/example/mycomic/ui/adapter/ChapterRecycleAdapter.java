package com.example.mycomic.ui.adapter;
import android.content.Context;

import com.example.mycomic.R;
import com.example.mycomic.data.entity.PreloadChapters;
import com.example.mycomic.ui.adapter.base.BaseRecyclerAdapter;
import com.example.mycomic.ui.adapter.base.BaseRecyclerHolder;

public class ChapterRecycleAdapter extends BaseRecyclerAdapter<String> {

    public ChapterRecycleAdapter(Context context, int itemLayoutId) {
        super(context, itemLayoutId);
    }

    public void setDatas(PreloadChapters preloadChapters){
        this.list.clear();
        this.list.addAll(preloadChapters.getPrelist());
        this.list.addAll(preloadChapters.getNowlist());
        this.list.addAll(preloadChapters.getNextlist());
        notifyDataSetChanged();
    }

    public void clearList(){
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public void convert(BaseRecyclerHolder holder, String item, int position) {
        holder.setPhotoViewImageByUrl(R.id.pv_comic,item);
    }
}
