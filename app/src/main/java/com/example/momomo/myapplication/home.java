package com.example.momomo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.momomo.myapplication.Adapter.homeitemAdapter;
import com.example.momomo.myapplication.activities.MainActivity;
import com.example.momomo.myapplication.ceshi.running;
import com.example.momomo.myapplication.data_save.homeitem;
import com.example.momomo.myapplication.food_activity.food;
import com.example.momomo.myapplication.mine_activity.mine;
import com.example.momomo.myapplication.note_activity.note;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    private List<homeitem> homeitemList=new ArrayList<>();
    private  String[] words={"跑步","心率","记事本","卡路里"};
    private  int[] imgids={R.mipmap.running,R.mipmap.gym,R.mipmap.work,R.mipmap.eating};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.homerv);
        ImageView avatar=(ImageView) findViewById(R.id.avatar);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        initdata();
        homeitemAdapter homeitemAdapter=new homeitemAdapter(homeitemList);
        recyclerView.setAdapter(homeitemAdapter);
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, mine.class);
                startActivity(intent);
            }
        });
        homeitemAdapter.setOnhomeClickListener(new homeitemAdapter.OnhomeClickListener() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0:{
                        Intent intent = new Intent(home.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 1:{
                        Intent intent = new Intent(home.this, running.class);
                        startActivity(intent);
                        break;
                    }
                    case 2:{
                        Intent intent = new Intent(home.this, note.class);
                        startActivity(intent);
                        break;
                    }
                    case 3:{
                        Intent intent = new Intent(home.this, food.class);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

    }
    private void initdata(){
        for(int i=0;i<4;i++){
            homeitem homeitem=new homeitem();
            homeitem.setImgid(imgids[i]);
            homeitem.setWord(words[i]);
            homeitemList.add(homeitem);
        }
    }
}
