package ru.aim.anotheryetbashclient;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
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
import ru.aim.anotheryetbashclient.loaders.RulezType;

/**
 *
 */
public class QuotesAdapter extends CursorAdapter {

    private static final String TAG = "QuotesAdapter";

    protected int animatedPosition = -1;
    protected DbHelper mDbHelper;
    protected boolean isAnimationEnabled;
    protected int textSize;
    protected MainActivity mContext;

    int favoriteFill;
    int favoriteEmpty;

    public QuotesAdapter(DbHelper dbHelper, Context context, Cursor c) {
        super(context, c, true);
        this.mDbHelper = dbHelper;
        this.mContext = (MainActivity) context;
        isAnimationEnabled = SettingsHelper.isItemAnimationEnabled(context);
        textSize = SettingsHelper.getFontSize(context);
        favoriteFill = AttributeUtils.resolveResource(context, R.attr.favorite_full_icon);
        favoriteEmpty = AttributeUtils.resolveResource(context, R.attr.favorite_empty_icon);
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
        viewHolder.bayan = view.findViewById(R.id.boyan);
        viewHolder.share = view.findViewById(R.id.share);
        viewHolder.quoteContainer = view.findViewById(R.id.quote_container);
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
            viewHolder.addFavorite.setImageResource(favoriteFill);
        } else {
            viewHolder.addFavorite.setImageResource(favoriteEmpty);
        }
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_favorite:
                        onFavoriteClick(id, viewHolder, context);
                        break;
                    case R.id.plus:
                        sendRulez(viewHolder.publicId, RulezType.RULEZ);
                        break;
                    case R.id.minus:
                        sendRulez(viewHolder.publicId, RulezType.SUX);
                        break;
                    case R.id.boyan:
                        sendRulez(viewHolder.publicId, RulezType.BAYAN);
                        break;
                    case R.id.share:
                        share(context, viewHolder);
                        break;
                }
            }
        };
        viewHolder.addFavorite.setOnClickListener(clickListener);
        viewHolder.plus.setOnClickListener(clickListener);
        viewHolder.minus.setOnClickListener(clickListener);
        viewHolder.bayan.setOnClickListener(clickListener);
        viewHolder.share.setOnClickListener(clickListener);

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

    protected Bitmap buildQuoteBitmap(ViewHolder viewHolder) {
        View quoteView = LayoutInflater.from(mContext).inflate(R.layout.simple_quote, null);
        TextView tmpTextView = (TextView) quoteView.findViewById(android.R.id.text2);
        tmpTextView.setText(viewHolder.id.getText());
        tmpTextView = (TextView) quoteView.findViewById(android.R.id.text1);
        tmpTextView.setText(viewHolder.date.getText());
        tmpTextView = (TextView) quoteView.findViewById(R.id.text);
        tmpTextView.setText(viewHolder.text.getText());
        quoteView.setDrawingCacheEnabled(true);
        quoteView.measure(View.MeasureSpec.makeMeasureSpec(800, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        quoteView.layout(0, 0, quoteView.getMeasuredWidth(), quoteView.getMeasuredHeight());
        return quoteView.getDrawingCache(true);
    }

    protected void share(Context context, ViewHolder viewHolder) {
        ShareDialog shareDialog = ShareDialog.newInstance(buildQuoteBitmap(viewHolder),
                viewHolder.publicId,
                viewHolder.text.getText().toString());
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            shareDialog.show(fragmentActivity.getSupportFragmentManager(), "share-dialog");
        }
    }

    protected void onFavoriteClick(String id, ViewHolder viewHolder, Context context) {
        if (mDbHelper.isFavorite(id)) {
            mDbHelper.removeFromFavorite(viewHolder.publicId);
            Toast.makeText(context, R.string.removed_to_favorites, Toast.LENGTH_SHORT).show();
            viewHolder.addFavorite.setImageResource(favoriteEmpty);
        } else {
            mDbHelper.addToFavorite(viewHolder.publicId);
            Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
            viewHolder.addFavorite.setImageResource(favoriteFill);
        }
    }

    void sendRulez(String id, RulezType type) {
        mContext.sendRulez(id, type);
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
        public View share;
        public View quoteContainer;

        public String publicId;
        public long innerId;
    }

    public int getTextSize() {
        return textSize;
    }
}