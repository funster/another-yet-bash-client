package ru.aim.anotheryetbashclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.RecycleQuotesAdapter;
import ru.aim.anotheryetbashclient.ShareDialog;
import ru.aim.anotheryetbashclient.loaders.FavoritesLoader;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

public class FavoritesFragment extends AbstractFragment
        implements LoaderManager.LoaderCallbacks<SimpleResult<Cursor>> {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // getListView().setEmptyView(view.findViewById(android.R.id.empty));
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_FAVORITES;
    }

    @Override
    protected void initLoader() {
        getLoaderManager().initLoader(1, Bundle.EMPTY, this);
    }

    @Override
    public void onManualUpdate() {
        getLoaderManager().getLoader(1).forceLoad();
    }

    @Override
    public Loader<SimpleResult<Cursor>> onCreateLoader(int i, Bundle bundle) {
        setRefreshing(true);
        return new FavoritesLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<SimpleResult<Cursor>> loader, SimpleResult<Cursor> result) {
        setRefreshing(false);
        if (result.containsError()) {
            showWarning(getActivity(), result.getError().getMessage());
        } else {
            RecycleQuotesAdapter listAdapter = new FavoritesAdapter(getActivity(), result.getResult());
            setAdapter(listAdapter);
            setMenuItemsVisibility(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<SimpleResult<Cursor>> loader) {
        safeSwap();
    }

    class FavoritesAdapter extends RecycleQuotesAdapter {

        public FavoritesAdapter(Context context, Cursor cursor) {
            super(context, cursor);
        }

        @Override
        protected void onFavoriteClick(String id, QuotesViewHolder viewHolder, Context context) {
            super.onFavoriteClick(id, viewHolder, context);
            onManualUpdate();
        }

        @Override
        public void onBindViewHolder(QuotesViewHolder viewHolder, Cursor cursor) {
            super.onBindViewHolder(viewHolder, cursor);
//            viewHolder.plus.setVisibility(View.GONE);
//            viewHolder.minus.setVisibility(View.GONE);
//            viewHolder.bayan.setVisibility(View.GONE);
        }

        @Override
        protected void share(Context context, QuotesViewHolder viewHolder) {
            Bitmap bitmap = buildQuoteBitmap(viewHolder);
            ShareDialog shareDialog = ShareDialog.newInstance(bitmap, null,
                    viewHolder.text.getText().toString());
            if (context instanceof FragmentActivity) {
                FragmentActivity fragmentActivity = (FragmentActivity) context;
                shareDialog.show(fragmentActivity.getSupportFragmentManager(), "share-dialog");
            }
        }
    }
}