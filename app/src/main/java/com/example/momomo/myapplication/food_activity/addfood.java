package com.example.momomo.myapplication.food_activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.momomo.myapplication.Adapter.fooditemAdapter;
import com.example.momomo.myapplication.Manager.AssetsDatabaseManager;
import com.example.momomo.myapplication.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.momomo.myapplication.data_save.fooditem;
import com.example.momomo.myapplication.data_save.selectfood.afternoon;
import com.example.momomo.myapplication.data_save.selectfood.evening;
import com.example.momomo.myapplication.data_save.selectfood.morning;
import com.example.momomo.myapplication.data_save.selectfood.noon;


public class addfood extends AppCompatActivity {
    private com.example.momomo.myapplication.Adapter.fooditemAdapter fooditemAdapter;
    private AssetsDatabaseManager assetsDatabaseManager;
    private SQLiteDatabase database;
    List<fooditem> fooditemList=new ArrayList<>();
    List<fooditem> selectList=new ArrayList<>();
    private int Cal=0;
    private TextView num;
    private ImageView enterfood;
    private int sposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addfood);
        //这里导入了food外部的数据库！！！！
        assetsDatabaseManager=new AssetsDatabaseManager(this);
        assetsDatabaseManager.copyDatabase();
        database = SQLiteDatabase.openOrCreateDatabase(AssetsDatabaseManager.DB_PATH + "/" + AssetsDatabaseManager.DB_NAME, null);
        fooditemList=getfood();
        RecyclerView recyclerView=(RecyclerView) findViewById(R.id.rv);
        num=(TextView)findViewById(R.id.num);
        enterfood=(ImageView) findViewById(R.id.enterfood);
        GridLayoutManager layoutManager=new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        fooditemAdapter=new fooditemAdapter(this,fooditemList);
        recyclerView.setAdapter(fooditemAdapter);
        Intent intent = getIntent();
        final int sposition=intent.getIntExtra("positionvalue",-1);
        fooditemAdapter.setOnItemClickListener(new fooditemAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                final fooditem fooditem=fooditemList.get(position);
                final String foodname=fooditem.getName();
                final int foodcal=fooditem.getCal();
                final int foodimg=fooditem.getImgid();
                Log.i("pooo","ss:"+foodimg);
                selectList.add(new fooditem(foodname,foodcal,foodimg));
                num.setText(String.valueOf(selectList.size()));
                Cal+=foodcal;
                switch (sposition) {
                    case 0: {
                        morning morningFoods = new morning();
                        morningFoods.setImgid(foodimg);
                        morningFoods.setCal(foodcal);
                        morningFoods.save();
                        break;
                    }
                    case 1: {
                        noon noonFoods = new noon();
                        noonFoods.setImgid(foodimg);
                        noonFoods.setCal(foodcal);
                        noonFoods.save();
                        break;
                    }
                    case 2: {
                        afternoon afternoonFoods = new afternoon();
                        afternoonFoods.setImgid(foodimg);
                        afternoonFoods.setCal(foodcal);
                        afternoonFoods.save();
                        break;
                    }
                    case 3: {
                        evening eveningFoods = new evening();
                        eveningFoods.setImgid(foodimg);
                        eveningFoods.setCal(foodcal);
                        eveningFoods.save();
                        break;
                    }
                }

            }
        });
        enterfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFood=new Intent(addfood.this,food.class);
                intentFood.putExtra("foodList", (Serializable) selectList);
                intentFood.putExtra("calvalue",Cal);
                intentFood.putExtra("spvalue",sposition);
                startActivity(intentFood);
            }
        });
    }
    private List<fooditem> getfood() {

        Cursor cur = database.rawQuery("SELECT * FROM fooditem", null);

        if (cur != null) {
            cur.moveToFirst();
            int NUM = cur.getCount();
            List<fooditem> food = new ArrayList<>(NUM);
            for(int i=0;i<NUM;i++) {
                cur.moveToPosition(i);
                String name = cur.getString(cur.getColumnIndex("name"));
                String imgid = cur.getString(cur.getColumnIndex("imgid"));
                int cal = cur.getInt(cur.getColumnIndex("cal"));
                ApplicationInfo appInfo = getApplicationInfo();
                int id =getResources().getIdentifier(imgid, "drawable", appInfo.packageName);
                fooditem foo = new fooditem(name,cal,id);
                food.add(foo);
            }
            return food;
        } else {
            return null;
        }
    }
}