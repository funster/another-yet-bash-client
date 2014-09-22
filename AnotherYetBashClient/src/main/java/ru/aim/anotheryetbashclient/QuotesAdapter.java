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
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import ru.aim.anotheryetbashclient.helper.DbHelper;

/**
 *
 */
public class QuotesAdapter extends CursorAdapter {

    protected int animatedPosition = -1;
    protected DbHelper mDbHelper;
    private boolean isAnimationEnabled;

    public QuotesAdapter(DbHelper dbHelper, Context context, Cursor c) {
        super(context, c, true);
        this.mDbHelper = dbHelper;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        isAnimationEnabled = preferences.getBoolean(SettingsActivity.LIST_ITEM_ANIMATION, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
        assert view != null;
        SwipeLayout swipeLayout = (SwipeLayout) view;
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.setDragEdge(SwipeLayout.DragEdge.Right);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.swipeLayout = swipeLayout;
        viewHolder.date = (TextView) view.findViewById(android.R.id.text1);
        viewHolder.id = (TextView) view.findViewById(android.R.id.text2);
        viewHolder.text = (TextView) view.findViewById(R.id.text);
        viewHolder.isNew = (TextView) view.findViewById(R.id.newQuote);
        viewHolder.rating = (TextView) view.findViewById(R.id.rating);
        viewHolder.addFavorite = (ImageButton) view.findViewById(R.id.add_favorite);
        view.setTag(viewHolder);
        return view;
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        final ViewHolder viewHolder = (ViewHolder) view.getTag();
        final String id = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID));
        String date = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_DATE));
        String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_TEXT));
        viewHolder.date.setText(date);
        viewHolder.id.setText(id);
        viewHolder.text.setText(Html.fromHtml(text));
        viewHolder.publicId = id;
        viewHolder.innerId = cursor.getLong(cursor.getColumnIndex(DbHelper.QUOTE_ID));
        viewHolder.rating.setText(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_RATING)));
        if (mDbHelper.isFavorite(id)) {
            viewHolder.addFavorite.setImageResource(R.drawable.ic_action_favorite_red);
        } else {
            viewHolder.addFavorite.setImageResource(R.drawable.ic_action_favorite);
        }
        viewHolder.addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDbHelper.isFavorite(id)) {
                    mDbHelper.removeFromFavorite(viewHolder.publicId);
                    Toast.makeText(context, R.string.removed_to_favorites, Toast.LENGTH_LONG).show();
                    viewHolder.addFavorite.setImageResource(R.drawable.ic_action_favorite);
                } else {
                    mDbHelper.addToFavorite(viewHolder.publicId);
                    Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_LONG).show();
                    viewHolder.addFavorite.setImageResource(R.drawable.ic_action_favorite_red);
                }
            }
        });
        if (isAnimationEnabled) {
            if (animatedPosition < cursor.getPosition()) {
                AnimatorSet animatorSet = new AnimatorSet();
                ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                animatorSet.playTogether(alpha);
                animatorSet.setDuration(500);
                animatorSet.start();
                animatedPosition = cursor.getPosition();
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        animatedPosition = -1;
        super.notifyDataSetChanged();
    }

    public static class ViewHolder {
        public SwipeLayout swipeLayout;
        public TextView date;
        public TextView id;
        public TextView text;
        public TextView isNew;
        public TextView rating;
        public ImageButton addFavorite;

        public String publicId;
        public long innerId;
    }
}