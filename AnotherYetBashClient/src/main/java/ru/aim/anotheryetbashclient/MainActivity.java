package ru.aim.anotheryetbashclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Toast;

import ru.aim.anotheryetbashclient.fragments.AbstractFragment;
import ru.aim.anotheryetbashclient.fragments.FragmentsFactory;
import ru.aim.anotheryetbashclient.fragments.RecycleFragment;
import ru.aim.anotheryetbashclient.fragments.RefreshFragment;
import ru.aim.anotheryetbashclient.helper.Utils;
import ru.aim.anotheryetbashclient.settings.SettingsActivity;
import ru.aim.anotheryetbashclient.settings.SettingsHelper;
import ru.aim.anotheryetbashclient.support.RulezActivity;

import static ru.aim.anotheryetbashclient.Package.updateHeader;
import static ru.aim.anotheryetbashclient.settings.SettingsHelper.saveType;

/**
 *
 */
public class MainActivity extends RulezActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    public final static String FRAGMENT_KEY = "FRAGMENT_KEY";

    FrameLayout mainFrame;
    boolean hideAdditionalMenu;
    ActionBarDrawerToggle mDrawerToggle;

    int currentTypeId;
    RefreshFragment mListFragment;
    boolean mScrollByVolumeEnabled;

    private DrawerLayout mDrawerLayout;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout != null) {
            mDrawerToggle = new ActionBarDrawerToggle(this,
                    mDrawerLayout,
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
        }

        mainFrame = (FrameLayout) findViewById(R.id.main_frame);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter1 = new IntentFilter(ActionsAndIntents.NOTIFY);
        localBroadcastManager.registerReceiver(notifyBroadcastReceiver, intentFilter1);

        assert getSupportActionBar() != null;

        if (savedInstanceState == null && SettingsHelper.isPreloadedAvailable(this)) {
            currentTypeId = ActionsAndIntents.TYPE_OFFLINE;
        }

        if (savedInstanceState != null) {
            currentTypeId = savedInstanceState.getInt("type");
            updateHeader(this, currentTypeId);
        }

        if (savedInstanceState == null) {
            currentTypeId = ActionsAndIntents.TYPE_NEW;
            setFragment();
        } else {
            mListFragment = (RefreshFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.nav_settings) {
                            menuItem.setChecked(false);
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        } else {
                            currentTypeId = menuItem.getItemId();
                            setFragment();
                        }
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    AbstractFragment getCurrentFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_KEY);
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
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    void setFragment() {
        updateHeader(this, currentTypeId);
        Fragment fragment = FragmentsFactory.getFragment(currentTypeId);
        if (fragment instanceof RecycleFragment) {
            mListFragment = (RefreshFragment) fragment;
        }
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.main_frame, fragment, FRAGMENT_KEY)
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
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (mDrawerToggle != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDrawerToggle != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    public void setMenuItemsVisible(boolean visible) {
        if (!Utils.isNetworkAvailable(this)) {
            return;
        }
        hideAdditionalMenu = !visible;
        if (getCurrentFragment() != null) {
            getCurrentFragment().setMenuItemsVisibility(visible);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_settings) {
            if (mDrawerLayout != null) {
                mDrawerLayout.closeDrawers();
            }
            startActivity(new Intent(this, SettingsActivity.class));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mScrollByVolumeEnabled && mListFragment != null) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//                mListFragment.getListView().smoothScrollByOffset(1);
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//                mListFragment.getListView().smoothScrollByOffset(-1);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("type", currentTypeId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mScrollByVolumeEnabled = SettingsHelper.isScrollByVolumeEnabled(this);
        if (SettingsHelper.isKeepScreenEnabled(this)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public RefreshFragment getRefreshFragment() {
        return mListFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionsUtil.EXTERNAL_STORAGE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AbstractActivity.runPermActions();
            } else {
                clearPermAcitons();
            }
        }
    }
}