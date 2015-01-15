package ru.aim.anotheryetbashclient.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.SwipeRefreshUtils;

import static ru.aim.anotheryetbashclient.helper.Utils.isNetworkAvailable;

@SuppressWarnings("unused")
public abstract class RefreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected boolean itemsVisible;
    protected SwipeRefreshLayout refreshLayout;
    protected View emptyView;
    protected ProgressBar progressView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(this);
        SwipeRefreshUtils.applyStyle(refreshLayout);
        emptyView = view.findViewById(android.R.id.empty);
        progressView = (ProgressBar) view.findViewById(android.R.id.progress);
        progressView.getIndeterminateDrawable().
                setColorFilter(getResources().getColor(R.color.app_colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);
        Button refreshButton = (Button) view.findViewById(R.id.refresh_button);
        if (refreshButton != null) {
            refreshButton.setOnClickListener(refreshListener);
        }
    }

    View.OnClickListener refreshListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onManualUpdate();
        }
    };

    protected void setRefreshing(boolean value) {
        getMainActivity().setMenuItemsVisible(!value);
        if (isEmptyList()) {
            if (value) {
                progressView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);
            } else {
                progressView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }
        }
        if (progressView.getVisibility() != View.VISIBLE) {
            refreshLayout.setRefreshing(value);
        }
    }

    boolean isEmptyList() {
        return (getListAdapter() == null || getListAdapter().getCount() == 0) && progressView != null;
    }

    public abstract void onManualUpdate();

    @Override
    public void onRefresh() {
        onManualUpdate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            onManualUpdate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setMenuItemsVisibility(boolean visible) {
        itemsVisible = visible;
        setMenuVisibility(isItemsVisible());
    }

    public boolean isItemsVisible() {
        return itemsVisible && !(refreshLayout.isRefreshing() || progressView.getVisibility() == View.VISIBLE) && isOnline();
    }

    protected boolean isOffline() {
        return !isNetworkAvailable(getActivity());
    }

    protected boolean isOnline() {
        return isNetworkAvailable(getActivity());
    }

    public boolean isRefreshing() {
        return refreshLayout == null || refreshLayout.isRefreshing();
    }

    public SwipeRefreshLayout getRefreshLayout() {
        return refreshLayout;
    }
}
