package com.example.mycomic.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mycomic.R;
import com.example.mycomic.data.entity.Comic;

public class GlideImageLoader{
    public void displayImage(Context context, Object path, ImageView imageView) {
        /**
         注意：
         1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
         2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
         传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */
        //Glide 加载图片简单用法
        Comic mComic = (Comic) path;
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(context)
                .load(mComic.getCover())
                .placeholder(R.mipmap.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    public static void loadImage(Context context,String url,ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.mipmap.pic_default)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }
    public static void loadImage(Context context,int resid,ImageView imageView) {
        Glide.with(context)
                .load(resid)
                .placeholder(R.mipmap.pic_default)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    public static void loadImage(Context context,int resid,ImageView imageView,boolean isCenter) {
        Glide.with(context)
                .load(resid)
                .placeholder(R.mipmap.pic_default)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(imageView);
    }

    public static void loadRoundImage(Context context, String url, ImageView mCover) {
        Glide.with(context).load(url).centerCrop().transform(new GlideCircleTransform()).into(mCover);
    }
}
