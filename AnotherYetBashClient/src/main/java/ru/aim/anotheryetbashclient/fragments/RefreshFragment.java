package ru.aim.anotheryetbashclient.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.SwipeRefreshUtils;

import static ru.aim.anotheryetbashclient.helper.Utils.isNetworkAvailable;

@SuppressWarnings("unused")
public abstract class RefreshFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private boolean itemsVisible;
    private SwipeRefreshLayout refreshLayout;
    private TextView emptyView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(this);
        SwipeRefreshUtils.applyStyle(refreshLayout);
        emptyView = (TextView) view.findViewById(android.R.id.empty);
        super.onViewCreated(view, savedInstanceState);
    }

    protected void setRefreshing(boolean value) {
        refreshLayout.setRefreshing(value);
        getMainActivity().setMenuItemsVisible(!value);
        if (isEmptyList()) {
            if (value) {
                emptyView.setText(R.string.data_refreshing);
            } else {
                emptyView.setText(R.string.empty_list);
            }
        }
    }

    boolean isEmptyList() {
        return (getListAdapter() == null || getListAdapter().getCount() == 0) && emptyView != null;
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
