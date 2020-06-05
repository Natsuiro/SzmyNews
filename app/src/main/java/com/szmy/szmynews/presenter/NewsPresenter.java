package com.szmy.szmynews.presenter;

import android.util.Log;

import com.szmy.szmynews.contract.NewsContract;
import com.szmy.szmynews.model.SzmyModel;
import com.szmy.szmynews.model.bean.NewsBean;

public class NewsPresenter extends BasePresenter implements NewsContract.Presenter {

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

        SzmyModel.instance().loadNews(channel, start, num, new SzmyModel.SzmyCallBack<NewsBean>() {
            @Override
            public void onResponse(final NewsBean body) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onLoadSuccess(body);
                    }
                });

            }

            @Override
            public void onFailed(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onLoadFailed(msg);
                    }
                });

            }
        });

    }
}
