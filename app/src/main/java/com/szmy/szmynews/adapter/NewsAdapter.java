package com.szmy.szmynews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.szmy.szmynews.model.bean.NewsData;
import com.szmy.szmynews.widget.NewsItemView;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<NewsData> mList;
    private Context mContext;

    public NewsAdapter(List<NewsData> list, Context context) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(new NewsItemView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, final int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(v, mList.get(position), position);
                }
            }
        });

        holder.itemView.bindView(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        NewsItemView itemView;

        NewsViewHolder(@NonNull NewsItemView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }


    //私有属性
    private OnItemClickListener mListener = null;

    //setter方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    //回调接口
    public interface OnItemClickListener {
        void onItemClick(View v, NewsData newsBean, int position);
    }
}
