package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class batteryData extends LitePalSupport {
    private String user;
    private String time;
    private int battery;

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }

    public String getTime() {
        return time;
    }

    public int getBattery() {
        return battery;
    }
}
