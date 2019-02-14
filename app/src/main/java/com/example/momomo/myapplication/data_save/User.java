package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private String name;
    private String password;
    private int height;
    private int weight;
    private String signature;
    private int punch;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getHeight() {
        return height;
    }

    public int getPunch() {
        return punch;
    }

    public int getWeight() {
        return weight;
    }

    public String getSignature() {
        return signature;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setPunch(int punch) {
        this.punch = punch;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
