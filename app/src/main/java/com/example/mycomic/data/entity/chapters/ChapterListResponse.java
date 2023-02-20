/**
  * Copyright 2023 bejson.com 
  */
package com.example.mycomic.data.entity.chapters;

/**
 * Auto-generated: 2023-02-12 20:29:29
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class ChapterListResponse {

    private int error_code;
    private ChapterListResData data;
    public void setError_code(int error_code) {
         this.error_code = error_code;
     }
     public int getError_code() {
         return error_code;
     }

    public void setData(ChapterListResData data) {
         this.data = data;
     }
     public ChapterListResData getData() {
         return data;
     }

}