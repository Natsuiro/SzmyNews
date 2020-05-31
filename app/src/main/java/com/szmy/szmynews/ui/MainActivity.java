package com.szmy.szmynews.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.szmy.szmynews.adapter.ContentAdapter;
import com.szmy.szmynews.contract.ChannelContract;
import com.szmy.szmynews.R;
import com.szmy.szmynews.model.bean.ChannelBean;
import com.szmy.szmynews.presenter.ChannelPresenter;

import java.util.List;



public class MainActivity extends AppCompatActivity implements ChannelContract.DataView {
    private ChannelPresenter presenter;
    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        presenter = new ChannelPresenter();
        presenter.attach(this);
        loadChannel();
        mDrawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.menuMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
                switch (item.getItemId()){
                    case R.id.nav_findMore:
                        Intent intent = new Intent(MainActivity.this, FindMoreActivity.class);
                        startActivity(intent);
                }
                return true;
            }
        });
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


    @Override
    public void onBackPressed() {
        if (mDrawerLayout != null && mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }else super.onBackPressed();

    }
}
