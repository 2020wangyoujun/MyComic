package com.example.mycomic.db.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.db.dao.ComicDao;
import com.example.mycomic.db.database.ComicDatabase;

import java.util.List;

public class DBEngine {
    private ComicDao comicDao;

    private Comic comicTmp;

    public DBEngine(Context context){
        ComicDatabase comicDatabase = ComicDatabase.getInstance(context);
        comicDao = comicDatabase.getComicDao();
    }
    public void insertComics(Comic... comics){
        new InsertAsyncTask(comicDao).execute(comics);
    }

    public void updataComics(Comic ... comics){
        new UpdadtaAsyncTask(comicDao).execute(comics);
    }

    public void deleteComics(Comic ... comics){
        new DeleteAsyncTask(comicDao).execute(comics);
    }

    public void deleteAllComics(){
        new DeleteAllAsyncTask(comicDao).execute();
    }

//    public void queryAllComics(){
//        new QueryAllComics(comicDao).execute();
//    }

    static class InsertAsyncTask extends AsyncTask<Comic,Void,Void>{
        private ComicDao dao;

        public InsertAsyncTask(ComicDao comicDao) {
            this.dao = comicDao;
        }

        @Override
        protected Void doInBackground(Comic... comics) {
            dao.insertComics(comics);
            return null;
        }
    }

    static class UpdadtaAsyncTask extends AsyncTask<Comic,Void,Void>{
        private ComicDao dao;

        public UpdadtaAsyncTask(ComicDao comicDao) {
            this.dao = comicDao;
        }

        @Override
        protected Void doInBackground(Comic... comics) {
            dao.updataComics(comics);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Comic,Void,Void>{
        private ComicDao dao;

        public DeleteAsyncTask(ComicDao comicDao) {
            this.dao = comicDao;
        }

        @Override
        protected Void doInBackground(Comic... comics) {
            dao.deleteComics(comics);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private ComicDao dao;

        public DeleteAllAsyncTask(ComicDao comicDao) {
            this.dao = comicDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAllComics();
            return null;
        }
    }

//    static class FindByIdAsyncTask extends AsyncTask<Long,Void,Void>{
//        private ComicDao dao;
//
//        public FindByIdAsyncTask(ComicDao comicDao) {
//            this.dao = comicDao;
//        }
//
//        @Override
//        protected Void doInBackground(Long... longs) {
//            comicTmp=dao.findById(longs[0]);
//            return null;
//        }
//    }

//    static class QueryAllComics extends AsyncTask<Void,Void,Void>{
//        private ComicDao dao;
//
//        public QueryAllComics(ComicDao comicDao) {
//            this.dao = comicDao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            List<Comic> allComics=dao.getAllComics();
//            for(Comic comic:allComics){
//                Log.e("query result:","查询结果："+comic.toString());
//            }
//            return null;
//        }
//    }

}
