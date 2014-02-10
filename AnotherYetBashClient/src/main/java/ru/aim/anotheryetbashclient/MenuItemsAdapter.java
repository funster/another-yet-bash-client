package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 *
 */
public class MenuItemsAdapter extends BaseAdapter {

    final Context context;
    final String[] arr;

    int selected;

    public MenuItemsAdapter(Context context) {
        this.context = context;
        arr = context.getResources().getStringArray(R.array.item_menu);
    }

    @Override
    public int getCount() {
        return arr.length;
    }

    @Override
    public Object getItem(int position) {
        return arr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public void setItemSelected(int position) {
        this.selected = position;
        notifyDataSetChanged();
    }
}
