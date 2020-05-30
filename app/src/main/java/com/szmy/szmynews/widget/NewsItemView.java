package com.szmy.szmynews.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.szmy.szmynews.R;
import com.szmy.szmynews.model.bean.SzmyNewsBean;

public class NewsItemView extends FrameLayout {
    public NewsItemView(@NonNull Context context) {
        super(context);
        View.inflate(context, R.layout.item_news_layout,this);
    }

    public void bindView(SzmyNewsBean news){
        if (news!=null){
            TextView newsTitle = findViewById(R.id.newsTitle);
            newsTitle.setText(news.getTitle());

            TextView newsTime = findViewById(R.id.newsTime);
            newsTime.setText(news.getTime());

            TextView category = findViewById(R.id.category);
            category.setText(news.getCategory());

            TextView newsContent = findViewById(R.id.newsContent);
            newsContent.setText(news.getContent().substring(0,100));

            TextView source = findViewById(R.id.source);
            source.setText(news.getSrc());

            ImageView pic  = findViewById(R.id.pic);
            Glide.with(this).load(news.getPic()).into(pic);

        }

    }
}
