package ru.aim.anotheryetbashclient;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;

public class AboutDialog extends DialogFragment implements View.OnClickListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.about_screen, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.about_app_title);
        builder.setView(view);
        view.findViewById(android.R.id.text1).setOnClickListener(this);
        view.findViewById(android.R.id.text2).setOnClickListener(this);
        builder.setPositiveButton(R.string.ok, null);
        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.text1:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.support_email)});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_email_subject));
                getActivity().startActivity(intent);
                break;
            case android.R.id.text2:
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(getString(R.string.bash_url)));
                startActivity(i);
                break;
        }
    }
}
