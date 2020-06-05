package com.szmy.szmynews.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.szmy.szmynews.R;
import com.szmy.szmynews.model.bean.NewsData;
import com.szmy.szmynews.model.bean.SearchData;

public class SearchDataItemView extends FrameLayout {

    public SearchDataItemView(@NonNull Context context) {
        super(context);
        View.inflate(context, R.layout.item_search_data_layout,this);
    }

    public void bindView(SearchData data){
        if (data!=null){
            TextView newsTitle = findViewById(R.id.newsTitle);
            TextView category = findViewById(R.id.category);
            TextView newsTime = findViewById(R.id.newsTime);
            ImageView pic = findViewById(R.id.pic);
            TextView newsContent = findViewById(R.id.newsContent);
            TextView source = findViewById(R.id.source);

            newsTitle.setText(data.getTitle());
            category.setText(data.getCategory());
            newsTime.setText(data.getTime());
            if (data.getPic()!=null){
                Glide.with(this).load(data.getPic()).into(pic);
            }else{
                pic.setVisibility(View.GONE);
            }
            newsContent.setText(data.getContent().substring(0,100));
            source.setText(data.getSrc());
        }

    }
}
