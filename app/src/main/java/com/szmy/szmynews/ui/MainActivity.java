package com.szmy.szmynews.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.szmy.szmynews.adapter.ContentAdapter;
import com.szmy.szmynews.contract.ChannelContract;
import com.szmy.szmynews.R;
import com.szmy.szmynews.model.bean.ChannelBean;
import com.szmy.szmynews.presenter.MainPresenter;

import java.util.List;



public class MainActivity extends AppCompatActivity implements ChannelContract.DataView {
    private MainPresenter presenter;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        presenter = new MainPresenter();
        presenter.attach(this);
        loadChannel();
    }

    private void loadChannel() {
        presenter.loadChannel();
    }

    @Override
    public void onRequestStart() {
        Log.d(TAG,"start load...");
    }
    @Override
    public void onRequestSuccess(ChannelBean data) {
        List<String> result = data.getResult();
        Log.d(TAG, result.toString());
        ViewPager viewPager = findViewById(R.id.contentPager);
       // ContentAdapter adapter = new ContentAdapter(result, this);
        ContentAdapter adapter = new ContentAdapter(getSupportFragmentManager(), result, this);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onRequestFailed(String msg) {
        Log.d(TAG,msg);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detach();
    }
}
