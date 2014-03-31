package ru.aim.anotheryetbashclient.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.DbHelper;
import ru.aim.anotheryetbashclient.helper.ObjectSerializer;
import ru.aim.anotheryetbashclient.helper.QuoteService;

import java.util.ArrayList;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.*;

public class RandomBashFragment extends AbstractBashFragment {

    String next;

    void nextPage() {
        Intent intent = new Intent(getActivity(), QuoteService.class);
        intent.putExtra(TYPE_ID, ActionsAndIntents.TYPE_RANDOM);
        if (next != null) {
            intent.putExtra(ActionsAndIntents.NEXT_PAGE, next);
        }
        getActivity().startService(intent);
        setRefreshing(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        nextPage();
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
                    } else if (which == 2) {
                        getDbHelper().addToFavorite(viewHolder.innerId);
                        Toast.makeText(getActivity(), R.string.added_to_favorites, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.show();
        return true;
    }

    @Override
    public void onReceive(Intent intent) {
        loadCursor(intent);
        if (intent.hasExtra(ActionsAndIntents.NEXT_PAGE)) {
            next = intent.getStringExtra(ActionsAndIntents.NEXT_PAGE);
        }
        setListAdapter(new QuotesAdapter(getDbHelper(), getActivity(), cursor));
        setRefreshing(false);
    }

    @Override
    public void onManualUpdate() {
        nextPage();
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_RANDOM;
    }
}
