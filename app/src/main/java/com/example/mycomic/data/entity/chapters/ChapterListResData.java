/**
  * Copyright 2023 bejson.com 
  */
package com.example.mycomic.data.entity.chapters;

import java.util.List;

/**
 * Auto-generated: 2023-02-12 20:29:29
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ChapterListResData {

//    private String pay_info;
    private List<ChapterListItem> chapter_list;
//    private List<String> volume_info;
//    public void setPay_info(String pay_info) {
//         this.pay_info = pay_info;
//     }
//     public String getPay_info() {
//         return pay_info;
//     }

    public void setChapter_list(List<ChapterListItem> chapter_list) {
         this.chapter_list = chapter_list;
     }
     public List<ChapterListItem> getChapter_list() {
         return chapter_list;
     }

//    public void setVolume_info(List<String> volume_info) {
//         this.volume_info = volume_info;
//     }
//     public List<String> getVolume_info() {
//         return volume_info;
//     }

}