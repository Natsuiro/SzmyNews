package com.szmy.szmynews.presenter;

import com.szmy.szmynews.contract.ChannelContract;
import com.szmy.szmynews.model.SzmyModel;
import com.szmy.szmynews.model.bean.ChannelBean;




public class ChannelPresenter extends BasePresenter implements ChannelContract.Presenter {

    private ChannelContract.DataView mView;

    @Override
    public void attach(ChannelContract.DataView view) {
        this.mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }
    @Override
    public void loadChannel(){
        mView.onRequestStart();
        exeLoadChannel();
    }
    private void exeLoadChannel() {
        SzmyModel.instance().getNewsChannel(new SzmyModel.SzmyCallBack<ChannelBean>() {
            @Override
            public void onResponse(final ChannelBean body) {
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
