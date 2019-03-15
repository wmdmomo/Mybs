package com.example.momomo.myapplication.mine_activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.momomo.myapplication.Adapter.noteAdapter;
import com.example.momomo.myapplication.Adapter.rankitemAdapter;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class about_rank extends AppCompatActivity {
    private rankitemAdapter rankitemAdapter;
    List<User> userList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_rank);
        initRecy();
    }
    private void initRecy(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rank_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        userList= LitePal.order("punch desc").find(User.class);
        rankitemAdapter = new rankitemAdapter(this,userList);
        recyclerView.setAdapter(rankitemAdapter);
    }
}
