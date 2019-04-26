package com.example.momomo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.example.momomo.myapplication.Adapter.homeitem2Adapter;
import com.example.momomo.myapplication.Manager.ActivityCollector;
import com.example.momomo.myapplication.Manager.BaseActivity;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.foodcal;
import com.example.momomo.myapplication.data_save.iconitem;
import com.example.momomo.myapplication.data_save.stepData;
import com.example.momomo.myapplication.food_activity.dish_home;
import com.example.momomo.myapplication.sports_activity.SportActivity;
import com.example.momomo.myapplication.mine_activity.mine;
import com.example.momomo.myapplication.note_activity.note;
import com.example.momomo.myapplication.utils.saveVarible;
import com.github.lzyzsd.circleprogress.ArcProgress;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


public class home extends BaseActivity {
    private List<iconitem> iconitemList = new ArrayList<>();
    private String[] title = {"每日打卡", "热量记录", "体重记录", "步数记录"};
    private String[] danwei = {"天", "卡", "公斤", "步"};
    private int[] sizes = {0, 0, 0, 0};
    private int[] imgid2s = {R.drawable.ic_icon_test, R.drawable.ic_shiwu, R.drawable.ic_tizhongcheng, R.drawable.ic_yundong};
    private LinearLayout linearLayout;
    private User user;
    private String username;
    private double cal;
    private LinearLayout l1, l2, l3, l4, l5;
    private int step;
    private String time;

    private int age;
    private String sex;
    private double weight;
    private  int height;
    private double bmr;
    private double origin_weight ,goal_weight;

    private ArcProgress arcProgress;


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
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, SportActivity.class);
                intent.putExtra("BMR",bmr);
                intent.putExtra("CAL",cal);
                intent.putExtra("STEP",step);
                startActivity(intent);
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, mine.class);
                startActivity(intent);
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, dish_home.class);
                startActivity(intent);
            }
        });
        l5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, note.class);
                startActivity(intent);
            }
        });

    }


    //    private FragmentTransaction switchFragment(Fragment targetFragment) {
//
//        FragmentTransaction transaction = getSupportFragmentManager()
//                .beginTransaction();
//        if (!targetFragment.isAdded()) {
//            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
//            if (currentFragment != null) {
//                transaction.hide(currentFragment);
//            }
//            transaction.add(R.id.fragment, targetFragment, targetFragment.getClass().getName());
//
//        } else {
//            transaction
//                    .hide(currentFragment)
//                    .show(targetFragment);
//        }
//        currentFragment = targetFragment;
//        return transaction;
//    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("ttt", "5555");
        ActivityCollector.finishAll();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initdata() {
        l1 = (LinearLayout) findViewById(R.id.tab1);
        l2 = (LinearLayout) findViewById(R.id.tab2);
        l3 = (LinearLayout) findViewById(R.id.tab3);
        l4 = (LinearLayout) findViewById(R.id.tab4);
        l5=(LinearLayout) findViewById(R.id.tab5);
        arcProgress=(ArcProgress) findViewById(R.id.arc_progress);

        final saveVarible app = (saveVarible) getApplication();
        int userId = app.getUserId();
        getTime();
        user = LitePal.find(User.class, userId);
        goal_weight=user.getGoal_weight();
        origin_weight=user.getWeight();

        //计算用户的BMR
        age=user.getAge();
        sex=user.getSex();
        height=user.getHeight();
        weight=user.getNow_weight();

        if(sex.equals("男")){
            bmr=(13.7*weight)+(5.0*height)-(6.8*age)+66;
        }else{
            bmr=(9.6*weight)+(1.8*height)-(4.7*age)+655;
        }

        int jindu;
        jindu=(int)((origin_weight-weight)/(origin_weight-goal_weight)*100);
        arcProgress.setProgress(jindu);
        username = user.getName();
        List<foodcal> foodDataList = new ArrayList<>();
        foodDataList = LitePal.where("user=? and time=?", username,time).find(foodcal.class);
        if (foodDataList.size() != 0) {
            cal = foodDataList.get(foodDataList.size() - 1).getCal();
        } else {
            cal = 0;
        }
        List<stepData> stepDataList = new ArrayList<>();
        stepDataList = LitePal.where("user=? and date=?", username,time).find(stepData.class);
        if (stepDataList.size() != 0) {
            step = stepDataList.get(stepDataList.size() - 1).getStep();
        } else {
            step = 0;
        }
        sizes[0] = user.getPunch();
        sizes[1] = (int) cal;
        sizes[2] = (int)weight;
        sizes[3] = step;

        for (int i = 0; i < 4; i++) {
            iconitem iconitem = new iconitem();
            iconitem.setTitle(title[i]);
            iconitem.setDes(danwei[i]);
            iconitem.setSize(sizes[i]);
            iconitem.setImgid(imgid2s[i]);
            iconitemList.add(iconitem);
        }
    }
    private void getTime() {
        long times = System.currentTimeMillis();
        Date date = new Date(times);
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd");
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        time = now.format(date);
    }

}
