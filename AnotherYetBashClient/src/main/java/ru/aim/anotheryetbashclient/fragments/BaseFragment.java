package ru.aim.anotheryetbashclient.fragments;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.aim.anotheryetbashclient.MainActivity;
import ru.aim.anotheryetbashclient.helper.L;

@SuppressWarnings("unused")
public abstract class BaseFragment extends ListFragment {

    public static String TAG = "BaseFragment";
    private List<Runnable> pausedActions = new ArrayList<Runnable>();

    protected void run(Runnable runnable) {
        if (isResumed()) {
            runnable.run();
        } else {
            pausedActions.add(runnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < pausedActions.size(); i++) {
            pausedActions.get(i).run();
        }
        pausedActions.clear();
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
                // SimpleDialog simpleDialog = SimpleDialog.newInstance(message);
                // simpleDialog.show(fragmentManager);
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
