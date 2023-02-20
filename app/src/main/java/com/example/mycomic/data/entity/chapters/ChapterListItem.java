/**
  * Copyright 2023 bejson.com 
  */
package com.example.mycomic.data.entity.chapters;
import com.example.mycomic.data.entity.BaseBean;

import java.util.Date;

/**
 * Auto-generated: 2023-02-12 20:29:29
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ChapterListItem extends BaseBean {

    private int chapter_id;
    private int seq_no;
    private String chapter_title;
    private String cover_h_url;
    private long size;
    private int good_count;
    private Date update_date;
    private int icon_type;
    private String buy_tips;
    private int is_related_cartoon;
    private int new_state;
    public void setChapter_id(int chapter_id) {
         this.chapter_id = chapter_id;
     }
     public int getChapter_id() {
         return chapter_id;
     }

    public void setSeq_no(int seq_no) {
         this.seq_no = seq_no;
     }
     public int getSeq_no() {
         return seq_no;
     }

    public void setChapter_title(String chapter_title) {
         this.chapter_title = chapter_title;
     }
     public String getChapter_title() {
         return chapter_title;
     }

    public void setCover_h_url(String cover_h_url) {
         this.cover_h_url = cover_h_url;
     }
     public String getCover_h_url() {
         return cover_h_url;
     }

    public void setSize(long size) {
         this.size = size;
     }
     public long getSize() {
         return size;
     }

    public void setGood_count(int good_count) {
         this.good_count = good_count;
     }
     public int getGood_count() {
         return good_count;
     }

    public void setUpdate_date(Date update_date) {
         this.update_date = update_date;
     }
     public Date getUpdate_date() {
         return update_date;
     }

    public void setIcon_type(int icon_type) {
         this.icon_type = icon_type;
     }
     public int getIcon_type() {
         return icon_type;
     }

    public void setBuy_tips(String buy_tips) {
         this.buy_tips = buy_tips;
     }
     public String getBuy_tips() {
         return buy_tips;
     }

    public void setIs_related_cartoon(int is_related_cartoon) {
         this.is_related_cartoon = is_related_cartoon;
     }
     public int getIs_related_cartoon() {
         return is_related_cartoon;
     }

    public void setNew_state(int new_state) {
         this.new_state = new_state;
     }
     public int getNew_state() {
         return new_state;
     }

    @Override
    public String toString() {
        return "ChapterListItem{" +
                "chapter_id=" + chapter_id +
                ", seq_no=" + seq_no +
                ", chapter_title='" + chapter_title + '\'' +
                ", cover_h_url='" + cover_h_url + '\'' +
                ", size=" + size +
                ", good_count=" + good_count +
                ", update_date=" + update_date +
                ", icon_type=" + icon_type +
                ", buy_tips='" + buy_tips + '\'' +
                ", is_related_cartoon=" + is_related_cartoon +
                ", new_state=" + new_state +
                '}';
    }
}