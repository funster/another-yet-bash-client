package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.loaders.RulezType;

public class RecycleQuotesAdapter extends RecycleCursorAdapter<RecycleQuotesAdapter.QuotesViewHolder> {

    private static final String TAG = "RecycleQuotesAdapter";

    DbHelper dbHelper;

    int favoriteFill;
    int favoriteEmpty;

    public RecycleQuotesAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        dbHelper = new DbHelper(context);
    }

    @Override
    public void onBindViewHolder(final QuotesViewHolder viewHolder, Cursor cursor) {
        final String id = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID));
        String date = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_DATE));
        String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_TEXT));
        viewHolder.date.setText(date);
        viewHolder.id.setText(id);
        viewHolder.text.setText(Html.fromHtml(text));
        viewHolder.publicId = id;
        viewHolder.innerId = cursor.getLong(cursor.getColumnIndex(DbHelper.QUOTE_ID));
        viewHolder.rating.setText(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_RATING)));
        if (dbHelper.isFavorite(id)) {
            viewHolder.addFavorite.setImageResource(favoriteFill);
        } else {
            viewHolder.addFavorite.setImageResource(favoriteEmpty);
        }
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add_favorite:
                        onFavoriteClick(id, viewHolder, getContext());
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
                        share(getContext(), viewHolder);
                        break;
                }
            }
        };
        viewHolder.addFavorite.setOnClickListener(clickListener);
        viewHolder.plus.setOnClickListener(clickListener);
        viewHolder.minus.setOnClickListener(clickListener);
        viewHolder.bayan.setOnClickListener(clickListener);
        viewHolder.share.setOnClickListener(clickListener);
    }

    protected Bitmap buildQuoteBitmap(QuotesViewHolder viewHolder) {
        View quoteView = LayoutInflater.from(getContext()).inflate(R.layout.simple_quote, null);
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

    protected void share(Context context, QuotesViewHolder viewHolder) {
        ShareDialog shareDialog = ShareDialog.newInstance(buildQuoteBitmap(viewHolder),
                viewHolder.publicId,
                viewHolder.text.getText().toString());
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            shareDialog.show(fragmentActivity.getSupportFragmentManager(), "share-dialog");
        }
    }

    protected void onFavoriteClick(String id, QuotesViewHolder viewHolder, Context context) {
        if (dbHelper.isFavorite(id)) {
            dbHelper.removeFromFavorite(viewHolder.publicId);
            Toast.makeText(context, R.string.removed_to_favorites, Toast.LENGTH_SHORT).show();
            viewHolder.addFavorite.setImageResource(favoriteEmpty);
        } else {
            dbHelper.addToFavorite(viewHolder.publicId);
            Toast.makeText(context, R.string.added_to_favorites, Toast.LENGTH_SHORT).show();
            viewHolder.addFavorite.setImageResource(favoriteFill);
        }
    }

    void sendRulez(String id, RulezType type) {
        // mContext.sendRulez(id, type);
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe_layout;
    }

    @Override
    public QuotesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new QuotesViewHolder(view);
    }

    public static class QuotesViewHolder extends RecyclerView.ViewHolder {

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

        public QuotesViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(android.R.id.text1);
            id = (TextView) view.findViewById(android.R.id.text2);
            text = (TextView) view.findViewById(R.id.text);
            isNew = (TextView) view.findViewById(R.id.newQuote);
            rating = (TextView) view.findViewById(R.id.rating);
            addFavorite = (ImageButton) view.findViewById(R.id.add_favorite);
            plus = view.findViewById(R.id.plus);
            minus = view.findViewById(R.id.minus);
            bayan = view.findViewById(R.id.boyan);
            share = view.findViewById(R.id.share);
            quoteContainer = view.findViewById(R.id.quote_container);
        }
    }

}