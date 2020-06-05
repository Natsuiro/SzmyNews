package com.szmy.szmynews.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.szmy.szmynews.model.bean.NewsData;
import com.szmy.szmynews.model.bean.SearchData;
import com.szmy.szmynews.widget.NewsItemView;
import com.szmy.szmynews.widget.SearchDataItemView;

import java.util.List;

public class SearchDataAdapter extends RecyclerView.Adapter<SearchDataAdapter.SearchViewHolder> {
    private List<SearchData> mList;
    private Context mContext;

    public SearchDataAdapter(List<SearchData> list, Context context) {
        mList = list;
        mContext = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(new SearchDataItemView(mContext));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, final int position) {

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

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        SearchDataItemView itemView;
        SearchViewHolder(@NonNull SearchDataItemView itemView) {
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
        void onItemClick(View v, SearchData data, int position);
    }

}
