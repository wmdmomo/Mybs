package com.example.momomo.myapplication.mine_activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.User;
import com.example.momomo.myapplication.data_save.punchday;
import com.example.momomo.myapplication.login_activity.login;
import com.example.momomo.myapplication.utils.saveVarible;

import org.litepal.LitePal;

public class fragment_mine extends Fragment {
    private View view;
    private LinearLayout linearLayout;
    private User user;
    private int userId;
    private int punchId;
    private punchday punchday;
    private LinearLayout set_punch, set_mine, set_rank, set_us,set_back;
    private TextView my_name, my_weight, my_height, my_goal;
    private String name, imgpath;
    private int weight, height, goal;
    private de.hdodenhof.circleimageview.CircleImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_fragment_mine, container, false);
        initview();
        initdata();
        initEdit();
        setEdit();
        set_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mine_set.class);
                startActivity(intent);
            }
        });
        set_rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), about_rank.class);
                startActivity(intent);
            }
        });
        set_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), about_us.class);
                startActivity(intent);
            }
        });
        set_punch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), about_punch.class);
                startActivity(intent);
            }
        });
        set_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void initview() {
        linearLayout = (LinearLayout) view.findViewById(R.id.mine_bg);
        linearLayout.getBackground().setAlpha(150);
        set_mine = (LinearLayout) view.findViewById(R.id.set_mine);
        set_rank = (LinearLayout) view.findViewById(R.id.set_rank);
        set_us = (LinearLayout) view.findViewById(R.id.set_about);
        set_punch = (LinearLayout) view.findViewById(R.id.set_punch);
        set_back = (LinearLayout) view.findViewById(R.id.set_back);
        my_name = (TextView) view.findViewById(R.id.avatar_name);
        my_weight = (TextView) view.findViewById(R.id.my_weight);
        my_height = (TextView) view.findViewById(R.id.my_height);
        my_goal = (TextView) view.findViewById(R.id.my_goal);
        imageView = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.my_img);

    }

    private void initdata() {
        final saveVarible app = (saveVarible) getActivity().getApplication();
        userId = app.getUserId();
        user = LitePal.find(User.class, userId);

    }

    private void initEdit() {
        name = user.getName();
        height = user.getHeight();
        weight = user.getWeight();
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
}