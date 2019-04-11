package com.example.momomo.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import com.example.momomo.myapplication.food_activity.dish_home;
import com.example.momomo.myapplication.map_activity.MapMainActivity;
import com.example.momomo.myapplication.sports_activity.SportActivity;
import com.example.momomo.myapplication.home_activity.fragment_home;
import com.example.momomo.myapplication.mine_activity.fragment_mine;
import com.example.momomo.myapplication.note_activity.note;


public class home extends AppCompatActivity {
    private Fragment currentFragment = new Fragment();
    private fragment_mine fragmentMine = new fragment_mine();
    private fragment_home fragmentHome = new fragment_home();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        switchFragment(fragmentHome).commit();
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottomBar);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setBarBackgroundColor(R.color.white);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ic_icon_home, "主页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_xinlv, "运动"))
                .addItem(new BottomNavigationItem(R.drawable.ic_map, "地图"))
                .addItem(new BottomNavigationItem(R.drawable.ic_wo, "我的"))
                .addItem(new BottomNavigationItem(R.drawable.ic_shiwu2, "卡路里"))
                .addItem(new BottomNavigationItem(R.drawable.ic_note, "计划"))
                .setFirstSelectedPosition(0)
                .initialise();
        int id = getIntent().getIntExtra("id", 0);
        if(id==1){
            switchFragment(fragmentMine).commit();
        }
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0: {
                        switchFragment(fragmentHome).commit();
                        break;
                    }
                    case 1: {
                        Intent intent = new Intent(home.this, SportActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 2: {
                        Intent intent = new Intent(home.this, MapMainActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case 3: {
                        switchFragment(fragmentMine).commit();
                        break;
                    }
                    case 4: {
                        Intent intent = new Intent(home.this, dish_home.class);
                        startActivity(intent);
                        break;
                    }
                    case 5: {
                        Intent intent = new Intent(home.this, note.class);
                        startActivity(intent);
                        break;
                    }
                }

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fragment, targetFragment, targetFragment.getClass().getName());

        } else {
            transaction
                    .hide(currentFragment)
                    .show(targetFragment);
        }
        currentFragment = targetFragment;
        return transaction;
    }

}
