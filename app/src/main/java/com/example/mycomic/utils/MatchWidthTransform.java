package com.example.mycomic.utils;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class MatchWidthTransform extends BitmapTransformation {
    String ID="com.example.glide.transformations.MatchWidthTransform";

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return Bitmap.createBitmap(toTransform,0,0,outWidth,outHeight);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID.getBytes(StandardCharsets.UTF_8));
    }
}
