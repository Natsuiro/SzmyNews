package com.szmy.szmynews.presenter;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.szmy.szmynews.contract.ChannelContract;
import com.szmy.szmynews.contract.SearchContract;
import com.szmy.szmynews.model.SzmyModel;
import com.szmy.szmynews.model.bean.ChannelBean;
import com.szmy.szmynews.model.bean.SearchBean;

public class SearchPresenter extends BasePresenter implements SearchContract.Presenter {
    private SearchContract.DataView mView;

    @Override
    public void attach(SearchContract.DataView view) {
        this.mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public void searchWithKeWord(String keyWord) {
        if (TextUtils.isEmpty(keyWord)) return;
        mView.onRequestStart();
        execSearch(keyWord);
    }

    private void execSearch(String keyWord) {
        SzmyModel.instance().searchWithKeyWord(keyWord, new SzmyModel.SzmyCallBack<SearchBean>() {
            @Override
            public void onResponse(final SearchBean body) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onRequestSuccess(body);
                    }
                });

            }
            @Override
            public void onFailed(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onRequestFailed(msg);
                    }
                });

            }
        });
    }
}
