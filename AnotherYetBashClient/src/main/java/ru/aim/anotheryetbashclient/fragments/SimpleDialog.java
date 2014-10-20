package ru.aim.anotheryetbashclient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import ru.aim.anotheryetbashclient.R;

public class SimpleDialog extends DialogFragment {

    public static final String DEFAULT_TAG = "simpleDialog";
    public static final String MESSAGE = "message";

    public static SimpleDialog newInstance(String message) {
        Bundle bundle = new Bundle();
        SimpleDialog simpleDialog = new SimpleDialog();
        bundle.putString(MESSAGE, message);
        simpleDialog.setArguments(bundle);
        return simpleDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.load_error);
        builder.setTitle(R.string.error);
        builder.setPositiveButton(R.string.ok, null);
        return builder.create();
    }

    public void show(FragmentManager fragmentManager) {
        show(fragmentManager, DEFAULT_TAG);
    }
}
