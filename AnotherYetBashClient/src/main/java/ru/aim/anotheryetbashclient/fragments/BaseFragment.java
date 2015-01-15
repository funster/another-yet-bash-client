package ru.aim.anotheryetbashclient.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.aim.anotheryetbashclient.MainActivity;
import ru.aim.anotheryetbashclient.helper.L;

@SuppressWarnings("unused")
public abstract class BaseFragment extends ListFragment {

    static String TAG = "BaseFragment";

    private static final String LIST_VIEW_STATE = "listViewState";

    private List<Runnable> mPausedActions = new ArrayList<Runnable>();
    private Parcelable mListViewState;

    protected void run(Runnable runnable) {
        if (isResumed()) {
            runnable.run();
        } else {
            mPausedActions.add(runnable);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mListViewState = savedInstanceState.getParcelable(LIST_VIEW_STATE);
        }
    }

    @Override
    public void setListAdapter(ListAdapter adapter) {
        super.setListAdapter(adapter);
        if (getListView() != null && mListViewState != null) {
            getListView().onRestoreInstanceState(mListViewState);
            mListViewState = null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < mPausedActions.size(); i++) {
            mPausedActions.get(i).run();
        }
        mPausedActions.clear();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public static void sendMessage(FragmentManager manager, Class<? extends BaseFragment> to, Object message) {
        //noinspection StatementWithEmptyBody
        for (int i = 0; i < manager.getFragments().size(); i++) {
            Fragment fragment = manager.getFragments().get(i);
            if (to.isAssignableFrom(fragment.getClass())) {
                ((BaseFragment) fragment).onMessageReceived(message);
            }
        }
    }

    public static void sendMessage(FragmentActivity activity, Class<? extends BaseFragment> to, Object message) {
        sendMessage(activity.getSupportFragmentManager(), to, message);
    }

    public void sendMessage(Class<? extends BaseFragment> to, Object message) {
        sendMessage(getActivity().getSupportFragmentManager(), to, message);
    }

    protected void onMessageReceived(Object message) {
        L.d(TAG, String.format("Message received %s", message));
    }

    public MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public static void showWarning(final FragmentActivity activity, final String message) {
        Handler handler = new Handler(Looper.myLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getListView() != null) {
            outState.putParcelable(LIST_VIEW_STATE, getListView().onSaveInstanceState());
        }
    }
}
