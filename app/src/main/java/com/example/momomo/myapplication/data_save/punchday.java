package com.example.momomo.myapplication.data_save;


import org.litepal.crud.LitePalSupport;

public class punchday extends LitePalSupport {
    private String time;
    private String user;
    private Boolean flag;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getFlag() {
        return flag;
    }

    public String getTime() {
        return time;
    }

    public String getUser() {
        return user;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
