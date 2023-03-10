package com.example.mycomic.presenter;
import android.app.Activity;

import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.ui.view.ICollectionView;
import com.example.mycomic.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableObserver;


public class CollectionPresenter extends SelectPresenter<ICollectionView>{

    public CollectionPresenter(Activity context, ICollectionView view) {
        super(context, view);
    }


    public void loadData(){
        mModel.getCollectedComicList(new DisposableObserver<List<Comic>>() {

            @Override
            public void onError(Throwable e) {
                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
            }

            @Override
            public void onComplete() {
                mView.getDataFinish();
            }


            @Override
            public void onNext(List<Comic> comics) {
                mComics= comics;
                if(comics.size()>0){
                    mView.fillData(comics);
                    resetSelect();
                }else{
                    mView.showEmptyView();
                }
            }
        });
    }

    @Override
    public void deleteComic() {
        List<Comic> mDeleteComics = new ArrayList<>();
        for(int i=0;i<mComics.size();i++){
            if(mMap.get(i) == Constants.CHAPTER_SELECTED){
                mDeleteComics.add(mComics.get(i));
            }
        }
        mModel.deleteCollectComic(mDeleteComics, new DisposableObserver<List<Comic>>() {

            @Override
            public void onNext(@NonNull List<Comic> comics) {
                clearSelect();
                mComics.clear();
                mComics.addAll(comics);
                if(comics.size()>0){
                    mView.fillData(comics);
                }else{
                    mView.showEmptyView();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                mView.quitEdit();
            }
        });

    }
}
