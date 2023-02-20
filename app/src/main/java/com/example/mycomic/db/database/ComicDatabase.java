package com.example.mycomic.db.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mycomic.data.commons.Constants;
import com.example.mycomic.data.entity.Comic;
import com.example.mycomic.data.entity.db.DBChapters;
import com.example.mycomic.data.entity.db.DBSearchResult;
import com.example.mycomic.data.entity.db.DownInfo;
import com.example.mycomic.db.dao.ComicDao;
import com.example.mycomic.db.dao.DBChaptersDao;
import com.example.mycomic.db.dao.DBSearchResultDao;
import com.example.mycomic.db.dao.DownInfoDao;

@Database(entities = {Comic.class, DBChapters.class, DBSearchResult.class, DownInfo.class},version = 1,exportSchema = false)
public abstract class ComicDatabase extends RoomDatabase {
    private static ComicDatabase INSTANCE;
    public abstract ComicDao getComicDao();
    public abstract DBChaptersDao getDBChaptersDao();
    public abstract DBSearchResultDao getDBSearchResultDao();
    public abstract DownInfoDao getDownInfoDao();
    public static synchronized ComicDatabase getInstance(Context context){
        if(INSTANCE==null){
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),ComicDatabase.class, Constants.DB_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
