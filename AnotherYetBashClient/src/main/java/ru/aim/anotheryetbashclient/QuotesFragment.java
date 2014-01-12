package ru.aim.anotheryetbashclient;

import android.app.AlertDialog;
import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.ObjectSerializer;
import ru.aim.anotheryetbashclient.helper.QuoteService;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.*;

public class QuotesFragment extends Fragment implements AdapterView.OnItemLongClickListener {

    DbHelper dbHelper;
    ListView listView;
    BroadcastReceiver refreshQuotesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Cursor cursor = dbHelper.getUnread();
            saveCurrentCursor(cursor);
            listView.setAdapter(new QuotesAdapter(dbHelper, context, cursor));
            getActivity().setProgressBarIndeterminateVisibility(false);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_list, null);
        assert result != null;
        listView = (ListView) result.findViewById(android.R.id.list);
        listView.setOnItemLongClickListener(this);
        dbHelper = new DbHelper(getActivity());
        if (savedCursorExists()) {
            callRefresh();
        } else {
            listView.setAdapter(new QuotesAdapter(dbHelper, getActivity(), dbHelper.getQuotes(getSavedCursor())));
        }
        IntentFilter intentFilter = new IntentFilter(ActionsAndIntents.REFRESH);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.registerReceiver(refreshQuotesReceiver, intentFilter);

        return result;
    }

    public void callRefresh() {
        getActivity().startService(new Intent(getActivity(), QuoteService.class).putExtra(TYPE_ID, ActionsAndIntents.TYPE_RANDOM));
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    void saveCurrentCursor(Cursor cursor) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ArrayList<String> list = new ArrayList<String>();
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(DbHelper.QUOTE_PUBLIC_ID)));
        }
        String bin = ObjectSerializer.serialize(list);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CURRENT_QUOTES, bin);
        editor.commit();
    }

    boolean savedCursorExists() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null) == null;
    }

    @SuppressWarnings("unchecked")
    String[] getSavedCursor() {
        String bin = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(CURRENT_QUOTES, null);
        ArrayList<String> list = (ArrayList<String>) ObjectSerializer.deserialize(bin);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public void onDestroyView() {
        dbHelper.close();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getActivity());
        localBroadcastManager.unregisterReceiver(refreshQuotesReceiver);
        super.onDestroyView();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String[] items = getResources().getStringArray(R.array.item_menu);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                QuotesAdapter.ViewHolder viewHolder = (QuotesAdapter.ViewHolder) view.getTag();
                if (viewHolder != null) {
                    if (which == 0) {
                        getActivity().startService(new Intent(getActivity(), QuoteService.class).
                                putExtra(TYPE_ID, TYPE_RULEZ).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else if (which == 1) {
                        getActivity().startService(new Intent(getActivity(), QuoteService.class).
                                putExtra(TYPE_ID, TYPE_SUX).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else {
                        Toast.makeText(getActivity(), "Skip", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.show();
        return true;
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
}
