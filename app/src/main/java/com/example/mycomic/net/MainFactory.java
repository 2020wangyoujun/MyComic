package com.example.mycomic.net;


public class MainFactory {

    public static ComicService comicService;
    protected static final Object monitor = new Object();

    public static ComicService getComicServiceInstance(){
        synchronized (monitor){
            if(comicService==null){
                comicService = new MainRetrofit().getService();
            }
            return comicService;
        }
    }
}
