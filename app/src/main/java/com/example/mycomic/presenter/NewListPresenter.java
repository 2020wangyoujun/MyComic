package com.example.mycomic.presenter;

import android.app.Activity;

import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.LoadingItem;
import com.example.mycomic.module.ComicModule;
import com.example.mycomic.ui.view.INewView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;

public class NewListPresenter extends BasePresenter<INewView>{
    private ComicModule mModel;
    private int page = 1;
    private List<Comic> mList;
    private boolean isloadingdata;

    public NewListPresenter(Activity context, INewView view) {
        super(context, view);
        mModel = new ComicModule(context);
        mList = new ArrayList<>();
        this.isloadingdata= false;
    }

    public void loadData(){
        if(!isloadingdata){
            mModel.getNewComicList(page, new DisposableObserver<List<Comic>>() {

                @Override
                protected void onStart() {
                    super.onStart();
                    isloadingdata = true;
                }

                @Override
                public void onNext(@NonNull List<Comic> comics) {
                    mList.addAll(comics);
                    List<Comic> temp = new ArrayList<>(mList);
                    if(comics.size()==12){
                        temp.add(new LoadingItem(true));
                        mView.fillData(temp);
                        isloadingdata = false;
                    }else{
                        temp.add(new LoadingItem(false));
                        mView.fillData(temp);
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {
                    page++;
                }
            });
        }
    }
}
