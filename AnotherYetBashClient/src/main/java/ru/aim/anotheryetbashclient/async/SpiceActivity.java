package ru.aim.anotheryetbashclient.async;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

/**
 * Pavel : 14.05.2015.
 */
public class SpiceActivity extends AppCompatActivity {

    private final SpiceManager spiceManager = new SpiceManager(NetworkService.class);

    @Override
    protected void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public SpiceManager getSpiceManager() {
        return spiceManager;
    }
}
