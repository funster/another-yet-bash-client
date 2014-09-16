package ru.aim.anotheryetbashclient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.EditText;

import ru.aim.anotheryetbashclient.R;

public class SearchDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public static final String DEFAULT_TAG = "searchDialog";

    EditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.search_dialog_title);
        builder.setPositiveButton(R.string.ok, this);
        View view = View.inflate(getActivity(), R.layout.search_dialog, null);
        editText = (EditText) view.findViewById(android.R.id.text1);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
    }

    public void show(FragmentManager manager) {
        show(manager, DEFAULT_TAG);
    }
}
