package com.example.mycomic.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mycomic.data.entity.Comic;

import java.util.List;


@Dao
public interface ComicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComics(Comic... comics);

    @Update
    void updataComics(Comic ... comics);

    @Delete
    void deleteComics(Comic ... comics);

    @Query("DELETE FROM Comic")
    void deleteAllComics();

    @Query("SELECT * FROM Comic WHERE id LIKE :id")
    Comic findById(Long id);

    @Query("SELECT * FROM Comic WHERE isCollected = 1 ORDER BY collectTime DESC LIMIT 10000")
    List<Comic> getAllCollect();

    @Query("SELECT * FROM Comic WHERE clickTime > 0 ORDER BY clickTime LIMIT :limit OFFSET :offset ")
    List<Comic> queryHistory(int limit,int offset);

    @Query("SELECT * FROM Comic WHERE clickTime > 0 ORDER BY clickTime LIMIT 1000")
    List<Comic> queryAllHistory();

    @Query("SELECT * FROM Comic WHERE stateInte = 1 OR stateInte = 5 ORDER BY downloadTime DESC")
    List<Comic> queryDownloadComic();
//    @Query("SELECT * FROM Comic ORDER BY ID DESC")
//    List<Comic> getAllStudents();
}
