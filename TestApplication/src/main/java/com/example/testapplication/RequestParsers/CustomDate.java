package com.example.testapplication.RequestParsers;

import java.text.ParseException;

public class CustomDate {
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private boolean onlyDate;

    public CustomDate(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        if (hour == -1) {
            onlyDate = true;
        } else {
            onlyDate = false;
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean ifOnlyDate() {
        return onlyDate;
    }
}

