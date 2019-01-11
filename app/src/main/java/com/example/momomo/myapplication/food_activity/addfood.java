package com.example.momomo.myapplication.food_activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.momomo.myapplication.Adapter.fooditemAdapter;
import com.example.momomo.myapplication.Adapter.foodwatchAdapter;
import com.example.momomo.myapplication.Manager.AssetsDatabaseManager;
import com.example.momomo.myapplication.Manager.Fooddatas;
import com.example.momomo.myapplication.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.example.momomo.myapplication.data_save.fooditem;
import com.example.momomo.myapplication.data_save.selectfoods;
import com.example.momomo.myapplication.utils.LocalTime;


public class addfood extends AppCompatActivity {
    private com.example.momomo.myapplication.Adapter.fooditemAdapter fooditemAdapter;
    private AssetsDatabaseManager assetsDatabaseManager;
    private SQLiteDatabase database;
    List<fooditem> fooditemList=new ArrayList<>();
    List<fooditem> selectList=new ArrayList<>();
//    private int Cal=0;
    private TextView num;
    private ImageView enterfood;
    private int sposition;
    private int numfood=0;
    private CardView cardView;

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
        cardView=(CardView) findViewById(R.id.bottomfood);
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
                numfood=selectList.size();
//                Cal+=foodcal;
                selectfoods selectfoods=new selectfoods();
                selectfoods.setTimeid(String.valueOf(sposition));
                LocalTime localTime=new LocalTime();
                selectfoods.setTime(localTime.LocalTime());
                selectfoods.setImgid(foodimg);
                selectfoods.setCal(foodcal);
                selectfoods.save();
                if(numfood!=0){
                    cardView.setVisibility(View.VISIBLE);
                    num.setText(String.valueOf(numfood));
                }else{
                    cardView.setVisibility(View.INVISIBLE);
                }
            }
        });

        enterfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterfood.setImageResource(R.drawable.ic_tianjia1);
//                Fooddatas fooddatas=new Fooddatas();
//                foodwatchAdapter foodwatchAdapter=new foodwatchAdapter(getApplication(),fooddatas);
//                foodwatchAdapter.setThisposition(sposition);
//                foodwatchAdapter.notifyDataSetChanged();
                Intent intentFood=new Intent(addfood.this,food.class);
//                intentFood.putExtra("foodList", (Serializable) selectList);
//                intentFood.putExtra("calvalue",Cal);
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