package com.example.prm391x_project_3_fx10105;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

public class Alarm implements Serializable {
    @SerializedName("id") private long id;
    @SerializedName("year") private final int year;
    @SerializedName("month") private final int month;
    @SerializedName("date") private final int date;
    @SerializedName("hour") private final int hour;
    @SerializedName("minute") private final int minute;
    @SerializedName("message") private final String title;
    @SerializedName("time") private final String showTime;
    @SerializedName("status") private String status;
    @SerializedName("longTime") private final long longTime;

    public Alarm(String title, String showTime, Calendar calendar) {
        this.title = title;
        this.showTime = showTime;
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.date = calendar.get(Calendar.DATE);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.minute = calendar.get(Calendar.MINUTE);
        this.longTime = calendar.getTimeInMillis();
        this.status = "online";

    }


    public long getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public String getTitle() {
        return title;
    }

    public String getShowTime() {
        return showTime;
    }


    public long getLongTime() {
        return longTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
