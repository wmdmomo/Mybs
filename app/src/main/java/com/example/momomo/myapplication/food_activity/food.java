package com.example.momomo.myapplication.food_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.momomo.myapplication.Adapter.foodwatchAdapter;
import com.example.momomo.myapplication.Manager.Fooddatas;
import com.example.momomo.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.example.momomo.myapplication.data_save.punchday;
import com.example.momomo.myapplication.data_save.selectfoods;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

public class food extends AppCompatActivity {
    private com.example.momomo.myapplication.Adapter.foodwatchAdapter foodwatchAdapter;
    private ImageView add;
    private int Position = -1;
    private int calvalue, totalvalue;
    private Fooddatas fooddatas;
    private ImageView food_sure;
    private punchday punchday;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        food_sure = (ImageView) findViewById(R.id.food_sure);
        food_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food_sure.setImageResource(R.drawable.ic_tixingfill);
                List<selectfoods> selectfoodsArrayList = new ArrayList<>();
                getTime();
                totalvalue = 0;
                selectfoodsArrayList = LitePal.where("time=?", time).find(selectfoods.class);
                for (selectfoods selectfoods : selectfoodsArrayList) {
                    totalvalue += selectfoods.getCal();
                }
                final saveVarible app = (saveVarible) getApplication();
                int punchId = app.getPunchId();
                punchday = LitePal.find(punchday.class, punchId);
                punchday.setCal(totalvalue);
                punchday.save();
                Toast.makeText(food.this, "今日摄入卡路里" + totalvalue, Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = getIntent();
        int sp = intent.getIntExtra("spvalue", -1);
        calvalue = intent.getIntExtra("calvalue", -1);
        recyclerView.setLayoutManager(layoutManager);
        fooddatas = new Fooddatas();
        foodwatchAdapter = new foodwatchAdapter(this, fooddatas);
        foodwatchAdapter.setCategoryBean(fooddatas);
        recyclerView.setAdapter(foodwatchAdapter);
        add = (ImageView) findViewById(R.id.add);
        if (sp != -1) {
            Position = sp;
            foodwatchAdapter.setThisposition(sp);
            foodwatchAdapter.notifyDataSetChanged();
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Position != -1) {
                    Intent addfood = new Intent(food.this, addfood.class);
                    addfood.putExtra("positionvalue", Position);
                    startActivity(addfood);
                }
            }
        });
        foodwatchAdapter.setOnTimeClickListener(new foodwatchAdapter.OnTimeClickListener() {
            @Override
            public void onClick(int position) {
                Position = position;
                foodwatchAdapter.setThisposition(position);
                foodwatchAdapter.notifyDataSetChanged();
            }
        });

    }

    private void getTime() {
        long times = System.currentTimeMillis();
        Date date = new Date(times);
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd");
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        time = now.format(date);
    }
}