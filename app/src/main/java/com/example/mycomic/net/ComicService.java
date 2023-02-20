package com.example.mycomic.net;

import com.example.mycomic.data.entity.HttpResult;
import com.example.mycomic.data.entity.PreloadChapters;
import com.example.mycomic.data.entity.SearchBean;
import com.example.mycomic.data.entity.db.DBChapters;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ComicService{

    @GET("getChapterList/{id}/{chapter}")
    Observable<DBChapters> getChapters(@Path("id") String id, @Path("chapter") String chapter);

    @GET("getPreNowChapterList/{id}/{chapter}")
    Observable<PreloadChapters> getPreNowChapterList(@Path("id") String id, @Path("chapter") String chapter);
    @GET
    Observable<HttpResult<List<SearchBean>>> getDynamicSearchResult(@Url String url);

    @GET("getKuKuChapterList/{id}/{chapter}")
    Observable<DBChapters> getKuKuChapterList(@Path("id") String id, @Path("chapter") String chapter);


    @Streaming/*大文件需要加入这个判断，防止下载过程中写入到内存中*/
    @GET
    Observable<ResponseBody> download(@Header("RANGE") String start, @Url String url);

    @Headers({"qimei: 22fcc7b6fbb467a9a35ffb5310001d716801",
            "qimei36: 22fcc7b6fbb467a9a35ffb5310001d716801",
            "version: 10.9.8",
            "channel: 2099",
            "resolution: 2560*1440",
            "network: WIFI",
            "osversion: 8.1.0",
            "teen-state: 1",
            "gender: 1",
            "model: Nexus 6P",
            "brand: google",
            "os-bit: 64",
            "recommend-switch: 2",
            "oversea: 1",
            "userstate: 2",
            "fakeduin: 0",
            "logintype: 0",
            "accept-encoding: deflate",
            "user-agent: okhttp/3.11.0"})
    @GET("10.9.8/Comic/comicChapterList/comic_id/{id}")
    Observable<ResponseBody> getChapterList(@Header("localtime") String localtime,@Header("sc") String sc,@Path("id") String id);

    @Headers({"qimei: 22fcc7b6fbb467a9a35ffb5310001d716801",
            "qimei36: 22fcc7b6fbb467a9a35ffb5310001d716801",
            "version: 10.9.8",
            "channel: 2099",
            "resolution: 2560*1440",
            "network: WIFI",
            "osversion: 8.1.0",
            "teen-state: 1",
            "gender: 1",
            "model: Nexus 6P",
            "brand: google",
            "os-bit: 64",
            "recommend-switch: 2",
            "oversea: 1",
            "userstate: 2",
            "fakeduin: 0",
            "logintype: 0",
            "accept-encoding: deflate",
            "user-agent: okhttp/3.11.0"})
    @GET("10.9.8/Chapter/chapterPictureList/hd_type/LD/comic_id/{comic_id}/preload_state/1/chapter_id/{chapter_id}")
    Observable<ResponseBody> getPictureList(@Header("localtime") String localtime,@Header("sc") String sc,@Path("comic_id") String comic_id,@Path("chapter_id") String chapter_id);
}
