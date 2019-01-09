package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public class Notes extends LitePalSupport implements Serializable {
    private String time;
    private String title;
    private String content;
    public void setTime(String time) {
        this.time = time;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getTime() {
        return time;
    }
    public String getContent() {
        return content;
    }
    public String getTitle() {
        return title;
    }
}
