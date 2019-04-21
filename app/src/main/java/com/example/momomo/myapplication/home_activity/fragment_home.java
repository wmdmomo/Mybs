package com.example.momomo.myapplication.home_activity;


import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.View;

import android.widget.LinearLayout;

import com.example.momomo.myapplication.Adapter.homeitem2Adapter;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.foodcal;
import com.example.momomo.myapplication.data_save.iconitem;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class fragment_home extends AppCompatActivity {
    private List<iconitem> iconitemList = new ArrayList<>();
    private String[] title = {"每日打卡", "热量记录", "体重记录", "步数记录"};
    private String[] danwei = {"天", "卡", "公斤", "步"};
    private int[] sizes = {0, 0, 0, 0};
    private int[] imgid2s = {R.drawable.ic_icon_test, R.drawable.ic_shiwu, R.drawable.ic_tizhongcheng, R.drawable.ic_yundong};
    private View view;
    private LinearLayout linearLayout;
    private User user;
    private String username;
    private double cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenthome);
        linearLayout = (LinearLayout) findViewById(R.id.home_bg);
        linearLayout.getBackground().setAlpha(100);
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.homerv2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(linearLayoutManager);
        initdata();
        homeitem2Adapter homeitem2Adapter = new homeitem2Adapter(iconitemList);
        recyclerView2.setAdapter(homeitem2Adapter);

    }

    private void initdata() {
        final saveVarible app = (saveVarible)getApplication();
        int userId = app.getUserId();

        user = LitePal.find(User.class, userId);
        username = user.getName();
        List<foodcal> foodDataList = new ArrayList<>();
        foodDataList = LitePal.where("user=?", username).find(foodcal.class);
        if (foodDataList.size() != 0) {
            cal = foodDataList.get(foodDataList.size() - 1).getCal();
        } else {
            cal=0;
        }
        sizes[0] = user.getPunch();
        sizes[1] = (int) cal;
        sizes[2] = user.getWeight();
        sizes[3] = 0;

        for (int i = 0; i < 4; i++) {
            iconitem iconitem = new iconitem();
            iconitem.setTitle(title[i]);
            iconitem.setDes(danwei[i]);
            iconitem.setSize(sizes[i]);
            iconitem.setImgid(imgid2s[i]);
            iconitemList.add(iconitem);
        }
    }



}
