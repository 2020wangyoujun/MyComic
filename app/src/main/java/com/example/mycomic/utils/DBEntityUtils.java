package com.example.mycomic.utils;

import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.SearchBean;
import com.example.mycomic.data.entity.db.DBSearchResult;

import java.util.ArrayList;
import java.util.List;

public class DBEntityUtils {
    public static List<Comic> transSearchToComic(List<DBSearchResult> results) {
        List<Comic> comics = new ArrayList<>();
        try{
            for(int i=0;i<results.size();i++){
                Comic comic = new Comic();
                comic.setTitle(results.get(i).getTitle());
                comics.add(comic);
            }
        }catch (Exception e){
            LogUtil.e(e.getMessage());
        }
        return comics;
    }

    public static List<Comic> transDynamicSearchToComic(List<SearchBean> results){
        List<Comic> comics = new ArrayList<>();
        try{
            for(int i=0;i<results.size();i++){
                Comic comic = new Comic();
                comic.setTitle(results.get(i).getTitle());
                comic.setId(results.get(i).getId());
                comics.add(comic);
            }
        }catch (Exception e){
            LogUtil.e(e.getMessage());
        }
        return comics;
    }
}
