package com.example.momomo.myapplication;

import org.litepal.crud.LitePalSupport;

public class User extends LitePalSupport {
    private String name;
    private String password;
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
}
