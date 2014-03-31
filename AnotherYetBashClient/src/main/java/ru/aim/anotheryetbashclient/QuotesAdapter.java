package ru.aim.anotheryetbashclient;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
        viewHolder.isNew = (TextView) view.findViewById(R.id.newQuote);
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
        if (cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.QUOTE_IS_NEW)) == 1) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewHolder.isNew.startAnimation(animation);
            viewHolder.isNew.setVisibility(View.VISIBLE);
        } else {
            viewHolder.isNew.setVisibility(View.GONE);
        }
        mDbHelper.markRead(viewHolder.innerId);
        if (isItemAnimationEnabled(context)) {
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
    }

    boolean isItemAnimationEnabled(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(SettingsActivity.LIST_ITEM_ANIMATION, false);
    }

    @Override
    public void notifyDataSetChanged() {
        animatedPosition = -1;
        super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView date;
        public TextView id;
        public TextView text;
        public TextView isNew;
        public String publicId;
        public long innerId;
    }
}