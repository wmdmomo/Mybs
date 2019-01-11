package com.example.momomo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.momomo.myapplication.Adapter.homeitemAdapter;
import com.example.momomo.myapplication.data_save.homeitem;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    private List<homeitem> homeitemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.homerv);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initdata();
        homeitemAdapter homeitemAdapter=new homeitemAdapter(homeitemList);
        recyclerView.setAdapter(homeitemAdapter);
    }
    private void initdata(){
        for(int i=0;i<4;i++){
            homeitem homeitem=new homeitem();
            homeitem.setImgid(R.mipmap.bg1);
            homeitemList.add(homeitem);
        }
    }
}
