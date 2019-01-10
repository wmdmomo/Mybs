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
    public List<selectfoods> getMoring() {
        localTime=new LocalTime();
        morningList = LitePal.where("timeid=? and time=?", "0",localTime.LocalTime()).find(selectfoods.class);
        return morningList;

    }

    public List<selectfoods> getNoon() {
        localTime=new LocalTime();
        noonList = LitePal.where("timeid=?and time=?", "1",localTime.LocalTime()).find(selectfoods.class);
        return noonList;

    }

    public List<selectfoods> getAfternoon() {
        localTime=new LocalTime();
        afternoonList = LitePal.where("timeid=?and time=?", "2",localTime.LocalTime()).find(selectfoods.class);
        return afternoonList;

    }

    public List<selectfoods> getEvening() {
        localTime=new LocalTime();
        eveningList = LitePal.where("timeid=?and time=?", "3",localTime.LocalTime()).find(selectfoods.class);
        return eveningList;
    }
}