package com.example.momomo.myapplication.mine_activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.Manager.ActivityCollector;
import com.example.momomo.myapplication.Manager.BaseActivity;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.home;
import com.example.momomo.myapplication.login_activity.login;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

public class mine extends BaseActivity {
    private LinearLayout linearLayout;
    private User user;
    private int userId;
    private LinearLayout set_punch, set_mine, set_rank, set_us;
    private TextView my_name, my_weight, my_height, my_goal;
    private String name, imgpath;
    private int  height;
    private double weight,goal;
    private de.hdodenhof.circleimageview.CircleImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        initview();
        initdata();
        initEdit();
        setEdit();
        set_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mine.this, mine_set.class);
                startActivity(intent);
            }
        });
        set_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mine.this, about_rank.class);
                startActivity(intent);
            }
        });
        set_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mine.this, about_us.class);
                startActivity(intent);
            }
        });
        set_punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mine.this, about_punch.class);
                startActivity(intent);
            }
        });
    }

    private void initview() {
        linearLayout = (LinearLayout) findViewById(R.id.mine_bg);
        linearLayout.getBackground().setAlpha(150);
        set_mine = (LinearLayout) findViewById(R.id.set_mine);
        set_rank = (LinearLayout)findViewById(R.id.set_rank);
        set_us = (LinearLayout) findViewById(R.id.set_about);
        set_punch = (LinearLayout) findViewById(R.id.set_punch);
        my_name = (TextView) findViewById(R.id.avatar_name);
        my_weight = (TextView) findViewById(R.id.my_weight);
        my_height = (TextView) findViewById(R.id.my_height);
        my_goal = (TextView) findViewById(R.id.my_goal);
        imageView = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.my_img);

    }

    private void initdata() {
        final saveVarible app = (saveVarible) getApplication();
        userId = app.getUserId();
        user = LitePal.find(User.class, userId);

    }

    private void initEdit() {
        name = user.getName();
        height = user.getHeight();
        weight = user.getNow_weight();
        goal = user.getGoal_weight();
        imgpath = user.getAvatar_path();
    }

    private void setEdit() {
        my_name.setText(name);
        my_height.setText(String.valueOf(height));
        my_weight.setText(String.valueOf(weight));
        if (!imgpath.equals(" ")) Glide.with(this).load(imgpath).into(imageView);
        else {
            int resourceId = R.mipmap.avatar;
            Glide.with(this)
                    .load(resourceId)
                    .into(imageView);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(mine.this, home.class);
        startActivity(intent);
    }

}
