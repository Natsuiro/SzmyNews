package com.szmy.szmynews.presenter;

import android.os.Handler;
import android.os.Looper;

public class BasePresenter {
    private Handler handler = new Handler(Looper.getMainLooper());
    protected void runOnUiThread(Runnable r) {
        handler.post(r);
    }
}
