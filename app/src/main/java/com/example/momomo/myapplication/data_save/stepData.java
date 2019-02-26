package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class stepData extends LitePalSupport {
    private String user;
    private String time;
    private int step;

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getTime() {
        return time;
    }

    public int getStep() {
        return step;
    }
}