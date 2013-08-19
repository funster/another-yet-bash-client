package ru.aim.anotheryetbashclient;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.QuoteService;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    DbHelper dbHelper;
    ListView mListView;
    ListView mTypesListView;
    int currentTypeId;

    BroadcastReceiver refreshQuotesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mListView.setAdapter(new QuotesAdapter(dbHelper, context, dbHelper.getUnread()));
            setProgressBarIndeterminateVisibility(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(android.R.id.list);
        mTypesListView = (ListView) findViewById(R.id.types);
        mTypesListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types)));
        mTypesListView.setOnItemClickListener(this);
        dbHelper = new DbHelper(this);
        IntentFilter intentFilter = new IntentFilter(ActionsAndIntents.REFRESH);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(refreshQuotesReceiver, intentFilter);
        refreshing();
    }

    public void refreshing() {
        startService(new Intent(this, QuoteService.class).putExtra(ActionsAndIntents.TYPE_ID, currentTypeId));
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(refreshQuotesReceiver);
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            refreshing();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentTypeId = position;
        refreshing();
    }

    static class QuotesAdapter extends CursorAdapter {

        DbHelper mDbHelper;

        public QuotesAdapter(DbHelper dbHelper, Context context, Cursor c) {
            super(context, c, true);
            this.mDbHelper = dbHelper;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            assert view != null;
            viewHolder.date = (TextView) view.findViewById(android.R.id.text1);
            viewHolder.id = (TextView) view.findViewById(android.R.id.text2);
            viewHolder.text = (TextView) view.findViewById(R.id.text);
            view.setTag(viewHolder);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            ViewHolder viewHolder = (ViewHolder) view.getTag();
            String id = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID));
            String date = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_DATE));
            String text = cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_TEXT));
            viewHolder.date.setText(date);
            viewHolder.id.setText(id);
            viewHolder.text.setText(Html.fromHtml(text));
            mDbHelper.markRead(cursor.getLong(cursor.getColumnIndex(DbHelper.QUOTE_ID)));
        }

        static class ViewHolder {
            TextView date;
            TextView id;
            TextView text;
        }
    }
}
