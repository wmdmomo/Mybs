package com.example.momomo.myapplication.Manager;

import com.example.momomo.myapplication.data_save.selectfoods;
import com.example.momomo.myapplication.utils.LocalTime;

import org.litepal.LitePal;


import java.util.ArrayList;
import java.util.List;

public class Fooddatas {
    private List<selectfoods> morningList = new ArrayList<>();
    private List<selectfoods> noonList = new ArrayList<>();
    private List<selectfoods> afternoonList = new ArrayList<>();
    private List<selectfoods> eveningList = new ArrayList<>();
    private LocalTime localTime;
    private String user;

    public Fooddatas(String user) {
        this.user = user;

    }

    public List<selectfoods> getMoring() {
        localTime = new LocalTime();
        morningList = LitePal.where("user=? and timeid=? and time=?", user, "0", localTime.LocalTime()).find(selectfoods.class);
        return morningList;
    }

    public List<selectfoods> getNoon() {
        localTime = new LocalTime();
        noonList = LitePal.where("user=? and timeid=?and time=?", user, "1", localTime.LocalTime()).find(selectfoods.class);
        return noonList;
    }

    public List<selectfoods> getAfternoon() {
        localTime = new LocalTime();
        afternoonList = LitePal.where("user=? and timeid=?and time=?", user, "2", localTime.LocalTime()).find(selectfoods.class);
        return afternoonList;
    }

}