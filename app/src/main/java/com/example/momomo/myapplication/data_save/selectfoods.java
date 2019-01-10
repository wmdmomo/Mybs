package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class selectfoods extends LitePalSupport {
    private int imgid;
    private int cal;
    private String time;
    private String timeid;

    public String getTimeid() {
        return timeid;
    }

    public void setTimeid(String timeid) {
        this.timeid = timeid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public int getCal() {
        return cal;
    }

    public int getImgid() {
        return imgid;
    }
}

