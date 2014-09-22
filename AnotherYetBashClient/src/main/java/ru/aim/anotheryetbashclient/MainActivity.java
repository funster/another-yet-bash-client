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
import android.support.v4.app.FragmentActivity;
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
public class MainActivity extends FragmentActivity implements AdapterView.OnItemClickListener {

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

    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

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
//                cache.reverseTransition(BLUR_DURATION / 2);
//                quotesFragment.getView().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mainFrame.setForeground(null);
//                        quotesFragment.getView().setVisibility(View.VISIBLE);
//                    }
//                }, BLUR_DURATION);
                setMenuItemsVisible(true);
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //quotesFragment.getView().setVisibility(View.INVISIBLE);
//                cache = Utils.makeTransition(MainActivity.this, quotesFragment.getView());
//                cache.setCrossFadeEnabled(true);
                ///              mainFrame.setForeground(cache);
                //           cache.startTransition(BLUR_DURATION);
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

        IntentFilter intentFilter = new IntentFilter(ActionsAndIntents.REFRESH);
        localBroadcastManager.registerReceiver(refreshReceiver, intentFilter);

        assert getActionBar() != null;
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

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
        localBroadcastManager.unregisterReceiver(refreshReceiver);
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
//        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setQueryHint(getString(R.string.search_hint));
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Intent intent = new Intent(MainActivity.this, QuoteService.class);
//                intent.putExtra(ActionsAndIntents.SEARCH_QUERY, query);
//                intent.putExtra(ActionsAndIntents.TYPE_ID, ActionsAndIntents.TYPE_SEARCH);
//                startService(intent);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
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
