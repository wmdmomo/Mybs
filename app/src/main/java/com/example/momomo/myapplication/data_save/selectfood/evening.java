package com.example.momomo.myapplication.data_save.selectfood;

import org.litepal.crud.LitePalSupport;

public class evening extends LitePalSupport {
    private int imgid;
    private int cal;

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