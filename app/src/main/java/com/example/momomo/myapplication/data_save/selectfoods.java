package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class selectfoods extends LitePalSupport {
    private String user;
    private double cal;
    private String time;
    private int timeid;
    private String imgpath;
    private String name;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }

    public int getTimeid() {
        return timeid;
    }

    public void setTimeid(int timeid) {
        this.timeid = timeid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCal(double cal) {
        this.cal = cal;
    }

    public double getCal() {
        return cal;
    }
}

