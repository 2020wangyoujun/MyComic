package com.example.mycomic.data.entity.db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mycomic.data.entity.BaseBean;
import com.example.mycomic.data.entity.DownState;
import com.example.mycomic.db.utils.StringConverter;

import java.util.List;

@Entity
@TypeConverters(StringConverter.class)
public class DBChapters extends BaseBean {
    @PrimaryKey
    protected Long id;
    //漫画ID
    protected Long comic_id;
    //漫画标题
    protected String title;
    //章节标题
    protected String chapters_title;
    //章节编号
    protected int chapters;
    //章节漫画页面
    protected int num;
    //当前下载的漫画页面
    protected int current_num;
    //开始下载时间
    protected Long create_time;
    //最后更新时间
    protected Long update_time;
    /*state状态数据库保存*/
    protected int stateInte;

    protected List<String> comiclist;
    protected List<String> chapters_path;

    public int getStateInte() {
        return this.stateInte;
    }
    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }
    public Long getUpdate_time() {
        return this.update_time;
    }
    public void setUpdate_time(Long update_time) {
        this.update_time = update_time;
    }
    public Long getCreate_time() {
        return this.create_time;
    }
    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }
    public int getCurrent_num() {
        return this.current_num;
    }
    public void setCurrent_num(int current_num) {
        this.current_num = current_num;
    }
    public int getNum() {
        return this.num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getChapters() {
        return this.chapters;
    }
    public void setChapters(int chapters) {
        this.chapters = chapters;
    }
    public String getChapters_title() {
        return this.chapters_title;
    }
    public void setChapters_title(String chapters_title) {
        this.chapters_title = chapters_title;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public Long getComic_id() {
        return this.comic_id;
    }
    public void setComic_id(Long comic_id) {
        this.comic_id = comic_id;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }




    public DownState getState() {
        switch (getStateInte()){
            case 0:
                return DownState.START;
            case 1:
                return DownState.DOWN;
            case 2:
                return DownState.PAUSE;
            case 3:
                return DownState.STOP;
            case 4:
                return DownState.ERROR;
            case 5:
                return DownState.FINISH;
            case 6:
                return DownState.NONE;
            case -1:
                return DownState.DELETE;
            case -2:
                return DownState.CACHE;
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }
    public List<String> getChapters_path() {
        return this.chapters_path;
    }
    public void setChapters_path(List<String> chapters_path) {
        this.chapters_path = chapters_path;
    }
    public List<String> getComiclist() {
        return this.comiclist;
    }
    public void setComiclist(List<String> comiclist) {
        this.comiclist = comiclist;
    }

    @Override
    public String toString() {
        return "DBChapters{" +
                "chapters=" + chapters +
                ", stateInte=" + stateInte +
                '}';
    }
}
