package com.szmy.szmynews.presenter;

import com.szmy.szmynews.contract.ChannelContract;
import com.szmy.szmynews.model.SzmyModel;
import com.szmy.szmynews.model.bean.ChannelBean;




public class MainPresenter implements ChannelContract.Presenter {

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
        SzmyModel.instance().getChannel(new SzmyModel.SzmyCallBack<ChannelBean>() {
            @Override
            public void onResponse(ChannelBean body) {
                mView.onRequestSuccess(body);
            }

            @Override
            public void onFailed(String msg) {
                mView.onRequestFailed(msg);
            }
        });
    }

}
