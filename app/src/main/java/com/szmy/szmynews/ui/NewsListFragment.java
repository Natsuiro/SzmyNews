package com.szmy.szmynews.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.szmy.szmynews.R;
import com.szmy.szmynews.adapter.NewsAdapter;
import com.szmy.szmynews.contract.NewsContract;
import com.szmy.szmynews.model.bean.NewsDataBean;
import com.szmy.szmynews.model.bean.SzmyNewsBean;
import com.szmy.szmynews.model.bean.SzmyResult;
import com.szmy.szmynews.presenter.NewsPresenter;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment implements NewsContract.DataView {
    //每一个页面的形式是一样的，根据keyWord来选择加载哪些数据
    private String mKeyWord;
    private Context mContext;
    private View mViewRoot;
    private NewsAdapter newsAdapter;
    private List<SzmyNewsBean> mDataList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerListener mListener;
    private int cur = 0;
    private int num = 10;
    private NewsPresenter presenter;
    private boolean mHaveLoadData;
    private static final String TAG = "ContentFragment";

    public NewsListFragment(String keyWord, Context context) {
        this.mKeyWord = keyWord;
        this.mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewRoot = View.inflate(mContext, R.layout.content_layout, null);
        presenter = new NewsPresenter();
        presenter.attach(this);

        return mViewRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initData();
        initView();
        loadNews();
    }

    private void initData() {
        mDataList = new ArrayList<>();
        newsAdapter = new NewsAdapter(mDataList, mContext);
        newsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, SzmyNewsBean newsBean, int position) {
                //根据点击的item打开详情页面展示更多数据
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsBean",newsBean);
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("newsDetail",bundle);
                startActivity(intent);
            }
        });
        mListener = new RecyclerListener();
    }

    private void initView() {
        recyclerView = mViewRoot.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(newsAdapter);
        recyclerView.addOnScrollListener(mListener);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });


        refreshLayout = mViewRoot.findViewById(R.id.swipeRefresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mDataList.clear();
                cur = 0;
                loadNews();
            }
        });
    }

    private void loadNews() {
        Log.d(TAG, "loadNews: load" + mKeyWord);
        presenter.loadNews(mKeyWord, 0, num);
    }

    private void stopRefreshing() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }

    private void startRefreshing(){
        if (!refreshLayout.isRefreshing()) refreshLayout.setRefreshing(true);
    }

    @Override
    public void onRequestStart() {
        Log.d(TAG, "onRequestStart: ");
        startRefreshing();
    }

    @Override
    public void onLoadSuccess(NewsDataBean data) {
        SzmyResult result = data.getResult();
        cur += result.getNum();
        int lastPosition = mDataList.size();
        mDataList.addAll(result.getList());
        newsAdapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(lastPosition);

        stopRefreshing();
    }

    @Override
    public void onLoadFailed(String msg) {
        Log.d(TAG, "onRequestFailed: " + msg);
        stopRefreshing();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.removeOnScrollListener(mListener);
        presenter.detach();
    }

    private void loadMore(){
        Log.d(TAG, "loadNews: load" + mKeyWord);
        if (cur<400){
            presenter.loadNews(mKeyWord, cur, num);
        }else{
            Toast.makeText(mContext,"no more data",Toast.LENGTH_SHORT).show();
        }

    }

    private class RecyclerListener extends RecyclerView.OnScrollListener {
        //用来标记是否正在向最后一个滑动
        boolean isSlidingDown = false;

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
            isSlidingDown = dy > 0;
        }

        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            // 当不滚动时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                //获取最后一个完全显示的ItemPosition
                int lastVisibleItem = 0;
                if (layoutManager != null) {
                    lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    int totalItemCount = layoutManager.getItemCount();
                    // 判断是否滚动到倒数第二个的时候，并且是向右滚动
                    if (lastVisibleItem == totalItemCount - 1 && isSlidingDown) {
                        //加载更多功能的代码
                        loadMore();
                    }
                }

            }
        }
    }

}
