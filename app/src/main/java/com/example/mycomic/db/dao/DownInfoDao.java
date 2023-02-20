package com.example.mycomic.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mycomic.data.entity.db.DownInfo;

import java.util.List;

@Dao
public interface DownInfoDao {
    @Insert
    void insertDownInfos(DownInfo... downInfos);

    @Update
    void updataDownInfos(DownInfo ... downInfos);

    @Delete
    void deleteDownInfos(DownInfo ... downInfos);

    @Query("DELETE FROM DownInfo")
    void deleteAllDownInfos();

    @Query("SELECT * FROM DownInfo WHERE comic_id LIKE :comic_id")
    List<DownInfo> findByComicId(Long comic_id);

//    @Query("SELECT * FROM Comic ORDER BY ID DESC")
//    List<Comic> getAllStudents();
}

