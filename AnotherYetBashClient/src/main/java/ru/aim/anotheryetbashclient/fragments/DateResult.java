package ru.aim.anotheryetbashclient.fragments;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 */
public class DateResult implements Serializable {

    public int day = -1;
    public int year = -1;
    public int month = -1;

    public boolean isToday() {
        if (isTodaySimple()) {
            return true;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return DateUtils.isToday(calendar);
    }

    public boolean isTodaySimple() {
        return year == -1 && month == -1 && day == -1;
    }

    public boolean isSet() {
        return !isTodaySimple();
    }
}
