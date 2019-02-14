package com.example.momomo.myapplication.utils;

import android.app.Application;

import org.litepal.LitePal;

public class saveVarible extends Application {
    private int userId;
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
