package ru.pavelshackih.anotheryetbashclient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.NumberPicker;

import ru.pavelshackih.anotheryetbashclient.R;

public class NumberPickerDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String DEFAULT_TAG = "NumberPickerDialog";

    protected int curPage;
    protected int maxPage;
    protected NumberPicker picker;

    public static Bundle buildArgs(int curPage, int maxPage) {
        Bundle bundle = new Bundle();
        bundle.putInt("curPage", curPage);
        bundle.putInt("maxPage", maxPage);
        return bundle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        curPage = getArguments().getInt("curPage");
        maxPage = getArguments().getInt("maxPage");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.page_picker, null);
        picker = (NumberPicker) view.findViewById(R.id.picker);
        picker.setMaxValue(maxPage);
        picker.setMinValue(1);
        picker.setValue(curPage);
        builder.setView(view);
        builder.setTitle(R.string.select_page);
        builder.setPositiveButton(R.string.ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    public void show(FragmentManager manager) {
        show(manager, DEFAULT_TAG);
    }
}
