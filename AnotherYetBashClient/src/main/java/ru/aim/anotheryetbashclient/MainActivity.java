package ru.aim.anotheryetbashclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import ru.aim.anotheryetbashclient.fragments.AbstractFragment;
import ru.aim.anotheryetbashclient.fragments.FragmentsFactory;
import ru.aim.anotheryetbashclient.helper.Utils;

import static ru.aim.anotheryetbashclient.Package.updateHeader;
import static ru.aim.anotheryetbashclient.SettingsHelper.saveType;

/**
 *
 */
public class MainActivity extends RulezActivity implements AdapterView.OnItemClickListener {

    public static final int BLUR_DURATION = 500;

    FrameLayout mainFrame;
    ListView mTypesListView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    MenuItemsAdapter adapter;
    TransitionDrawable cache;
    boolean hideAdditionalMenu;

    int currentTypeId;
    final String fragmentKey = "fragmentKey";

    BroadcastReceiver notifyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, intent.getStringExtra(ActionsAndIntents.MESSAGE), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                setMenuItemsVisible(true);
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                setMenuItemsVisible(false);
                super.onDrawerOpened(drawerView);
            }
        };
        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mTypesListView = (ListView) findViewById(R.id.types);
        adapter = new MenuItemsAdapter(this, R.layout.menu_item);
        mTypesListView.setAdapter(adapter);
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

        if (savedInstanceState == null && SettingsHelper.isPreloadedAvailable(this)) {
            currentTypeId = ActionsAndIntents.TYPE_OFFLINE;
        }
        setFragment();
    }

    AbstractFragment getCurrentFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentKey);
        if (fragment instanceof AbstractFragment) {
            return (AbstractFragment) fragment;
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(notifyBroadcastReceiver);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getCurrentFragment().onManualUpdate();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Utils.setItemsVisibility(menu, !hideAdditionalMenu);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (currentTypeId != position) {
            currentTypeId = position;
            setFragment();
        }
        mDrawerLayout.closeDrawers();
    }

    void setFragment() {
        updateHeader(this, currentTypeId);
        Fragment fragment = FragmentsFactory.getFragment(currentTypeId);
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.main_frame, fragment, fragmentKey)
                .commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveType(this, currentTypeId);
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

    public void setMenuItemsVisible(boolean visible) {
        hideAdditionalMenu = !visible;
        if (getCurrentFragment() != null) {
            getCurrentFragment().setMenuItemsVisibility(visible);
        }
        invalidateOptionsMenu();
    }
}
