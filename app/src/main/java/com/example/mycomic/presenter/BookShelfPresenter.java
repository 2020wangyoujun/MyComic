package com.example.mycomic.presenter;
import android.app.Activity;

import com.example.mycomic.ui.view.IBookShelfView;


public class BookShelfPresenter extends BasePresenter<IBookShelfView> {
    public BookShelfPresenter(Activity context, IBookShelfView view) {
        super(context, view);
    }
}
