package com.example.mycomic.db.manager;

import android.content.Context;
import android.util.Log;

import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.db.DBChapters;
import com.example.mycomic.data.entity.db.DBSearchResult;
import com.example.mycomic.data.entity.db.DownInfo;
import com.example.mycomic.db.dao.ComicDao;
import com.example.mycomic.db.dao.DBChaptersDao;
import com.example.mycomic.db.dao.DBSearchResultDao;
import com.example.mycomic.db.dao.DownInfoDao;
import com.example.mycomic.db.database.ComicDatabase;

import java.util.List;

public class MainThreadDBEngine {
    private ComicDao comicDao;
    private DBChaptersDao dbChaptersDao;
    private DBSearchResultDao dbSearchResultDao;
    private DownInfoDao downInfoDao;
    public MainThreadDBEngine(Context context) {
        ComicDatabase comicDatabase = ComicDatabase.getInstance(context);
        comicDao = comicDatabase.getComicDao();
        dbChaptersDao=comicDatabase.getDBChaptersDao();
        dbSearchResultDao=comicDatabase.getDBSearchResultDao();
        downInfoDao= comicDatabase.getDownInfoDao();
    }
    public Comic findComic(Long id){
        return comicDao.findById(id);
    }

    public DBChapters findDBDownloadItems(long id){
        return dbChaptersDao.findById(id);
    }

    public boolean insert(Comic ... comics){
        try {
            comicDao.insertComics(comics);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(DBChapters... dbChapters){
        try {
            dbChaptersDao.insertDBChapters(dbChapters);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(DBSearchResult... dbSearchResults){
        try {
            dbSearchResultDao.insertDBSearchResult(dbSearchResults);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insert(DownInfo... downInfos){
        try {
            downInfoDao.insertDownInfos(downInfos);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertComicList(List<Comic> comics) {
        try {
            for(Comic item:comics) {
                comicDao.insertComics(item);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean insertDBChapterList(List<DBChapters> dbChapters){
        try {
            for(DBChapters item:dbChapters) {
                dbChaptersDao.insertDBChapters(item);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //更新
    public boolean update(Comic ... comics) {
        try {
            comicDao.updataComics(comics);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(DBChapters ... dbChapters) {
        try {
            dbChaptersDao.updataDBChapters(dbChapters);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //更新
    public boolean update(List<Comic> comics) {
        try {
            for(Comic item:comics) {
                comicDao.updataComics(item);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //更新
    public boolean update(DownInfo... downInfos) {
        try {
            downInfoDao.updataDownInfos(downInfos);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<DBSearchResult> queryAllDBSearchResult(){
        return dbSearchResultDao.queryAllDBSearchResult();
    }

    public boolean deleteAllDBSearchResult(){
        try {
            dbSearchResultDao.deleteAllDBSearchResult();
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Comic> queryAllCollect(){
        return comicDao.getAllCollect();
    }

    public List<Comic> queryHistory(int page){
        return comicDao.queryHistory(12,12*page);
    }

    public List<Comic> queryAllHistory(){
        return comicDao.queryAllHistory();
    }

    public List<DownInfo> queryDownInfo(long comic_id){
        return downInfoDao.findByComicId(comic_id);
    }
    public List<Comic> queryDownloadComic(){
        return comicDao.queryDownloadComic();
    }

    public List<DBChapters> queryDownloadItems(Long comic_id){
        return dbChaptersDao.queryDownloadItems(comic_id);
    }
}
