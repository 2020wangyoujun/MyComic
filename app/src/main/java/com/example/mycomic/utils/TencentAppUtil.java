package com.example.mycomic.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class TencentAppUtil {
    private static String chapterListUrl="/10.9.8/Comic/comicChapterList/comic_id/";
    private static String pictureListUrl="/10.9.8/Chapter/chapterPictureList/hd_type/LD/comic_id/{comic_id}/preload_state/1/chapter_id/{chapter_id}";

    private static String appBaseUrl="a.ac.qq.com";
    private static String fakeduin="0";
    private static String qimei="22fcc7b6fbb467a9a35ffb5310001d716801";
    private static String baseStr="4jo2YHMm0d2VGt59tVYndX9P7eFcw8TvRv5lMqFP1TT";
    public static String getTimestamp(){
        long time=System.currentTimeMillis();//获取系统时间的10位的时间戳
        return String.valueOf(time);
    }
    public static String getChapterListUrl(String comic_id){
        return chapterListUrl+comic_id;
    }

    public static String getPictureListUrl(String comic_id,String chapter_id){
        return "/10.9.8/Chapter/chapterPictureList/hd_type/LD/comic_id/"+comic_id+"/preload_state/1/chapter_id/"+chapter_id;
    }

    public static String getSc(String url,String timeStamp){
        return md5encode(url+appBaseUrl+fakeduin+qimei+timeStamp+baseStr);
    }
    public static String  md5encode(String str){
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.reset();
            instance.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] digest = instance.digest();
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                if (Integer.toHexString(digest[i] & 255).length() == 1) {
                    stringBuilder.append("0");
                    stringBuilder.append(Integer.toHexString(digest[i] & 255));
                } else {
                    stringBuilder.append(Integer.toHexString(digest[i] & 255));
                }
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
