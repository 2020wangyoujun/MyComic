package com.example.mycomic.module;

import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.commons.Url;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.DownState;
import com.example.mycomic.data.entity.HomeTitle;
import com.example.mycomic.data.entity.HttpResult;
import com.example.mycomic.data.entity.SearchBean;
import com.example.mycomic.data.entity.chapters.ChapterListItem;
import com.example.mycomic.data.entity.chapters.ChapterListResponse;
import com.example.mycomic.data.entity.db.DBChapters;
import com.example.mycomic.data.entity.db.DBSearchResult;
import com.example.mycomic.data.entity.db.DownInfo;
import com.example.mycomic.data.entity.pictures.PictureInfo;
import com.example.mycomic.data.entity.pictures.PictureListItem;
import com.example.mycomic.data.entity.pictures.PictureListResponse;
import com.example.mycomic.db.manager.MainThreadDBEngine;
import com.example.mycomic.net.ComicService;
import com.example.mycomic.net.Function.RetryFunction;
import com.example.mycomic.net.MainFactory;
import com.example.mycomic.presenter.ComicChapterPresenter;
import com.example.mycomic.utils.CryptUtil;
import com.example.mycomic.utils.DBEntityUtils;
import com.example.mycomic.utils.FileUtil;
import com.example.mycomic.utils.GlideCacheUtil;
import com.example.mycomic.utils.KukuComicAnalysis;
import com.example.mycomic.utils.LogUtil;
import com.example.mycomic.utils.NetworkUtils;
import com.example.mycomic.utils.TencentAppUtil;
import com.example.mycomic.utils.TencentComicAnalysis;
import com.google.gson.Gson;
import com.trello.rxlifecycle4.android.ActivityEvent;
import com.trello.rxlifecycle4.components.support.RxAppCompatActivity;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;


public class ComicModule {
    private RxAppCompatActivity rxAppCompatActivity;
    private MainThreadDBEngine mHelper;
    public static final ComicService comicService = MainFactory.getComicServiceInstance();
    public ComicModule(Activity context){
        rxAppCompatActivity = (RxAppCompatActivity) context;
        mHelper = new MainThreadDBEngine(context);
    }

