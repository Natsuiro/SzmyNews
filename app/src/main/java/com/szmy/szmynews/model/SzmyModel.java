package com.szmy.szmynews.model;

import android.os.Handler;
import android.os.Looper;

import com.szmy.szmynews.model.bean.ChannelBean;
import com.szmy.szmynews.model.bean.NewsDataBean;
import com.szmy.szmynews.model.bean.SzmyNewsBean;
import com.szmy.szmynews.retro.SzmyService;

import java.util.Objects;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SzmyModel {

    private Handler handler = new Handler(Looper.getMainLooper());

    public void runOnUiThread(Runnable r){
        handler.post(r);
    }


    private static SzmyModel instance;
    private static Retrofit retrofit;
    private SzmyModel(){}
    public static SzmyModel instance(){
        if (instance == null) {
            instance = new SzmyModel();
            retrofit = new Retrofit.Builder()
                    .baseUrl(App.getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .build();
        }
        return instance;
    }

    public void getChannel(final SzmyCallBack<ChannelBean> callBack){

        SzmyService service = retrofit.create(SzmyService.class);
        Call<ChannelBean> channelBeanCall = service.loadChannel(App.getAppKey());
        channelBeanCall.enqueue(new Callback<ChannelBean>() {
            @Override
            public void onResponse(Call<ChannelBean> call, Response<ChannelBean> response) {
                final int code = response.code();
                final String message = response.message();
                if (code==200){
                    final ChannelBean body = response.body();
                    final int status = Objects.requireNonNull(body).getStatus();
                    final String msg = body.getMsg();
                    if (status==0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onResponse(body);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailed(msg);
                            }
                        });
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(message);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<ChannelBean> call, final Throwable t) {
                t.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(t.getMessage());
                    }
                });
            }
        });
    }

    public void loadNews(String channel ,int start,int num,final SzmyCallBack<NewsDataBean> callBack){

        SzmyService service = retrofit.create(SzmyService.class);

        Call<NewsDataBean> szmyNewsBeanCall = service.loadNews(channel, num, start, App.getAppKey());

        szmyNewsBeanCall.enqueue(new Callback<NewsDataBean>() {
            @Override
            public void onResponse(Call<NewsDataBean> call, Response<NewsDataBean> response) {
                final int code = response.code();
                final String message = response.message();
                if (code==200){
                    final NewsDataBean body = response.body();
                    final int status = Objects.requireNonNull(body).getStatus();
                    final String msg = body.getMsg();
                    if (status==0){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onResponse(body);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailed(msg);
                            }
                        });
                    }
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onFailed(message);
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<NewsDataBean> call, final Throwable t) {
                t.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onFailed(t.getMessage());
                    }
                });
            }
        });
    }

    public interface SzmyCallBack<T>{
        void onResponse(T body);
        void onFailed(String msg);
    }

}
