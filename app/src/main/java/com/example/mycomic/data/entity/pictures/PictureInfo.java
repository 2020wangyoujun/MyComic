/**
  * Copyright 2023 bejson.com 
  */
package com.example.mycomic.data.entity.pictures;

import java.util.Date;
import java.util.List;

/**
 * Auto-generated: 2023-02-12 20:39:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PictureInfo {

    private String base_url;
    private int hd_width;
    private String picture_suffix;
    private int total_picture_count;
    private List<PictureListItem> picture_list;
    private String image_source;
    public void setBase_url(String base_url) {
         this.base_url = base_url;
     }
     public String getBase_url() {
         return base_url;
     }

    public void setHd_width(int hd_width) {
         this.hd_width = hd_width;
     }
     public int getHd_width() {
         return hd_width;
     }

    public void setPicture_suffix(String picture_suffix) {
         this.picture_suffix = picture_suffix;
     }
     public String getPicture_suffix() {
         return picture_suffix;
     }

    public void setTotal_picture_count(int total_picture_count) {
         this.total_picture_count = total_picture_count;
     }
     public int getTotal_picture_count() {
         return total_picture_count;
     }

    public void setPicture_list(List<PictureListItem> picture_list) {
         this.picture_list = picture_list;
     }
     public List<PictureListItem> getPicture_list() {
         return picture_list;
     }

    public void setImage_source(String image_source) {
         this.image_source = image_source;
     }
     public String getImage_source() {
         return image_source;
     }

}