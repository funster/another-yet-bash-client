package ru.aim.anotheryetbashclient;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import ru.aim.anotheryetbashclient.helper.DbHelper;

public class AlarmReceiver extends BroadcastReceiver {

    public static final String ALARM_ACTION = "ru.aim.anotheryetbashclient.UPDATE";
    public static final int DEFAULT_REQUEST_CODE = 0;

    public static void setAlarm(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        dbHelper.clearFresh();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DEFAULT_REQUEST_CODE,
                buildIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, SettingsHelper.getUpdateHour(context));
        calendar.set(Calendar.MINUTE, SettingsHelper.getUpdateMinute(context));
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    public static void cancelAlarm(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        dbHelper.clearFresh();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DEFAULT_REQUEST_CODE,
                buildIntent(), PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
        SettingsHelper.writeUpdateTimestamp(context, 0);
    }

    public static Intent buildIntent() {
        return new Intent(ALARM_ACTION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, QuoteService.class));
    }
}
