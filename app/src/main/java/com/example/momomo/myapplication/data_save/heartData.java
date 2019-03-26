package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class heartData extends LitePalSupport {
    private String user;
    private String time;
    private int heart;
    private String date;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }

    public String getTime() {
        return time;
    }

    public int getHeart() {
        return heart;
    }
}
