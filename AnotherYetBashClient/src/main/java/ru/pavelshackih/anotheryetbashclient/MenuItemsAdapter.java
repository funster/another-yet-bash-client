package ru.pavelshackih.anotheryetbashclient;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 *
 */
public class MenuItemsAdapter extends ArrayAdapter<String> {

    public MenuItemsAdapter(Context context, int resource) {
        super(context, resource, context.getResources().getStringArray(R.array.types));
    }
}
