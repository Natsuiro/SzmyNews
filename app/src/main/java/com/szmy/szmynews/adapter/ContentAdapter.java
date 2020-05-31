package com.szmy.szmynews.adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.szmy.szmynews.ui.ContentFragment;

import java.util.HashMap;
import java.util.List;

public class ContentAdapter extends FragmentPagerAdapter {

    private static final String TAG = "MyAdapter";
    private List<String> mTitles ;
    private Context mContext;

    public ContentAdapter(@NonNull FragmentManager fm, List<String> titles, Context context) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mTitles = titles;
        mContext = context;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d(TAG, "getItem: "+position);
        String key = mTitles.get(position);
        return new ContentFragment(key, mContext);
    }
    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
