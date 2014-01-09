package ru.aim.anotheryetbashclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.*;
import android.widget.*;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.QuoteService;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.*;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

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
    BroadcastReceiver notifyBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(MainActivity.this, intent.getStringExtra(ActionsAndIntents.MESSAGE), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setOnItemLongClickListener(this);
        mTypesListView = (ListView) findViewById(R.id.types);
        mTypesListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.types)));
        mTypesListView.setOnItemClickListener(this);
        dbHelper = new DbHelper(this);
        IntentFilter intentFilter = new IntentFilter(ActionsAndIntents.REFRESH);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(refreshQuotesReceiver, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(ActionsAndIntents.NOTIFY);
        localBroadcastManager.registerReceiver(notifyBroadcastReceiver, intentFilter1);
        refreshing();
    }

    public void refreshing() {
        startService(new Intent(this, QuoteService.class).putExtra(TYPE_ID, currentTypeId));
        setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(refreshQuotesReceiver);
        localBroadcastManager.unregisterReceiver(notifyBroadcastReceiver);
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
            viewHolder.publicId = id;
            mDbHelper.markRead(cursor.getLong(cursor.getColumnIndex(DbHelper.QUOTE_ID)));
        }

        static class ViewHolder {
            TextView date;
            TextView id;
            TextView text;
            String publicId;
        }
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent, final View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = getResources().getStringArray(R.array.item_menu);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QuotesAdapter.ViewHolder viewHolder = (QuotesAdapter.ViewHolder) view.getTag();
                if (viewHolder != null) {
                    if (which == 0) {
                        startService(new Intent(MainActivity.this, QuoteService.class).
                                putExtra(TYPE_ID, RULEZ).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else if (which == 1) {
                        startService(new Intent(MainActivity.this, QuoteService.class).
                                putExtra(TYPE_ID, SUX).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else {
                        Toast.makeText(MainActivity.this, "Skip", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.show();
        return true;
    }
}
