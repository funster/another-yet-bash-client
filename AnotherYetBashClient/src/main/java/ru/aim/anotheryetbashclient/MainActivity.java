package ru.aim.anotheryetbashclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.*;
import ru.aim.anotheryetbashclient.helper.Utils;

import static ru.aim.anotheryetbashclient.SettingsHelper.loadType;
import static ru.aim.anotheryetbashclient.SettingsHelper.saveType;

/**
 * In progress:
 * <p/>
 * 1. *** Save currentType when orientation changed.
 * 2. *** Highlight selected menu item in main menu. Restore it.
 * 3.
 */
public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

    public static final int BLUR_DURATION = 500;

    FrameLayout mainFrame;
    ListView mTypesListView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    QuotesFragment quotesFragment;
    MenuItemsAdapter adapter;
    TransitionDrawable cache;

    int currentTypeId;

    BroadcastReceiver notifyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, intent.getStringExtra(ActionsAndIntents.MESSAGE), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentTypeId = loadType(this);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);
        mainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                cache.reverseTransition(BLUR_DURATION / 2);
                quotesFragment.getView().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainFrame.setForeground(null);
                        quotesFragment.getView().setVisibility(View.VISIBLE);
                    }
                }, BLUR_DURATION);
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                quotesFragment.getView().setVisibility(View.INVISIBLE);
                cache = Utils.makeTransition(MainActivity.this, quotesFragment.getView());
                cache.setCrossFadeEnabled(true);
                mainFrame.setForeground(cache);
                cache.startTransition(BLUR_DURATION);
                super.onDrawerOpened(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mTypesListView = (ListView) findViewById(R.id.types);
        adapter = new MenuItemsAdapter(this, getResources().getStringArray(R.array.types));
        mTypesListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_activated_1,
                getResources().getStringArray(R.array.types)));
        mTypesListView.setOnItemClickListener(this);
        mTypesListView.post(new Runnable() {
            @Override
            public void run() {
                mTypesListView.setItemChecked(currentTypeId, true);
            }
        });

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter1 = new IntentFilter(ActionsAndIntents.NOTIFY);
        localBroadcastManager.registerReceiver(notifyBroadcastReceiver, intentFilter1);

        assert getActionBar() != null;
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        quotesFragment = (QuotesFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        quotesFragment.setCurrentType(currentTypeId);
    }

    public void callRefresh() {
        quotesFragment.callRefresh(currentTypeId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(notifyBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            callRefresh();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position != currentTypeId) {
            currentTypeId = position;
            saveType(this, currentTypeId);
            callRefresh();
        }
        mDrawerLayout.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //noinspection SimplifiableIfStatement
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
