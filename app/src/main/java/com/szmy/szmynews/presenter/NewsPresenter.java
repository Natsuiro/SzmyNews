package com.szmy.szmynews.presenter;

import android.util.Log;

import com.szmy.szmynews.contract.NewsContract;
import com.szmy.szmynews.model.SzmyModel;
import com.szmy.szmynews.model.bean.NewsDataBean;

public class NewsPresenter implements NewsContract.Presenter {

    private NewsContract.DataView mView;
    private static final String TAG = "NewsPresenter";

    @Override
    public void attach(NewsContract.DataView view) {
        this.mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public void loadNews(String channel,int start,int num) {
        if (mView != null){
            mView.onRequestStart();
            exeLoadNews(channel, start, num);
        }else {
            Log.e(TAG,"you have not attached a DataView with it");
        }
    }

    private void exeLoadNews(String channel,int start,int num) {

        SzmyModel.instance().loadNews(channel, start, num, new SzmyModel.SzmyCallBack<NewsDataBean>() {
            @Override
            public void onResponse(NewsDataBean body) {
                mView.onLoadSuccess(body);
            }

            @Override
            public void onFailed(String msg) {
                mView.onLoadFailed(msg);
            }
        });

    }
}
