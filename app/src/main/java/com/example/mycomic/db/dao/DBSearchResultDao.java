package com.example.mycomic.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mycomic.data.entity.db.DBSearchResult;

import java.util.List;

@Dao
public interface DBSearchResultDao {
    @Insert
    void insertDBSearchResult(DBSearchResult... dbSearchResults);

    @Update
    void updataDBSearchResult(DBSearchResult... dbSearchResults);

    @Delete
    void deleteDBSearchResult(DBSearchResult... dbSearchResults);

    @Query("DELETE FROM DBSearchResult")
    void deleteAllDBSearchResult();

    @Query("SELECT * FROM DBSearchResult ORDER BY search_time DESC")
    List<DBSearchResult> queryAllDBSearchResult();
//    @Query("SELECT * FROM DBSearchResult WHERE id LIKE :id")
//    DBChapters findById(Long id);
}
