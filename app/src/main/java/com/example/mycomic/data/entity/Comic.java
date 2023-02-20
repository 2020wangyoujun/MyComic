package com.example.mycomic.data.entity;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.mycomic.data.entity.chapters.ChapterListItem;
import com.example.mycomic.db.utils.StringConverter;

import java.util.List;


@Entity
@TypeConverters(StringConverter.class)
public class Comic extends BaseBean {
    @PrimaryKey
    protected long id;
    //标题
    protected String title;
    //封面图片
    protected String cover;
    //作者
    protected String author;
    //章节标题
    protected List<String> chapters;

    protected List<String> chapters_url;

    //标签
    protected List<String> tags;
    //收藏数
    protected String collections;
    //详情
    protected String describe;
    //评分
    protected String point;
    //人气值
    protected String popularity;
    //话题量
    protected String topics;
    //更新时间
    protected String updates;
    //状态
    protected String status;
    //默认阅读方式
    protected int readType;
    //当前章节
    protected int currentChapter;
    //收藏时间
    protected long collectTime;
    //点击时间
    protected long clickTime;
    //下载时间
    protected long downloadTime;
    //是否收藏
    protected boolean isCollected;

    /*state状态数据库保存*/
    protected int stateInte;
    //当前页
    protected int current_page;
    //当前共有多少页面
    protected int current_page_all;
    //有多少话在下载
    protected int download_num;
    //下载完成多少话
    protected int download_num_finish;
    //来自什么资源
    protected int from;
    //章节id列表
    @Ignore
    protected List<ChapterListItem> chapterListItems;
    
    public int getDownload_num_finish() {
        return this.download_num_finish;
    }
    public void setDownload_num_finish(int download_num_finish) {
        this.download_num_finish = download_num_finish;
    }
    public int getDownload_num() {
        return this.download_num;
    }
    public void setDownload_num(int download_num) {
        this.download_num = download_num;
    }
    public int getCurrent_page() {
        return this.current_page;
    }
    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }
    public int getStateInte() {
        return this.stateInte;
    }
    public void setStateInte(int stateInte) {
        this.stateInte = stateInte;
    }
    public boolean getIsCollected() {
        return this.isCollected;
    }
    public void setIsCollected(boolean isCollected) {
        this.isCollected = isCollected;
    }
    public long getDownloadTime() {
        return this.downloadTime;
    }
    public void setDownloadTime(long downloadTime) {
        this.downloadTime = downloadTime;
    }
    public long getClickTime() {
        return this.clickTime;
    }
    public void setClickTime(long clickTime) {
        this.clickTime = clickTime;
    }
    public long getCollectTime() {
        return this.collectTime;
    }
    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }
    public int getCurrentChapter() {
        return this.currentChapter;
    }
    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }
    public int getReadType() {
        return this.readType;
    }
    public void setReadType(int readType) {
        this.readType = readType;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getUpdates() {
        return this.updates;
    }
    public void setUpdates(String updates) {
        this.updates = updates;
    }
    public String getTopics() {
        return this.topics;
    }
    public void setTopics(String topics) {
        this.topics = topics;
    }
    public String getPopularity() {
        return this.popularity;
    }
    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }
    public String getPoint() {
        return this.point;
    }
    public void setPoint(String point) {
        this.point = point;
    }
    public String getDescribe() {
        return this.describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public String getCollections() {
        return this.collections;
    }
    public void setCollections(String collections) {
        this.collections = collections;
    }
    public List<String> getTags() {
        return this.tags;
    }
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
    public List<String> getChapters() {
        return this.chapters;
    }
    public void setChapters(List<String> chapters) {
        this.chapters = chapters;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getCover() {
        return this.cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public List<ChapterListItem> getChapterListItems() {
        return chapterListItems;
    }

    public void setChapterListItems(List<ChapterListItem> chapterListItems) {
        this.chapterListItems = chapterListItems;
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
            default:
                return DownState.FINISH;
        }
    }

    public void setState(DownState state) {
        setStateInte(state.getState());
    }
    public int getCurrent_page_all() {
        return this.current_page_all;
    }
    public void setCurrent_page_all(int current_page_all) {
        this.current_page_all = current_page_all;
    }
    public int getFrom() {
        return this.from;
    }
    public void setFrom(int from) {
        this.from = from;
    }
    public List<String> getChapters_url() {
        return this.chapters_url;
    }
    public void setChapters_url(List<String> chapters_url) {
        this.chapters_url = chapters_url;
    }

}