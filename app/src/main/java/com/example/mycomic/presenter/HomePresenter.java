package com.example.mycomic.presenter;
import android.app.Activity;
import android.util.Log;

import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.module.ComicModule;
import com.example.mycomic.ui.view.IHomeView;
import com.example.mycomic.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.observers.DisposableObserver;


public class HomePresenter extends BasePresenter<IHomeView> {
    private int num = 20;
    private ComicModule mModel;
    private List<Comic> mBanners, mDatas;
//    private DaoHelper mHelper;
    private Comic recentComic;

    public List<Comic> getmBanners() {
        return mBanners;
    }

    public List<Comic> getmDatas() {
        return mDatas;
    }

    public HomePresenter(Activity context, IHomeView view) {
        super(context, view);
        this.mModel = new ComicModule(context);
//        mHelper= new DaoHelper(context);
        mBanners = new ArrayList<>();
        mDatas = new ArrayList<>();
    }

    public void LoadData() {
        mModel.getData(new DisposableObserver<List<Comic>>() {
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
//                Log.e("hhh",comics.get(3).getTitle());
                if (comics.size() > 12) {
                    mView.fillData(comics);
                    mDatas = comics;
                } else {
                    mView.fillBanner(comics);
                    mBanners = comics;
                }
            }
        });
    }

//    public void refreshData() {
//        mModel.refreshData(new Observer<List<Comic>>() {
//
//            @Override
//            public void onError(Throwable e) {
//                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
//            }
//
//            @Override
//            public void onComplete() {
//
//            }
//
//            @Override
//            public void onSubscribe(@NonNull Disposable disposable) {
//
//            }
//
//            @Override
//            public void onNext(List<Comic> comics) {
//                mView.fillData(comics);
//                mDatas = comics;
//            }
//        });
//    }
//
//
//    public void loadMoreData(int page) {
//        mModel.getMoreComicList(page, new Observer<List<Comic>>() {
//
//            @Override
//            public void onError(Throwable e) {
//                mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
//            }
//
//            @Override
//            public void onComplete() {
//                mView.getDataFinish();
//            }
//
//            @Override
//            public void onSubscribe(@NonNull Disposable disposable) {
//
//            }
//
//            @Override
//            public void onNext(List<Comic> comics) {
//                mView.appendMoreDataToView(comics);
//            }
//        });
//    }
//
//    public void toRecentComic() {
//        IntentUtil.ToComicChapter(mContext,recentComic.getCurrentChapter(),recentComic);
//    }
//
//    public void getRecent() {
//        recentComic = mHelper.findRecentComic();
//        if(recentComic!=null){
//            String recent = "续看:"+recentComic.getTitle()+" 第"+(recentComic.getCurrentChapter()+1)+"话";
//            mView.fillRecent(recent);
//        }else{
//            mView.fillRecent(null);
//        }
//    }
}
