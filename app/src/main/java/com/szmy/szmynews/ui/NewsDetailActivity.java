package com.szmy.szmynews.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szmy.szmynews.R;
import com.szmy.szmynews.model.bean.SzmyNewsBean;

import java.io.Serializable;

/**
 * 展示新闻的详细内容，通过webView加载新闻的url
 */
@SuppressLint("SetJavaScriptEnabled")
public class NewsDetailActivity extends AppCompatActivity {

    private SzmyNewsBean newsBean = null;
    private static final String TAG = "NewsDetailActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initData();
        initView();
    }

    private void initView() {

        ImageView quit = findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (newsBean!=null){
            final WebView webView = findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setSupportMultipleWindows(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(newsBean.getUrl());
            Log.d(TAG, "initView: "+newsBean.getUrl());

            webView.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        //按返回键操作并且能回退网页
                        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                            //后退
                            webView.goBack();
                            return true;
                        }
                    }
                    return false;
                }

            });

        }
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle newsDetail = intent.getBundleExtra("newsDetail");
        if (newsDetail != null){
            newsBean = (SzmyNewsBean) newsDetail.getSerializable("newsBean");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}