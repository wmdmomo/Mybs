package com.example.momomo.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LocalTime {
   private String nowtime;
   public String LocalTime()
    {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        SimpleDateFormat now = new SimpleDateFormat("yyyy-MM-dd");
        now.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        nowtime = now.format(date);
        return nowtime;
    }
}
