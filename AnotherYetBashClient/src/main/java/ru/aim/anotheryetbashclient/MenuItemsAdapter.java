package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 *
 */
public class MenuItemsAdapter extends BaseAdapter {

    final Context context;
    final String[] arr;

    int selected;

    public MenuItemsAdapter(Context context, String[] arr) {
        this.context = context;
        this.arr = arr;
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
        if (convertView == null) {
            convertView = View.inflate(context, android.R.layout.simple_list_item_activated_1, null);
        }
        TextView textView = (TextView) convertView;
        textView.setText(arr[position]);
        //if (position == selected) {
        //    textView.setBackgroundResource(android.R.color.holo_green_dark);
        //} else {
            textView.setBackgroundResource(0);
        //}
        return convertView;
    }

    public void setItemSelected(int position) {
        this.selected = position;
        notifyDataSetChanged();
    }
}
