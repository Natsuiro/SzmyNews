package com.szmy.szmynews.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szmy.szmynews.R;
import com.szmy.szmynews.model.bean.SzmyNewsBean;

import java.io.Serializable;

public class NewsDetailActivity extends AppCompatActivity {

    private SzmyNewsBean newsBean = null;
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

            TextView newsTitle = findViewById(R.id.detail_newsTitle);
            newsTitle.setText(newsBean.getTitle());

            TextView newsTime = findViewById(R.id.detail_newsTime);
            newsTime.setText(newsBean.getTime());

            TextView category = findViewById(R.id.detail_category);
            category.setText(newsBean.getCategory());

            TextView newsContent = findViewById(R.id.detail_newsContent);
            newsContent.setText(newsBean.getContent());

            TextView source = findViewById(R.id.detail_source);
            source.setText(newsBean.getSrc());

            ImageView pic  = findViewById(R.id.detail_pic);
            Glide.with(this).load(newsBean.getPic()).into(pic);

        }


    }

    private void initData() {
        Intent intent = getIntent();
        Bundle newsDetail = intent.getBundleExtra("newsDetail");
        if (newsDetail != null){
            newsBean = (SzmyNewsBean) newsDetail.getSerializable("newsBean");
        }
    }
}