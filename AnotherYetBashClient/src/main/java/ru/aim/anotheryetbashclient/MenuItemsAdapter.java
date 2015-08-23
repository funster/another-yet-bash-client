package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 *
 */
@Deprecated
public class MenuItemsAdapter extends ArrayAdapter<String> {

    public MenuItemsAdapter(Context context, int resource) {
        super(context, resource, context.getResources().getStringArray(R.array.types));
    }
}
