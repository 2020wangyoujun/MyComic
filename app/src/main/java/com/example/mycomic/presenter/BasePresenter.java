package com.example.mycomic.presenter;

import android.app.Activity;
import android.content.Intent;

import com.example.mycomic.net.ComicService;
import com.example.mycomic.net.MainFactory;
import com.example.mycomic.ui.view.IBaseView;


public abstract class BasePresenter<GV extends IBaseView> {

    protected GV mView;
    protected Activity mContext;

    public static final ComicService comicService = MainFactory.getComicServiceInstance();

    public BasePresenter(Activity context, GV view) {
        mContext = context;
        mView = view;
    }


    protected BasePresenter() {
    }

    public long getCurrentTime(){
        java.util.Date date = new java.util.Date();
        long datetime = date.getTime();
        return datetime;
    }



}
