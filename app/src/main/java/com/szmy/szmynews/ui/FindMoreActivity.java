package com.szmy.szmynews.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.szmy.szmynews.R;

public class FindMoreActivity extends AppCompatActivity {

    private Fragment contentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_more);
        initData();
        initView();
    }

    private void initData() {
        contentFragment = new NewsListFragment("头条", this);
    }

    private void initView() {
        ImageView quit = findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
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

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContent,contentFragment)
                .commit();
    }

    private void doSearch(String query) {
        //在这里转交给presenter业务层去处理
    }
}