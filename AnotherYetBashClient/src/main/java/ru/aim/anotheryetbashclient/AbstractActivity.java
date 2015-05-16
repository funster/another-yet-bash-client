package ru.aim.anotheryetbashclient;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import ru.aim.anotheryetbashclient.async.SpiceActivity;

public abstract class AbstractActivity extends SpiceActivity {

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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, 1);
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
