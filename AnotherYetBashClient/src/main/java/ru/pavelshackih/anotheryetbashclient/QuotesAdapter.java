package ru.pavelshackih.anotheryetbashclient;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import ru.pavelshackih.anotheryetbashclient.helper.DbHelper;
import ru.pavelshackih.anotheryetbashclient.loaders.RulezType;

/**
 *
 */
public class QuotesAdapter extends CursorAdapter {

    protected int animatedPosition = -1;
    protected DbHelper mDbHelper;
    protected boolean isAnimationEnabled;
    protected RulezActivity rulezActivity;
    protected int textSize;
    protected Context mContext;

    public QuotesAdapter(DbHelper dbHelper, Context context, Cursor c) {
        super(context, c, true);
        this.mDbHelper = dbHelper;
        this.mContext = context;
        isAnimationEnabled = SettingsHelper.isItemAnimationEnabled(context);
        textSize = SettingsHelper.getFontSize(context);
        if (context instanceof RulezActivity) {
            rulezActivity = (RulezActivity) context;
        }
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
        viewHolder.date.setTextSize(textSize);
        viewHolder.id = (TextView) view.findViewById(android.R.id.text2);
        viewHolder.id.setTextSize(textSize);
        viewHolder.text = (TextView) view.findViewById(R.id.text);
        viewHolder.text.setTextSize(textSize);
        viewHolder.isNew = (TextView) view.findViewById(R.id.newQuote);
        viewHolder.rating = (TextView) view.findViewById(R.id.rating);
        viewHolder.addFavorite = (ImageButton) view.findViewById(R.id.add_favorite);
        viewHolder.plus = view.findViewById(R.id.plus);
        viewHolder.minus = view.findViewById(R.id.minus);
        viewHolder.bayan = view.findViewById(R.id.bayan);
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
                onFavoriteClick(id, viewHolder, context);
            }
        });
        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRulez(viewHolder.publicId, RulezType.RULEZ);
            }
        });
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRulez(viewHolder.publicId, RulezType.SUX);
            }
        });
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRulez(viewHolder.publicId, RulezType.BAYAN);
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

    protected void onFavoriteClick(String id, ViewHolder viewHolder, Context context) {
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

    void sendRulez(String id, RulezType type) {
        if (rulezActivity != null) {
            rulezActivity.sendRulez(id, type);
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
        public View plus;
        public View minus;
        public View bayan;

        public String publicId;
        public long innerId;
    }
}