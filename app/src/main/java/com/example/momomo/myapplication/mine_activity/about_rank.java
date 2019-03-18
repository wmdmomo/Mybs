package com.example.momomo.myapplication.mine_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.momomo.myapplication.Adapter.noteAdapter;
import com.example.momomo.myapplication.Adapter.rankitemAdapter;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class about_rank extends AppCompatActivity {
    private rankitemAdapter rankitemAdapter;
    private android.support.v7.widget.Toolbar toolbar;
    List<User> userList = new ArrayList<>();
    private int userId;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_rank);
        initBar();
        initRecy();
    }

    private void initRecy() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rank_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        userList = LitePal.order("punch desc").find(User.class);
        rankitemAdapter = new rankitemAdapter(this, userList);
        recyclerView.setAdapter(rankitemAdapter);
        rankitemAdapter.setOnRankClickListener(new rankitemAdapter.OnRankClickListener() {
            @Override
            public void onClick(int position) {
                rankitemAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initBar() {
        toolbar = (Toolbar) findViewById(R.id.about_rank_bar);
        TextView textView = (TextView) findViewById(R.id.rank_title);
        textView.setText("排行榜");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//显示左边的Home图标为返回按钮
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //这个就是toolbar的返回按钮空间的id
                onBackPressed();
                break;
        }
        return true; //告诉系统我们自己处理了点击事件
    }
}
