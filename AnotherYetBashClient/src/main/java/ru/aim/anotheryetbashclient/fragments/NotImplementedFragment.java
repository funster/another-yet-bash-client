package ru.aim.anotheryetbashclient.fragments;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

/**
 *
 */
public class NotImplementedFragment extends AbstractBashFragment {

    @Override
    public void onStart() {
        super.onStart();
        setEmptyText("Not implemented yet");
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onReceive(Intent intent) {

    }

    @Override
    public void onManualUpdate() {

    }

    @Override
    public int getType() {
        return 0;
    }
}
