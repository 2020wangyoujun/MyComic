/**
  * Copyright 2023 bejson.com 
  */
package com.example.mycomic.data.entity.pictures;

/**
 * Auto-generated: 2023-02-12 20:39:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PictureListResponse {

    private int error_code;
    private PicturesListResData data;
    public void setError_code(int error_code) {
         this.error_code = error_code;
     }
     public int getError_code() {
         return error_code;
     }

    public void setData(PicturesListResData data) {
         this.data = data;
     }
     public PicturesListResData getData() {
         return data;
     }

}