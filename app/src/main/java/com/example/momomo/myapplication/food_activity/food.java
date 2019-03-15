package com.example.momomo.myapplication.food_activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.example.momomo.myapplication.Adapter.foodwatchAdapter;
import com.example.momomo.myapplication.Manager.Fooddatas;
import com.example.momomo.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import com.example.momomo.myapplication.data_save.fooditem;
import com.example.momomo.myapplication.data_save.foodwatch;

public class food extends AppCompatActivity {
    private List<foodwatch> foodwatchList = new ArrayList<>(4);
    private com.example.momomo.myapplication.Adapter.foodwatchAdapter foodwatchAdapter;
    private ImageView add;
    private int Position = -1;
//    private List<fooditem> resultList;
    private int calvalue;
    private Fooddatas fooddatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        Log.i("pooo", "ss:" + Position);
        Intent intent = getIntent();
        int sp = intent.getIntExtra("spvalue", -1);
        calvalue = intent.getIntExtra("calvalue", -1);
        Log.i("pooo", "传回来的值：" + Position);
        recyclerView.setLayoutManager(layoutManager);
        fooddatas=new Fooddatas();
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
                    Log.i("pooo", "改变了去传：" + Position);
                    Intent addfood = new Intent(food.this, addfood.class);
                    addfood.putExtra("positionvalue", Position);
                    startActivity(addfood);
                }
            }
        });
//        resultList = (List<fooditem>) intent.getSerializableExtra("foodList");
        foodwatchAdapter.setOnTimeClickListener(new foodwatchAdapter.OnTimeClickListener() {
            @Override
            public void onClick(int position) {
                Position = position;
                Log.i("pooo", "改变了：" + Position);
                foodwatchAdapter.setThisposition(position);
                foodwatchAdapter.notifyDataSetChanged();
            }
        });

    }
}