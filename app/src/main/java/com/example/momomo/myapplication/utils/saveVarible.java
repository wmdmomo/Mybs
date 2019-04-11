package com.example.momomo.myapplication.utils;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePal;

public class saveVarible extends Application {
    private int userId;
    private int punchId;

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
//        mContext = getApplicationContext();
//        SDKInitializer.initialize(mContext);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public int getPunchId() {
        return punchId;
    }

    public void setPunchId(int punchId) {
        this.punchId = punchId;
    }
}
