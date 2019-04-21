package com.example.momomo.myapplication.data_save;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private String name;
    private String password;
    private int height;
    private int weight;
    private int goal_weight;
    private String signature;
    private int punch;
    private int id;
    private String avatar_path;
    private int likes;
    private String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getGoal_weight() {
        return goal_weight;
    }

    public void setGoal_weight(int goal_weight) {
        this.goal_weight = goal_weight;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

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
