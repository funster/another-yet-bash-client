package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 *
 */
public class MenuItemsAdapter extends ArrayAdapter<String> {

    public MenuItemsAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean isEnabled(int position) {
        if (position == ActionsAndIntents.TYPE_OFFLINE) {
            return false;
        }
        return super.isEnabled(position);
    }
}