    //????????????
    public void getData(Observer<List<Comic>> observer){
        Observable ComicListObservable = Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic>  mdats = new ArrayList<>();
                    Document recommend = Jsoup.connect(Url.TencentHomePage).get();
                    Document japan = Jsoup.connect(Url.TencentJapanHot).get();
                    Document doc = Jsoup.connect(Url.TencentTopUrl+"1").get();
                    //addComic(recommend,mdats,Constants.TYPE_RECOMMEND);
                    addComic(recommend,mdats,Constants.TYPE_HOT_SERIAL);
                    addComic(recommend,mdats,Constants.TYPE_BOY_RANK);
                    addComic(recommend,mdats,Constants.TYPE_GIRL_RANK);
                    addComic(japan,mdats,Constants.TYPE_HOT_JAPAN);
                    addComic(doc,mdats,Constants.TYPE_RANK_LIST);

                    HomeTitle homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("");
                    mdats.add(homeTitle);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }

        });
        Observable ComicBannerObservable = Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentBanner).get();
                    List<Comic>  mdats = TencentComicAnalysis.TransToBanner(doc);
                    observableEmitter.onNext(mdats);
                } catch (IOException e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        });


        Observable.merge(ComicListObservable, ComicBannerObservable).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * ???????????????List???
     * @param doc
     * @param mdats
     * @param type
     */
    private void addComic(Document doc, List<Comic> mdats, int type) {
        HomeTitle homeTitle;
        try {
            switch (type){
                case Constants.TYPE_RECOMMEND:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("????????????");
                    homeTitle.setTitleType(Constants.TYPE_RECOMMEND);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToRecommendComic(doc));
                    break;
                case Constants.TYPE_BOY_RANK:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("????????????");
                    homeTitle.setTitleType(Constants.TYPE_BOY_RANK);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToBoysComic(doc));
                    break;
                case Constants.TYPE_GIRL_RANK:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("????????????");
                    homeTitle.setTitleType(Constants.TYPE_GIRL_RANK);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToGirlsComic(doc));
                    break;
                case Constants.TYPE_HOT_SERIAL:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("????????????");
                    homeTitle.setTitleType(Constants.TYPE_HOT_SERIAL);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToNewComic(doc));
                    break;
                case Constants.TYPE_HOT_JAPAN:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("?????????");
                    homeTitle.setTitleType(Constants.TYPE_HOT_JAPAN);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToJapanComic(doc));
                    break;
                case Constants.TYPE_RANK_LIST:
                    homeTitle = new HomeTitle();
                    homeTitle.setItemTitle("?????????");
                    homeTitle.setTitleType(Constants.TYPE_RANK_LIST);
                    mdats.add(homeTitle);
                    mdats.addAll(TencentComicAnalysis.TransToComic(doc));
                    break;
            }
        }catch (Exception e){
            Log.d("zhhr","type = "+type+" is Error");
        }

    }

    //???????????????
    public void getComicDetail(final String mComicId, final int from, Observer observer){
        Observable.create(new ObservableOnSubscribe<Comic>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Comic> observableEmitter) throws Exception {
                try {
                    Comic comicFromDB = (Comic) mHelper.findComic(Long.parseLong(mComicId));
//                    Comic comicFromDB = null;
                    if(NetworkUtils.isAvailable(rxAppCompatActivity)){
                        Comic mComic;
                        if(from == Constants.FROM_TENCENT){
                            Document doc = Jsoup.connect(Url.TencentDetail+mComicId).get();
                            mComic = TencentComicAnalysis.TransToComicDetail(doc);
                            mComic.setFrom(from);
                        }else{
                            String url = Url.KukuComicBase+"/comiclist/"+(Long.parseLong(mComicId)/1000000);
                            Document doc = Jsoup.connect(url).get();
                            mComic = KukuComicAnalysis.TransToComicDetail(doc);
                            mComic.setFrom(from);
                        }

                        if(comicFromDB!=null) {
                            mComic.setCurrentChapter(comicFromDB.getCurrentChapter());
                            mComic.setStateInte(comicFromDB.getStateInte());
                            mComic.setDownloadTime(comicFromDB.getDownloadTime());
                            mComic.setCollectTime(comicFromDB.getCollectTime());
                            mComic.setClickTime(comicFromDB.getClickTime());
                            mComic.setDownload_num(comicFromDB.getDownload_num());
                            mComic.setDownload_num_finish(comicFromDB.getDownload_num_finish());
                            mComic.setCurrent_page(comicFromDB.getCurrent_page());
                            mComic.setCurrent_page_all(comicFromDB.getCurrent_page_all());
                            mComic.setIsCollected(comicFromDB.getIsCollected());
                            mComic.setReadType(comicFromDB.getReadType());
                            mComic.setFrom(from);
                        }else{
                            mComic.setCurrentChapter(0);
                        }
                        observableEmitter.onNext(mComic);
                    }else{
                        if(comicFromDB !=null){
                            observableEmitter.onNext(comicFromDB);
                        }else{
                            observableEmitter.onError(new ConnectException());
                        }
                    }
                } catch (SocketTimeoutException e){
                    observableEmitter.onError(e);
                } catch (Exception e) {
                    LogUtil.e(e.toString());
                    if(e instanceof InterruptedIOException){//????????????????????????????????????

                    }else{
                        observableEmitter.onError(e);
                    }
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }).subscribeOn(Schedulers.io())
                .retryWhen(new RetryFunction())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getChaptersList(final Comic mComic, final ComicChapterPresenter.OnProgressListener listener, final int comic_chapters, Observer observer){
        //??????????????????????????????
        //?????????????????????????????????????????????????????????????????????????????????????????????
        DBChapters items;
        final String comic_id = mComic.getId()+"";
        try{
            //??????-1???????????????
            items = mHelper.findDBDownloadItems(Long.parseLong(comic_id+comic_chapters));
            if(items.getState()== DownState.DELETE){
                items = null;
            }
        }catch (Exception e){
            items = null;
        }
        //?????????????????????????????????item??????null???????????????????????????????????????????????????????????????????????????????????????
        if(items!=null&&items.getComiclist()!=null){
            LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"???????????????????????????????????????");
            if(items.getState() == DownState.FINISH){
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"???????????????????????????SD???????????????");
                items.setComiclist(items.getChapters_path());
            }
            observer.onNext(items);
            observer.onComplete();
        }else{
            LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"????????????");
            //?????????????????????
            if(mComic.getFrom() == Constants.FROM_TENCENT){
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"????????????????????????");
                //??????????????????????????????????????????????????????
                Observable<DBChapters> observable = comicService.getChapters(comic_id,comic_chapters+"");
                observable.map(new Function<DBChapters, DBChapters>() {

                            @Override
                            public DBChapters apply(DBChapters dbChapters) throws Throwable {
                                mHelper.insert(dbChapters);
                                return dbChapters;
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                        .subscribe(observer);
                //????????????????????????
//                CacheProviders.getComicCacheInstance()
//                        .getChapters(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
//                        .retryWhen(new RetryFunction())
//                        .map(new Function<DBChapters, Object>() {
//                            @Override
//                            public Object apply(@NonNull DBChapters chapters) throws Exception {
//                                chapters.setComic_id(mComic.getId());
//                                chapters.setChapters(comic_chapters);
//                                return chapters;
//                            }
//                        }).subscribeOn(Schedulers.io())
//                        .unsubscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//??????????????????
//                        .subscribe(observer);
                //?????????????????????RxCache?????????????????????????????????????????????????????????????????????
            }else{ //?????????????????????
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"????????????????????????");
                //??????????????????????????????????????????????????????
                /*String chapters[] = mComic.getChapters_url().get(comic_chapters).split("/");
                Observable<DBChapters> observable = comicService.getKuKuChapterList(chapters[2],chapters[3]);
                //????????????????????????
                CacheProviders.getComicCacheInstance()
                        .getKuKuChapterList(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
                        .retryWhen(new RetryFunction())
                        .map(new Function<DBChapters, Object>() {
                            @Override
                            public Object apply(@NonNull DBChapters chapters) throws Exception {
                                chapters.setComic_id(mComic.getId());
                                chapters.setChapters(comic_chapters);
                                return chapters;
                            }
                        }).subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//??????????????????
                        .subscribe(observer);*/
                //?????????????????????RxCache?????????????????????????????????????????????????????????????????????

//                final List<String> imageUrl = new ArrayList<>();
//                Observable.create(new ObservableOnSubscribe<DBChapters>() {
//                    @Override
//                    public void subscribe(@NonNull ObservableEmitter<DBChapters> observableEmitter) throws Exception {
//                        try{
//                            int page = 1;
//                            int size = 0;
//                            int offset = 2;
//                            String url = Url.KukuComicBase+mComic.getChapters_url().get(comic_chapters);
//                            while (ComicChapterPresenter.isLoading|| DownloadChapterlistPresenter.isLoading){
//                                //????????????
//                                Document doc = Jsoup.connect(url+page+".htm").get();
//                                if(size==0){
//                                    String size_pre = doc.getElementsByAttributeValue("valign","top").get(0).text().split("???")[1];
//                                    size = Integer.parseInt(size_pre.split("???")[0]);
//                                }
//                                String image = doc.select("script").get(3).toString();
//                                String image_url[] = image.split("src=");
//                                //?????????????????????page????????????????????????
//                                String image_url1 = "";
//                                String image_url2 = "";
//                                //??????????????????????????????????????????????????????????????????
//                                try{//??????????????????
//                                    image_url1= Url.KukuComicImageBae+image_url[0].split("\"")[5].split("'")[0];
//                                    image_url2 = Url.KukuComicImageBae+image_url[1].split("\"")[2].split("'")[0];
//                                }catch (Exception e){//????????????????????????????????????
//                                    image_url1 = Url.KukuComicImageBae+image_url[1].split("\"")[2].split("'")[0];
//                                    if(image_url.length>2){
//                                        image_url2 = Url.KukuComicImageBae+image_url[2].split("\"")[2].split("'")[0];
//                                    }else{
//                                        offset = 1;//???????????????
//                                    }
//                                }
//                                if(imageUrl.size()<size){
//                                    imageUrl.add(image_url1);
//                                    LogUtil.d(mComic.getTitle()+(comic_chapters+1)+image_url1);
//                                }
//                                if(imageUrl.size()<size&&offset==2){//???????????????
//                                    imageUrl.add(image_url2);
//                                    LogUtil.d(mComic.getTitle()+(comic_chapters+1)+image_url2);
//                                }
//                                if(listener!=null){//??????????????????
//                                    listener.OnProgress(page,size);
//                                }
//                                page = page +offset;
//                            }
//                        } catch (HttpStatusException e){
//                            DBChapters chapters = new DBChapters();
//                            chapters.setId(Long.parseLong(comic_id+comic_chapters));
//                            chapters.setChapters_title(mComic.getChapters().get(comic_chapters));
//                            chapters.setComic_id(mComic.getId());
//                            chapters.setChapters(comic_chapters);
//                            chapters.setComiclist(imageUrl);
//                            chapters.setState(DownState.CACHE);
//                            //?????????VIEW
//                            observableEmitter.onNext(chapters);
//                            try{
//                                //???????????????????????????RxCache????????????????????????????????????????????????????????????
//                                //?????????????????????list????????????????????????????????????cache????????????????????????
//                                mHelper.insert(chapters);
//                                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"???????????????,id="+comic_id+comic_chapters);
//                            }catch (SQLiteConstraintException exception){
//                                //?????????????????????????????????????????????????????????
//                                LogUtil.e(mComic.getTitle()+(comic_chapters+1)+"?????????????????????");
//                            }
//                        }catch (SocketTimeoutException e){
//                            observableEmitter.onError(e);
//                        } catch (Exception e){
//                            LogUtil.e(e.toString());
//                            if(e instanceof InterruptedIOException){//????????????????????????????????????
//
//                            }else{
//                                observableEmitter.onError(e);
//                            }
//                        }finally {
//                            observableEmitter.onComplete();
//                        }
//                    }
//                }).subscribeOn(Schedulers.io())
//                        .unsubscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//????????????????????????????????????????????????
//                        .subscribe(observer);
            }
        }
    }

    public void loadChapterIdList(final String comic_id,Observer observer){
        String localtime= TencentAppUtil.getTimestamp();
        comicService.getChapterList(localtime,TencentAppUtil.getSc(TencentAppUtil.getChapterListUrl(comic_id),localtime),comic_id)
                .map(new Function<ResponseBody, ChapterListResponse>() {

                    @Override
                    public ChapterListResponse apply(ResponseBody responseBody) throws Throwable {
                        ChapterListResponse response=new Gson().fromJson(CryptUtil.decode(responseBody.string()), ChapterListResponse.class);
                        return response;
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getPicturesList(final Comic mComic, final ComicChapterPresenter.OnProgressListener listener, final int comic_chapters,final int chapter_id, Observer observer){
        //??????????????????????????????
        //?????????????????????????????????????????????????????????????????????????????????????????????
        DBChapters items;
        final String comic_id = mComic.getId()+"";
        try{
            //??????-1???????????????
            items = mHelper.findDBDownloadItems(Long.parseLong(comic_id+comic_chapters));
            if(items.getState()== DownState.DELETE){
                items = null;
            }
        }catch (Exception e){
            items = null;
        }
        //?????????????????????????????????item??????null???????????????????????????????????????????????????????????????????????????????????????
        if(items!=null&&items.getComiclist()!=null){
            LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"???????????????????????????????????????");
            if(items.getState() == DownState.FINISH){
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"???????????????????????????SD???????????????");
                items.setComiclist(items.getChapters_path());
            }
            observer.onNext(items);
            observer.onComplete();
        }else{
            LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"????????????");
            //?????????????????????
            if(mComic.getFrom() == Constants.FROM_TENCENT){
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"????????????????????????");
                //??????????????????????????????????????????????????????
                String localtime=TencentAppUtil.getTimestamp();
                comicService.getPictureList(localtime,TencentAppUtil.getSc(TencentAppUtil.getPictureListUrl(comic_id,String.valueOf(chapter_id)),localtime),comic_id,String.valueOf(chapter_id))
                .map(new Function<ResponseBody, PictureListResponse>() {

                    @Override
                    public PictureListResponse apply(ResponseBody responseBody) throws Throwable {
                        PictureListResponse response=new Gson().fromJson(CryptUtil.decode(responseBody.string()), PictureListResponse.class);
                        return response;
                    }
                })
                .map(new Function<PictureListResponse, DBChapters>() {
                    @Override
                    public DBChapters apply(PictureListResponse pictureListResponse) throws Throwable {
                        PictureInfo pictureInfo=pictureListResponse.getData().getPicture_info();
                        String base_url=pictureInfo.getBase_url();
                        final List<String> imageUrls = new ArrayList<>();
                        for(PictureListItem pictureListItem:pictureInfo.getPicture_list()){
                            imageUrls.add(base_url+pictureListItem.getPic_name()+"/800");
                        }
                        DBChapters chapters = new DBChapters();
                        chapters.setId(Long.parseLong(comic_id+comic_chapters));
                        chapters.setChapters_title(mComic.getChapters().get(comic_chapters));
                        chapters.setComic_id(mComic.getId());
                        chapters.setChapters(comic_chapters);
                        chapters.setComiclist(imageUrls);
                        chapters.setState(DownState.CACHE);
                        try{
                            //???????????????????????????RxCache????????????????????????????????????????????????????????????
                            //?????????????????????list????????????????????????????????????cache????????????????????????
                            mHelper.insert(chapters);
                            LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"???????????????,id="+comic_id+comic_chapters);
                        }catch (Exception exception){
                            //?????????????????????????????????????????????????????????
                            LogUtil.e(mComic.getTitle()+(comic_chapters+1)+"?????????????????????");
                        }
                        return chapters;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
            }else{ //?????????????????????
                LogUtil.d(mComic.getTitle()+(comic_chapters+1)+"????????????????????????");
            }
        }
    }

    //???????????????????????????

    public void getComicFromDB(final long id,Observer observer) {
        Observable.create(new ObservableOnSubscribe<Comic>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Comic> observableEmitter) throws Exception {
                try{
                    Comic mComic = (Comic) mHelper.findComic(id);
                    if(mComic!=null) {
                        observableEmitter.onNext(mComic);
                    }else{
                        observableEmitter.onNext(null);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    //???????????????????????????

    public void saveComicToDB(final Comic comic,Observer observer){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    if(mHelper.findComic(comic.getId())==null){
                        if(mHelper.insert(comic)){
                            observableEmitter.onNext(true);
                        }else{
                            observableEmitter.onNext(false);
                        }
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void updateComicToDB(final Comic mComic, Observer observer){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    Comic comic = (Comic) mHelper.findComic(mComic.getId());
                    if(comic!=null){
                        if(mHelper.update(mComic)){
                            observableEmitter.onNext(true);
                        }else{
                            observableEmitter.onNext(false);
                        }
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    /**
     * ??????????????????????????????
     * @param title
     * @param observer
     */
    public void updateSearchResultToDB(final String title, Observer<Boolean> observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    final java.util.Date date = new java.util.Date();
                    long datetime = date.getTime();
                    DBSearchResult result = new DBSearchResult();
                    result.setTitle(title);
                    result.setSearch_time(datetime);
                    if(mHelper.insert(result)){
                        observableEmitter.onNext(true);
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.<Boolean>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }


    public void getRankList(final long currenttime, final String type, final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentRankUrl+"t="+currenttime+"&type="+type+"&page="+page+"&pageSize=10&style=items").get();
                    List<Comic> mdats = TencentComicAnalysis.TransToRank(doc);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getCategroyList(final Map<String, Integer> mSelectMap, final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    String theme= "";
                    String audience = "";
                    String finish = "";
                    String nation = "";
                    if(mSelectMap.get("theme")!=0){
                        theme = "/theme/"+mSelectMap.get("theme");
                    }
                    if(mSelectMap.get("audience")!=0){
                        audience = "/audience/"+mSelectMap.get("audience");
                    }
                    if(mSelectMap.get("finish")!=0){
                        finish = "/finish/"+mSelectMap.get("finish");
                    }
                    if(mSelectMap.get("nation")!=0){
                        nation = "/nation/"+mSelectMap.get("nation");
                    }
                    String url = Url.TencentCategoryUrlHead+audience+theme+finish+Url.TencentCategoryUrlMiddle+nation+Url.TencentCategoryUrlFoot+page;
                    Document doc = Jsoup.connect(url).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToComic(doc);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getNewComicList( final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentNewUrl+page).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToNewListComic(doc);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    //????????????
    public void getDynamicResult(String title,Observer observer){
        Observable<HttpResult<List<SearchBean>>> Observable = comicService.getDynamicSearchResult(Url.TencentSearchUrl+title);
        Observable.map(new Function<HttpResult<List<SearchBean>>, List<SearchBean>>() {

                @Override
                public List<SearchBean> apply(HttpResult<List<SearchBean>> listHttpResult) throws Throwable {
                    return listHttpResult.getData();
                }
            })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
        //        CacheProviders.getComicCacheInstance()
//                .getDynamicSearchResult(Observable,new DynamicKey(Url.TencentSearchUrl+title),new EvictDynamicKey(false))
//                .map(new HttpResultFunc<List<SearchBean>>())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
//                .subscribe(observer);
    }

    public void getSearchResult(final String title, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                List<Comic> mdats = new ArrayList<>();
                List<Comic> mkukudatas = new ArrayList<>();
                try {
                    //???????????????
                    Document doc = Jsoup.connect(Url.TencentSearchResultUrl+title).get();
                    mdats = TencentComicAnalysis.TransToSearchResultComic(doc);
                    observableEmitter.onNext(mdats);
                }catch (SocketTimeoutException e){
                    e.printStackTrace();
                } catch (Exception e) {
                    //observableEmitter.onError(e);
                    e.printStackTrace();
                }
                if(Constants.isNeedKuku){
                    try{
                        //???????????????
                        String kukuUrl = Url.KukuSearchUrlHead+ URLEncoder.encode(title, "GBK")+Url.KukuSearchUrlFoot;
                        Document doc_kuku = Jsoup.connect(kukuUrl).get();
                        mkukudatas = KukuComicAnalysis.TransToSearchResultComic_Kuku(doc_kuku);
                        observableEmitter.onNext(mkukudatas);
                    }catch (SocketTimeoutException e){
                        e.printStackTrace();
                    } catch (Exception e) {
                        //observableEmitter.onError(e);
                        e.printStackTrace();
                    }
                }
                if(mdats.size()==0&&mkukudatas.size()==0){
                    observableEmitter.onError(new NullPointerException());
                }
                observableEmitter.onComplete();
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getHistorySearch(Observer<List<Comic>> observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try{
                    List<DBSearchResult> results = mHelper.queryAllDBSearchResult();
                    List<Comic> comics = DBEntityUtils.transSearchToComic(results);
                    observableEmitter.onNext(comics);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.<List<Comic>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    public void clearSearchHistory(Observer observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    if(mHelper.deleteAllDBSearchResult()){
                        observableEmitter.onNext(true);
                    }else{
                        observableEmitter.onNext(false);
                    }
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getTopResult(Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    Document doc = Jsoup.connect(Url.TencentSearchRecommend).get();
                    List<Comic> mdats = TencentComicAnalysis.TransToSearchTopComic(doc);
                    observableEmitter.onNext(mdats);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getCollectedComicList(Observer observer){
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic> comics = mHelper.queryAllCollect();
                    observableEmitter.onNext(comics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    public void deleteCollectComic(final List<Comic> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    for (int i = 0; i < mLists.size(); i++) {
                        Comic items = mLists.get(i);
                        items.setIsCollected(false);
                        mHelper.update(items);
                    }
                    List<Comic> mComics = mHelper.queryAllCollect();
                    observableEmitter.onNext(mComics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     *???????????????????????????list
     * @param page ??????
     * @param observer
     */
    public void getHistoryComicList(final int page, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic> comics = mHelper.queryHistory(page);
                    observableEmitter.onNext(comics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    public void deleteHistoryComic(final List<Comic> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try{
                    for(int i=0;i<mLists.size();i++){
                        Comic items = mLists.get(i);
                        items.setClickTime(0);
                        items.setCurrent_page(0);
                        items.setCurrentChapter(0);
                        mHelper.update(items);
                    }
                    List<Comic> mComics = mHelper.queryHistory(0);
                    observableEmitter.onNext(mComics);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void deleteHistoryComic(Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try{
                    List<Comic> mLists = mHelper.queryAllHistory();
                    for(int i=0;i<mLists.size();i++){
                        mLists.get(i).setClickTime(0);
                        mLists.get(i).setCurrent_page(0);
                        mLists.get(i).setCurrentChapter(0);
                    }

                    mHelper.update(mLists);
                    List<Comic> mComics = mHelper.queryHistory(0);
                    observableEmitter.onNext(mComics);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    public void getDownloadChaptersList(final Comic comic, final int comic_chapters, Observer observer){
        String comic_id = String.valueOf(comic.getId());
        loadChapterIdList(comic_id, new DisposableObserver<ChapterListResponse>() {
            @Override
            public void onNext(ChapterListResponse chapterListResponse) {
                List<ChapterListItem> listItems=chapterListResponse.getData().getChapter_list();
                int id=-1;
                if(listItems!=null){
                    for(ChapterListItem item:listItems){
                        if(item.getSeq_no()==comic_chapters){
                            id=item.getChapter_id();
                            break;
                        }
                    }
                }
                if(id!=-1){
                    getPicturesList(comic,null,comic_chapters,id,observer);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        /*  Observable<DBChapters> observable = comicService.getChapters(comic_id,comic_chapters+"");
        CacheProviders.getComicCacheInstance()
                .getChapters(observable,new DynamicKey(comic_id+comic_chapters),new EvictDynamicKey(false))
                .retryWhen(new RetryFunction())
                .map(new Function<DBChapters, Object>() {
                    @Override
                    public Object apply(@NonNull DBChapters chapters) throws Exception {
                        return chapters.getComiclist();
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))//??????????????????
                .subscribe(observer);*/
    }

    public void getDownItemFromDB(final long comic_id, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DownInfo>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DownInfo>> observableEmitter) throws Exception {
                try{
                    List<DownInfo> results = mHelper.queryDownInfo(comic_id);
                    observableEmitter.onNext(results);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getDownloadComicList(Observer observer){
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    List<Comic> comics = mHelper.queryDownloadComic();
                    observableEmitter.onNext(comics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);

    }

    public void deleteDownloadComic(final List<Comic> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<Comic>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Comic>> observableEmitter) throws Exception {
                try {
                    for (int i = 0; i < mLists.size(); i++) {
                        mLists.get(i).setStateInte(-1);
                        FileUtil.deleteDir(FileUtil.SDPATH + FileUtil.COMIC + mLists.get(i).getId());
                    }
                    mHelper.insertComicList(mLists);
                    List<Comic> mComics = mHelper.queryDownloadComic();
                    observableEmitter.onNext(mComics);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void getDownloadItemsFromDB(final long comic_id, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DBChapters>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBChapters>> observableEmitter) throws Exception {
                try{
                    List<DBChapters> results = mHelper.queryDownloadItems(comic_id);
                    observableEmitter.onNext(results);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void getDownloadItemsFromDB(final Comic mComic, final HashMap<Integer,Integer> mMap, Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DBChapters>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBChapters>> observableEmitter) throws Exception {
                try{
                    if(mMap.size()>0){
                        DBChapters item;
                        //???hashmap??????????????????
                        List<Map.Entry<Integer,Integer>> list = new ArrayList<>(mMap.entrySet());
                        Collections.sort(list,new Comparator<Map.Entry<Integer,Integer>>() {
                            public int compare(Map.Entry<Integer, Integer> o1,
                                               Map.Entry<Integer, Integer> o2) {
                                return o1.getKey().compareTo(o2.getKey());
                            }
                        });
                        //??????map
                        for(Map.Entry<Integer,Integer> mapping:list){
                            if(mapping.getValue() == Constants.CHAPTER_SELECTED){
                                try{
                                    //???????????????????????????????????????????????????????????????????????????????????????????????????
                                    item = mHelper.findDBDownloadItems(Long.parseLong(mComic.getId()+""+mapping.getKey()));
                                    if(item.getState()==DownState.DELETE){
                                        item = new DBChapters();//?????????????????????????????????????????????new
                                    }
                                }catch (Exception e){
                                    item = new DBChapters();//?????????????????????????????????????????????new
                                }
                                if(item.getState()!=DownState.FINISH) {
                                    item.setId(Long.parseLong(mComic.getId() + "" + mapping.getKey()));
                                    item.setChapters_title(mComic.getChapters().get(mapping.getKey()));
                                    item.setComic_id(mComic.getId());
                                    item.setChapters(mapping.getKey());
                                    item.setState(DownState.NONE);
                                    mHelper.insert(item);
                                }
//                                try{
//                                    //???????????????????????????
//                                    mHelper.insert(item);
//                                }catch (SQLiteConstraintException exception){
//                                    LogUtil.e("??????????????????????????????????????????");
//                                    //???????????????????????????????????????state
//                                    mHelper.update(item);
//                                }
                            }
                        }
                    }
                    List<DBChapters> results = mHelper.queryDownloadItems(mComic.getId());
                    //????????????????????????
                    observableEmitter.onNext(results);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }

    public void updateDownloadItemsList(final List<DBChapters> mLists, Observer observer) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                try{
                    boolean result = true;
                    for(int i=0;i<mLists.size();i++){
                        if (mLists.get(i).getState() != DownState.FINISH){
                            mLists.get(i).setState(DownState.NONE);
                        }
                        result = mHelper.insertDBChapterList(mLists);
                    }
                    observableEmitter.onNext(result);
                }catch (Exception e){
                    observableEmitter.onError(e);
                }finally {
                    observableEmitter.onComplete();
                }
            }

        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void deleteDownloadItem(final List<DBChapters> mLists, final Comic comic , Observer observer) {
        Observable.create(new ObservableOnSubscribe<List<DBChapters>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DBChapters>> observableEmitter) throws Exception {
                try {
                    for (int i = 0; i < mLists.size(); i++) {
                        mLists.get(i).setStateInte(-1);
                        mLists.get(i).setChapters_path(new ArrayList<String>());
                        FileUtil.deleteDir(FileUtil.SDPATH + FileUtil.COMIC + comic.getId() + "/" + mLists.get(i).getChapters());
                    }
                    mHelper.insertDBChapterList(mLists);
                    List<DBChapters> Items = mHelper.queryDownloadItems(comic.getId());
                    observableEmitter.onNext(Items);
                } catch (Exception e) {
                    observableEmitter.onError(e);
                } finally {
                    observableEmitter.onComplete();
                }
            }

        }).subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void download(String url, final String filePath, final String fileName, Observer observer) {
        comicService.download("bytes=0" + "-", url)
                /*????????????*/
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {

                    @Override
                    public InputStream apply(@NonNull ResponseBody responseBody) throws Exception {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation()) // ??????????????????
                .doOnNext(new Consumer<InputStream>() {
                    @Override
                    public void accept(@NonNull InputStream inputStream) throws Exception {
                        FileUtil.saveImgToSdCard(inputStream, filePath,fileName);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void download(final DBChapters info, final int page, Observer observer) {
        if(page>=info.getComiclist().size())//??????????????????
            return;
        comicService.download("bytes=0" + "-", info.getComiclist().get(page))
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Object>() {
                    @Override
                    public Object apply(@NonNull ResponseBody responseBody) throws Exception {
                        //??????????????????SD???
                        FileUtil.saveImgToSdCard(responseBody.byteStream(), FileUtil.SDPATH + FileUtil.COMIC + info.getComic_id() + "/" + info.getChapters()+"/",page+".png");
                        ArrayList<String> paths =  new ArrayList<>();
                        if(info.getChapters_path()!=null){
                            paths =new ArrayList<>(info.getChapters_path());
                        }
                        paths.add(FileUtil.SDPATH + FileUtil.COMIC + info.getComic_id() + "/"+ info.getChapters()+"/"+page+".png");
                        //??????????????????
                        info.setChapters_path(paths);
                        return info;
                    }
                })
                .observeOn(Schedulers.computation()) // ??????????????????
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public void clearCache(Observer observer) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> observableEmitter) throws Exception {
                try {
                    //FileUtil.deleteDir(FileUtil.SDPATH + FileUtil.CACHE);//????????????API??????
                    GlideCacheUtil.getInstance().clearImageAllCache(rxAppCompatActivity);
                    observableEmitter.onNext(GlideCacheUtil.getInstance().getCacheSize(rxAppCompatActivity));
                } catch (Exception e) {
                    observableEmitter.onError(e);
                    e.printStackTrace();
                }finally {
                    observableEmitter.onComplete();
                }
            }
        }) .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
    }
}
