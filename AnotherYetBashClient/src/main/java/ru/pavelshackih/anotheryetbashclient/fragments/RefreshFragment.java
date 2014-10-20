package ru.pavelshackih.anotheryetbashclient.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ru.pavelshackih.anotheryetbashclient.R;
import ru.pavelshackih.anotheryetbashclient.SwipeRefreshUtils;

import static ru.pavelshackih.anotheryetbashclient.helper.Utils.isNetworkAvailable;

@SuppressWarnings("unused")
public abstract class RefreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private boolean itemsVisible;
    private SwipeRefreshLayout refreshLayout;
    private TextView emptyView;
    private View progressView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(this);
        SwipeRefreshUtils.applyStyle(refreshLayout);
        emptyView = (TextView) view.findViewById(android.R.id.empty);
        progressView = view.findViewById(android.R.id.progress);
        super.onViewCreated(view, savedInstanceState);
    }

    protected void setRefreshing(boolean value) {
        refreshLayout.setRefreshing(value);
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
        return itemsVisible && !refreshLayout.isRefreshing() && isOnline();
    }

    protected boolean isOffline() {
        return !isNetworkAvailable(getActivity());
    }

    protected boolean isOnline() {
        return isNetworkAvailable(getActivity());
    }

    public boolean isRefreshing() {
        return refreshLayout.isRefreshing();
    }
}
