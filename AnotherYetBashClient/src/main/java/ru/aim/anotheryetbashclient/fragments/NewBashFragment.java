package ru.aim.anotheryetbashclient.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Toast;
import ru.aim.anotheryetbashclient.ActionsAndIntents;
import ru.aim.anotheryetbashclient.QuotesAdapter;
import ru.aim.anotheryetbashclient.R;
import ru.aim.anotheryetbashclient.helper.QuoteService;

import java.util.ArrayList;
import java.util.List;

import static ru.aim.anotheryetbashclient.ActionsAndIntents.*;

public class NewBashFragment extends AbstractBashFragment {

    int currentPage;
    int maxPage;

    @Override
    protected void afterViewCreated() {
        super.afterViewCreated();
        sendRequest();
    }

    void sendRequest() {
        getActivity().invalidateOptionsMenu();
        Intent intent = new Intent(getActivity(), QuoteService.class);
        intent.putExtra(TYPE_ID, getCurrentType());
        if (currentPage > 0) {
            intent.putExtra(ActionsAndIntents.CURRENT_PAGE, currentPage);
        }
        doStartService(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentPage > 0) {
            outState.putInt(ActionsAndIntents.CURRENT_PAGE, currentPage);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final List<String> list = new ArrayList<>();
        if (isOnline()) {
            list.add(getString(R.string.like));
            list.add(getString(R.string.dont_like));
        }
        final QuotesAdapter.ViewHolder viewHolder = (QuotesAdapter.ViewHolder) view.getTag();
        if (getDbHelper().isFavorite(viewHolder.innerId)) {
            list.add(getString(R.string.remove_from_favorites));
        } else {
            list.add(getString(R.string.add_to_favorites));
        }
        String[] items = list.toArray(new String[list.size()]);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (viewHolder != null) {
                    if (getString(R.string.like).equals(list.get(which))) {
                        getActivity().startService(new Intent(getActivity(), QuoteService.class).
                                putExtra(TYPE_ID, TYPE_RULEZ).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else if (getString(R.string.dont_like).equals(list.get(which))) {
                        getActivity().startService(new Intent(getActivity(), QuoteService.class).
                                putExtra(TYPE_ID, TYPE_SUX).putExtra(ActionsAndIntents.QUOTE_ID, viewHolder.publicId));
                    } else if (getString(R.string.add_to_favorites).equals(list.get(which))) {
                        getDbHelper().addToFavorite(viewHolder.innerId);
                        Toast.makeText(getActivity(), R.string.added_to_favorites, Toast.LENGTH_LONG).show();
                    } else if (getString(R.string.remove_from_favorites).equals(list.get(which))) {
                        getDbHelper().removeFromFavorite(viewHolder.innerId);
                        Toast.makeText(getActivity(), R.string.removed_to_favorites, Toast.LENGTH_LONG).show();
                    } else {
                        throw new AssertionError("Unknown action");
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
        if (intent.hasExtra(ActionsAndIntents.CURRENT_PAGE)) {
            currentPage = intent.getIntExtra(ActionsAndIntents.CURRENT_PAGE, 0);
        }
        if (intent.hasExtra(ActionsAndIntents.MAX_PAGE)) {
            maxPage = intent.getIntExtra(ActionsAndIntents.MAX_PAGE, 0);
        }
        setListAdapter(new QuotesAdapter(getDbHelper(), getActivity(), cursor));
        getActivity().invalidateOptionsMenu();
        setRefreshing(false);
    }

    @Override
    public void onManualUpdate() {
        sendRequest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_forward) {
            currentPage -= 1;
            sendRequest();
            return true;
        }
        if (item.getItemId() == R.id.action_back) {
            currentPage += 1;
            sendRequest();
            return true;
        }
        if (item.getItemId() == R.id.action_page) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View view = View.inflate(getActivity(), R.layout.page_picker, null);
            final NumberPicker picker = (NumberPicker) view.findViewById(R.id.picker);
            picker.setMaxValue(maxPage);
            picker.setMinValue(1);
            picker.setValue(currentPage);
            builder.setView(view);
            builder.setTitle(R.string.select_page);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    currentPage = picker.getValue();
                    sendRequest();
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getType() {
        return ActionsAndIntents.TYPE_NEW;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news, menu);
        MenuItem menuItem = menu.findItem(R.id.action_page);
        if (currentPage != 0) {
            menuItem.setTitle(Integer.toString(currentPage));
        }
        checkItemsVisibility(menu);
        if (maxPage != 0 && isItemsVisible()) {
            menu.findItem(R.id.action_back).setVisible(currentPage != maxPage);
            menu.findItem(R.id.action_forward).setVisible(currentPage != 0);
        }
    }

    @Override
    public void saveData() {
        super.saveData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putInt(ActionsAndIntents.CURRENT_PAGE, currentPage).
                putInt(ActionsAndIntents.MAX_PAGE, maxPage).commit();
    }

    @Override
    protected void loadData() {
        super.loadData();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        currentPage = preferences.getInt(ActionsAndIntents.CURRENT_PAGE, 0);
        maxPage = preferences.getInt(ActionsAndIntents.MAX_PAGE, 0);
        getActivity().invalidateOptionsMenu();
    }
}
