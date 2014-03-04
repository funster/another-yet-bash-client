package ru.aim.anotheryetbashclient;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import ru.aim.anotheryetbashclient.helper.DbHelper;

/**
 *
 */
public class QuotesAdapter extends CursorAdapter {

    int animatedPosition = -1;
    DbHelper mDbHelper;

    public QuotesAdapter(DbHelper dbHelper, Context context, Cursor c) {
        super(context, c, true);
        this.mDbHelper = dbHelper;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder();
        assert view != null;
        viewHolder.date = (TextView) view.findViewById(android.R.id.text1);
        viewHolder.id = (TextView) view.findViewById(android.R.id.text2);
        viewHolder.text = (TextView) view.findViewById(R.id.text);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String id = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID));
        String date = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_DATE));
        String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_TEXT));
        viewHolder.date.setText(date);
        viewHolder.id.setText(id);
        viewHolder.text.setText(Html.fromHtml(text));
        viewHolder.publicId = id;
        viewHolder.innerId = cursor.getLong(cursor.getColumnIndex(DbHelper.QUOTE_ID));
        mDbHelper.markRead(viewHolder.innerId);
        if (animatedPosition < cursor.getPosition()) {
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            ObjectAnimator translate = ObjectAnimator.ofFloat(view, "x", -300f, 0f);
            animatorSet.playTogether(alpha, translate);
            animatorSet.setDuration(1000);
            animatorSet.start();
            animatedPosition = cursor.getPosition();
        }
    }

    @Override
    public void notifyDataSetChanged() {
        animatedPosition = -1;
        super.notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView date;
        TextView id;
        TextView text;
        String publicId;
        long innerId;
    }
}