package ru.aim.anotheryetbashclient;

import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.List;

public class AbstractActivity extends ActionBarActivity {

    boolean isResumed = false;
    List<Runnable> actions = new ArrayList<Runnable>();

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
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                        finish();
                        startActivity(getIntent());
                    } else {
                        recreate();
                    }
                }
            }, 1);
            actions.get(i).run();
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
