package com.szmy.szmynews.app;

import android.app.Application;
import android.content.Context;

/**
 * 定义全局资源
 */
public class SzmyApplication extends Application {
    private static SzmyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    public static SzmyApplication instance(){
        return instance;
    }

}
