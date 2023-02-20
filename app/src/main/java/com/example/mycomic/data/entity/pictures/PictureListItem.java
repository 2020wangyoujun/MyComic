/**
  * Copyright 2023 bejson.com 
  */
package com.example.mycomic.data.entity.pictures;

import com.example.mycomic.data.entity.BaseBean;

/**
 * Auto-generated: 2023-02-12 20:39:9
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PictureListItem extends BaseBean {

    private String current_img_url;
    private int width;
    private int height;
    private int img_id;
    private String pic_name;
    public void setCurrent_img_url(String current_img_url) {
         this.current_img_url = current_img_url;
     }
     public String getCurrent_img_url() {
         return current_img_url;
     }

    public void setWidth(int width) {
         this.width = width;
     }
     public int getWidth() {
         return width;
     }

    public void setHeight(int height) {
         this.height = height;
     }
     public int getHeight() {
         return height;
     }

    public void setImg_id(int img_id) {
         this.img_id = img_id;
     }
     public int getImg_id() {
         return img_id;
     }

    public void setPic_name(String pic_name) {
         this.pic_name = pic_name;
     }
     public String getPic_name() {
         return pic_name;
     }

}