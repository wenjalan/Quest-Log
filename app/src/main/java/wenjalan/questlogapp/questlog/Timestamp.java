package wenjalan.questlogapp.questlog;
// Represents a Timestamp with a year, month, day, hour, and minute

import java.util.Calendar;

public class Timestamp {

// Fields //
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

// Constructor //
    // default
    public Timestamp() {
        init();
    }

    // specific
    public Timestamp(int year, int month, int day, int hour, int minute) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

// Methods //
    // initializes a new Timestamp object with the current time
    private void init() {
        // get the time fields
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
    }

// Getters //
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

    // toString
    @Override
    public String toString() {
        return year + "/" + month + "/" + day + "; " + hour + ":" + minute;
    }

}
