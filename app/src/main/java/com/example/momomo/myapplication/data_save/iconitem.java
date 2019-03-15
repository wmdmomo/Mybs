package com.example.momomo.myapplication.data_save;

public class iconitem {
    private int imgid;
    private String title;
    private String des;
    private int size;
    private int jindu;

    public int getJindu() {
        return jindu;
    }

    public void setJindu(int jindu) {
        this.jindu = jindu;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getImgid() {
        return imgid;
    }

    public String getDes() {
        return des;
    }

    public String getTitle() {
        return title;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
