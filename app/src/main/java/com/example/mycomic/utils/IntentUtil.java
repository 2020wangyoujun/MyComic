package com.example.mycomic.utils;

import android.app.Activity;
import android.content.Intent;

import com.example.mycomic.ui.activity.MainActivity;
import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.ui.activity.CategoryActivity;
import com.example.mycomic.ui.activity.ComicChapterActivity;
import com.example.mycomic.ui.activity.ComicDetaiActivity;
import com.example.mycomic.ui.activity.DownloadChapterlistActivity;
import com.example.mycomic.ui.activity.NewListActivity;
import com.example.mycomic.ui.activity.RankActivity;
import com.example.mycomic.ui.activity.SearchActivity;
import com.example.mycomic.ui.activity.SelectDownloadActivity;

import java.util.HashMap;

public class IntentUtil {
    public static void ToMainActivity(Activity context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    public static void ToComicDetail(Activity context, String id,String title){
        Intent intent = new Intent(context, ComicDetaiActivity.class);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        context.startActivity(intent);
    }

    public static void ToComicDetail(Activity context, String id,String title,int type){
        Intent intent = new Intent(context, ComicDetaiActivity.class);
        intent.putExtra(Constants.COMIC_FROM,type);
        intent.putExtra(Constants.COMIC_ID,id);
        intent.putExtra(Constants.COMIC_TITLE,title);
        context.startActivity(intent);
    }


    public static void ToComicChapter(Activity context, int chapters, Comic mComic){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivity(intent);
    }

//    public static void ToComicChapter(Context context, int chapters,Comic mComic){
//        Intent intent = new Intent(context, ComicChapterActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
//        intent.putExtra(Constants.COMIC,mComic);
//        context.startActivity(intent);
//    }
//
    public static void ToComicChapterForResult(Activity context, int chapters, Comic mComic){
        Intent intent = new Intent(context, ComicChapterActivity.class);
        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivityForResult(intent,1);
    }


//    public static void ToComicChapter(Activity context, int chapters,long id,String title,List<String> chapter_titles,int type){
//        Intent intent = new Intent(context, ComicChapterActivity.class);
//        intent.putExtra(Constants.COMIC_CHAPERS,chapters);
//        intent.putExtra(Constants.COMIC_ID,id);
//        intent.putExtra(Constants.COMIC_TITLE,title);
//        intent.putExtra(Constants.COMIC_READ_TYPE,type);
//        intent.putStringArrayListExtra(Constants.COMIC_CHAPER_TITLE,  new ArrayList<>(chapter_titles));
//        context.startActivity(intent);
//    }
//
//    public static void ToIndex(Activity context, Comic mComic){
//        Intent intent = new Intent(context, IndexActivity.class);
//        intent.putExtra(Constants.COMIC,mComic);
//        context.startActivity(intent);
//    }
//
    public static void ToSelectDownload(Activity context, Comic mComic){
        Intent intent = new Intent(context, SelectDownloadActivity.class);
        intent.putExtra(Constants.COMIC,mComic);
        context.startActivity(intent);
    }

    public static void ToSearch(Activity context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    public static void ToDownloadListActivity(Activity context, HashMap<Integer,Integer> map, Comic comic) {
        Intent intent = new Intent(context, DownloadChapterlistActivity.class);
        intent.putExtra(Constants.COMIC_SELECT_DOWNLOAD,map);
        intent.putExtra(Constants.COMIC,comic);
        context.startActivity(intent);
    }

    public static void toRankActivity(Activity context) {
        Intent intent = new Intent(context, RankActivity.class);
        context.startActivity(intent);
    }


//    public static void toUrl(Activity context,String url) {
//        Uri uri = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        context.startActivity(intent);
//    }
//
//    public static void toQQchat(Activity context,String number) {
//        String url = "mqqwpa://im/chat?chat_type=wpa&uin="+number;//uin是发送过去的qq号码
//        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//    }
//
    public static void toCategoryActivity(Activity context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        context.startActivity(intent);
    }

    public static void toCategoryActivity(Activity context,String type,int value) {
        Intent intent = new Intent(context, CategoryActivity.class);
        intent.putExtra(type,value);
        context.startActivity(intent);
    }

    public static void toNewActivity(Activity context) {
        Intent intent = new Intent(context, NewListActivity.class);
        context.startActivity(intent);
    }
}
