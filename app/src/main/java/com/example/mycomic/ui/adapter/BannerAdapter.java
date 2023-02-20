package com.example.mycomic.ui.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;

import java.util.List;

public class BannerAdapter extends com.youth.banner.adapter.BannerAdapter<Comic, BannerAdapter.BannerViewHolder> {

    public BannerAdapter(List<Comic> datas) {
        super(datas);
    }

    @Override
    public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        return new BannerViewHolder(imageView);
    }

    @Override
    public void onBindView(BannerViewHolder holder, Comic data, int position, int size) {
        Glide.with(holder.itemView)
                .load(data.getCover())
                .placeholder(R.mipmap.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.imageView);
    }

    class BannerViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public BannerViewHolder(@NonNull ImageView itemView) {
            super(itemView);
            this.imageView = itemView;
        }
    }
}
