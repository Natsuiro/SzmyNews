package com.szmy.szmynews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.szmy.szmynews.model.bean.SzmyNewsBean;
import com.szmy.szmynews.widget.NewsItemView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


    private List<SzmyNewsBean> mList;
    private Context mContext;
    public NewsAdapter(List<SzmyNewsBean> list,Context context){
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(new NewsItemView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.itemView.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder{
        NewsItemView itemView;
        NewsViewHolder(@NonNull NewsItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
