package com.szmy.szmynews.contract;
import com.szmy.szmynews.model.bean.ChannelBean;
import com.szmy.szmynews.presenter.BasePresenter;

public interface ChannelContract {

    interface Presenter{
        void attach(DataView view);
        void detach();
        void loadChannel();
    }

    interface DataView {
        /**
         * 开始请求时
         */
        void onRequestStart();
        /**
         * 请求成功
         * 通过参数传递具体的数据
         * view层可以直接将数据展示到布局中
         * 由于布局内容基本使用列表展示，因此需要返回List集合
         * 其中已经对数据进行封装，view层可以直接使用数据
         */
        void onRequestSuccess(ChannelBean data);
        /**
         * 请求失败，可能由于参数错误，服务器异常等原因
         * view层可以获得错误码和错误信息
         */
        void onRequestFailed(String msg);
    }
}
