package com.szmy.szmynews.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.szmy.szmynews.R;
import com.szmy.szmynews.adapter.SearchDataAdapter;
import com.szmy.szmynews.contract.SearchContract;
import com.szmy.szmynews.model.bean.SearchBean;
import com.szmy.szmynews.model.bean.SearchData;
import com.szmy.szmynews.presenter.SearchPresenter;

import java.util.ArrayList;
import java.util.List;

public class FindMoreActivity extends AppCompatActivity implements SearchContract.DataView {

    private SearchPresenter presenter;
    private ProgressBar progress;
    private List<SearchData> mList;
    private SearchDataAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_more);
        initData();
        initView();
    }

    private void initData() {
        presenter = new SearchPresenter();
        presenter.attach(this);
        mList = new ArrayList<>();
        mAdapter = new SearchDataAdapter(mList,this);
        mAdapter.setOnItemClickListener(new SearchDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, SearchData data, int position) {
                NewsDetailActivity.start(data.getUrl(),FindMoreActivity.this);
            }
        });
    }

    private void initView() {
        findViewById(R.id.quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SearchView searchView = findViewById(R.id.searchKeyWord);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //提交查询
                if (query!=null){
                    Toast.makeText(getApplicationContext(),"正在查询",Toast.LENGTH_LONG).show();
                    doSearch(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mRecyclerView = findViewById(R.id.recyclerContent);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        progress = findViewById(R.id.loading);
        doSearch("头条");
    }

    private void doSearch(String query) {
        //在这里转交给presenter业务层去处理
        presenter.searchWithKeWord(query);
    }
    @Override
    public void onRequestStart() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestSuccess(SearchBean data) {
        progress.setVisibility(View.GONE);
        mList.clear();
        mList.addAll(data.getResult().getList());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestFailed(String msg) {
        progress.setVisibility(View.GONE);
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        presenter.detach();
        super.onDestroy();
    }
}