package com.example.momomo.myapplication.data_save;


import java.io.Serializable;

public class fooditem implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int cal;
    private int imgid;

    public int getCal() {
        return cal;
    }

    public String getName() {
        return name;
    }

    public int getImgid() {
        return imgid;
    }

    public fooditem(String name, int cal, int imgid) {
        this.name = name;
        this.cal = cal;
        this.imgid = imgid;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public void setName(String name) {
        this.name = name;
    }
}
