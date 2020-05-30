package com.szmy.szmynews.retro;

import com.szmy.szmynews.model.bean.ChannelBean;
import com.szmy.szmynews.model.bean.NewsDataBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SzmyService {
    @GET("channel")
    Call<ChannelBean> loadChannel(@Query("appkey") String appkey);

    @GET("get")
    Call<NewsDataBean> loadNews(@Query("channel") String channel,
                                @Query("num") int num,
                                @Query("start") int start,
                                @Query("appkey") String appkey
    );

}
