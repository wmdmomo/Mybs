package com.example.momomo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.momomo.myapplication.Adapter.homeitemAdapter;
import com.example.momomo.myapplication.Adapter.hometopAdapter;
import com.example.momomo.myapplication.data_save.homeitem;
import com.example.momomo.myapplication.data_save.hometopitem;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    private List<homeitem> homeitemList=new ArrayList<>();
    private List<hometopitem> hometopitemList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.homerv);
        RecyclerView recyclerView1=(RecyclerView) findViewById(R.id.homerv2);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView1.setLayoutManager(layoutManager);
        initdata();
        homeitemAdapter homeitemAdapter=new homeitemAdapter(homeitemList);
        recyclerView.setAdapter(homeitemAdapter);
        hometopAdapter hometopAdapter=new hometopAdapter(hometopitemList);
        recyclerView1.setAdapter(hometopAdapter);

    }
    private void initdata(){
        for(int i=0;i<4;i++){
            homeitem homeitem=new homeitem();
            homeitem.setImgid(R.mipmap.bg1);
            homeitemList.add(homeitem);
        }
        for(int i=0;i<6;i++){
            hometopitem hometopitem=new hometopitem();
            hometopitem.setImgid(R.mipmap.bg2);
            hometopitem.setTitle("饮食");
            hometopitemList.add(hometopitem);
        }
    }
}
