package com.example.mycomic.ui.adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.mycomic.R;
import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.PreloadChapters;
import com.example.mycomic.utils.LogUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ChapterViewpagerAdapter extends PagerAdapter {
    private List<String> mdatas;
    private Context mContext;
    private OnceClickListener listener;
    private int Direction = Constants.LEFT_TO_RIGHT;
    private LinkedList<View> mViews = null;

    public OnceClickListener getListener() {
        return listener;
    }

    public void setListener(OnceClickListener listener) {
        this.listener = listener;
    }


    public ChapterViewpagerAdapter(Context context) {
        mdatas = new ArrayList<>();
        this.mContext = context;
        mViews = new LinkedList<>();
    }
    public ChapterViewpagerAdapter(Context context, PreloadChapters preloadChapters, int mDirect) {
        this(context);
        this.mdatas.addAll(preloadChapters.getPrelist());
        this.mdatas.addAll(preloadChapters.getNowlist());
        this.mdatas.addAll(preloadChapters.getNextlist());
        this.Direction = mDirect;
    }

    public int getDirection() {
        return Direction;
    }

    public void setDirection(int direction) {
        Direction = direction;
        this.notifyDataSetChanged();
    }

    public void setDatas(PreloadChapters preloadChapters){
        this.mdatas.clear();
        this.mdatas.addAll(preloadChapters.getPrelist());
        this.mdatas.addAll(preloadChapters.getNowlist());
        this.mdatas.addAll(preloadChapters.getNextlist());
        notifyDataSetChanged();
    }

    public void clearList(){
        this.mdatas.clear();
        notifyDataSetChanged();
    }

    public void setNextDatas(List<String> mdatas){
        this.mdatas.addAll(mdatas);
        notifyDataSetChanged();
    }

    public void setPreDatas(List<String> mdatas){
        for(int i=0;i<mdatas.size();i++){
            this.mdatas.add(0,mdatas.get(mdatas.size()-1-i));
        }
        Log.d("mdatas",this.mdatas.toString());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {//必须实现
        return mdatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {//必须实现
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {//必须实现，实例化
        View convertView = null;
        ViewHolder holder = null;
        if (mViews.size() == 0) {
            holder = new ViewHolder();
            convertView = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_chapters, null);
            holder.imageView = (PhotoView) convertView.findViewById(R.id.pv_comic);
            convertView.setTag(holder);
        } else {
            convertView = mViews.removeFirst();
            holder = (ViewHolder) convertView.getTag();
        }
        final DiskCacheStrategy cache;//判断图片来自SD卡还是网络
        if(mdatas.get(mdatas.size()-position-1).substring(1,8).equals("storage")){
            cache = DiskCacheStrategy.NONE;
        }else{
            cache = DiskCacheStrategy.AUTOMATIC;
        }
        PhotoView iv = holder.imageView;
        if(Direction == Constants.RIGHT_TO_LEFT){
            setImageView(mdatas.get(mdatas.size()-position-1),iv,true);
        }else{
            setImageView(mdatas.get(position),iv,false);
        }
        holder.imageView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if(listener!=null){
                    listener.onClick(view,x,y);
                }
            }
        });
        container.addView(convertView);
        return convertView;
    }

    public class ViewHolder {
        public PhotoView imageView = null;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
        container.removeView((View) object);
    }

    public interface OnceClickListener{
        void onClick(View view, float x, float y);
    }

    public void setImageView(final String url, final PhotoView iv, final boolean isRightToLeft){
        LogUtil.d(url.substring(1,8));
        final DiskCacheStrategy cache;//判断图片来自SD卡还是网络
        if(url.substring(1,8).equals("storage")){
            cache = DiskCacheStrategy.NONE;
        }else{
            cache = DiskCacheStrategy.AUTOMATIC;
        }
        Glide.with(mContext)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        LogUtil.e(url+"加载失败"+e.toString());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).placeholder(R.mipmap.pic_default_vertical)
                  .diskCacheStrategy(cache)
                  .into(iv);
//                .asBitmap()//强制Glide返回一个Bitmap对象
//                .listener(new RequestListener<String, Bitmap>() {
//                    @Override
//                    public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
//                        LogUtil.e(url+"加载失败"+e.toString());
//                        Glide.with(mContext)
//                                .load(R.mipmap.pic_default_vertical)
//                                .diskCacheStrategy(cache)
//                                .into(iv);
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
//                        return false;
//                    }
//                })
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                        int width = bitmap.getWidth();
//                        int height = bitmap.getHeight();
//                        int S_width =  DisplayUtil.getScreenWidth(mContext);
//                        int S_height =  DisplayUtil.getScreenHeight(mContext);
//
//                        float scale = ((float) height)/width;
//                        iv.setImageBitmap(bitmap);
//                        if(width>height){
//                            float S_scale = S_height/(S_width*scale);
//                            if(isRightToLeft){
//                                iv.setScale(S_scale,S_height*S_scale,0,false);
//                            }else{
//                                iv.setScale(S_scale,0,0,false);
//                            }
//
//                        }
//                    }
//                });
    }

}
