package com.example.momomo.myapplication.data_save;

public class foodwatch {
    private String time;
    private int weather;
//    private int food1;
    private int cal;

    public String getTime() {
        return time;
    }

    public int getWeather() {
        return weather;
    }

//    public int getFood1() {
//        return food1;
//    }


    public int getCal() {
        return cal;
    }

    public foodwatch(String time, int weather) {
        this.time = time;
        this.weather = weather;
    }

    public void setCal(int cal) {
        this.cal = cal;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    public void setFood1(int food1) {
//        this.food1 = food1;
//    }

}
