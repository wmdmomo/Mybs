package com.example.momomo.myapplication.home_activity;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.momomo.myapplication.Adapter.homeitem2Adapter;
import com.example.momomo.myapplication.R;
import com.example.momomo.myapplication.data_save.iconitem;

import java.util.ArrayList;
import java.util.List;

public class fragment_home extends Fragment{
    private List<iconitem> iconitemList = new ArrayList<>();
    private String[] title = {"每日打卡", "热量记录", "体重记录", "步数记录"};
    private String[] danwei = {"天", "千卡", "公斤", "步"};
    private int[] jindu = {10, 11, 12, 13};
    private int[] imgid2s = {R.drawable.ic_icon_test, R.drawable.ic_shiwu, R.drawable.ic_tizhongcheng, R.drawable.ic_yundong};
    private View view;
    private LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragmenthome,container,false);
        linearLayout = (LinearLayout) view.findViewById(R.id.home_bg);
        linearLayout.getBackground().setAlpha(100);
        RecyclerView recyclerView2 = (RecyclerView) view.findViewById(R.id.homerv2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(linearLayoutManager);
        initdata();
        homeitem2Adapter homeitem2Adapter = new homeitem2Adapter(iconitemList);
        recyclerView2.setAdapter(homeitem2Adapter);
        return view;
    }
    private void initdata() {
        for (int i = 0; i < 4; i++) {
            iconitem iconitem = new iconitem();
            iconitem.setTitle(title[i]);
            iconitem.setDes(danwei[i]);
            iconitem.setJindu(jindu[i]);
            iconitem.setImgid(imgid2s[i]);
            iconitemList.add(iconitem);
        }
    }
}