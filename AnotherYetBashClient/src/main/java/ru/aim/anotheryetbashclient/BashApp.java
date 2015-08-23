package ru.aim.anotheryetbashclient;

import android.app.Application;

import ru.aim.anotheryetbashclient.helper.DbHelper;

public class BashApp extends Application {

    private static BashApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbHelper.getInstance(BashApp.this);
            }
        }).start();
    }

    public static BashApp getInstance() {
        return instance;
    }
}
