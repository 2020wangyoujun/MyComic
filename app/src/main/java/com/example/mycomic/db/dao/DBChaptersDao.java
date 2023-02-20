package com.example.mycomic.db.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mycomic.data.entity.db.DBChapters;

import java.util.List;


@Dao
public interface DBChaptersDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDBChapters(DBChapters ... dbChapters);

    @Update
    void updataDBChapters(DBChapters ... dbChapters);

    @Delete
    void deleteDBChapters(DBChapters ... dbChapters);

    @Query("DELETE FROM DBChapters")
    void deleteAllDBChapters();

    @Query("SELECT * FROM DBChapters WHERE id LIKE :id")
    DBChapters findById(Long id);

    @Query("SELECT * FROM DBChapters WHERE comic_id LIKE :comic_id AND stateInte >=0")
    List<DBChapters> queryDownloadItems(Long comic_id);
//    @Query("SELECT * FROM Comic ORDER BY ID DESC")
//    List<Comic> getAllStudents();
}
