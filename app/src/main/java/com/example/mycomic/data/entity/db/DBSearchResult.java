package com.example.mycomic.data.entity.db;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mycomic.data.entity.BaseBean;


@Entity
public class DBSearchResult extends BaseBean {
    @PrimaryKey(autoGenerate = true)
    protected Long id;
    @NonNull
    protected String title;
    @NonNull
    protected Long search_time;
    public Long getSearch_time() {
        return this.search_time;
    }
    public void setSearch_time(Long search_time) {
        this.search_time = search_time;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
