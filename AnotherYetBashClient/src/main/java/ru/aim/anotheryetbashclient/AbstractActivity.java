package ru.aim.anotheryetbashclient;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity {

    boolean isResumed = false;
    List<Runnable> actions = new ArrayList<>();

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < actions.size(); i++) {
            Handler handler = new Handler();
            final Runnable runnable = actions.get(i);
            handler.postDelayed(runnable::run, 1);
        }
        actions.clear();
    }

    public boolean isActivityResumed() {
        return isResumed;
    }

    public void submitAction(Runnable runnable) {
        if (isActivityResumed()) {
            runnable.run();
        } else {
            actions.add(runnable);
        }
    }
}
