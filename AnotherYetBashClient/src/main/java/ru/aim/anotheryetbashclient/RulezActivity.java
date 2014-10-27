package ru.aim.anotheryetbashclient;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.Toast;

import ru.aim.anotheryetbashclient.loaders.RulezLoader;
import ru.aim.anotheryetbashclient.loaders.RulezType;
import ru.aim.anotheryetbashclient.loaders.SimpleResult;

import static ru.aim.anotheryetbashclient.loaders.RulezLoader.RulezResult;

public abstract class RulezActivity extends FragmentActivity {

    public void sendRulez(String id, RulezType type) {
        if (id.contains("#")) {
            id = id.replace("#", "");
        }
        Bundle bundle = RulezLoader.buildBundle(id, type);
        getSupportLoaderManager().restartLoader(1, bundle, loaderCallbacks);
    }

    LoaderManager.LoaderCallbacks<SimpleResult<RulezResult>> loaderCallbacks = new LoaderManager.LoaderCallbacks<SimpleResult<RulezResult>>() {
        @Override
        public Loader<SimpleResult<RulezResult>> onCreateLoader(int id, Bundle args) {
            return id == 1 ? new RulezLoader(RulezActivity.this, args) : null;
        }

        @Override
        public void onLoadFinished(Loader<SimpleResult<RulezResult>> loader, SimpleResult<RulezResult> data) {
            if (data.containsError()) {
                Toast.makeText(RulezActivity.this, R.string.error_send_request, Toast.LENGTH_LONG).show();
            } else {
                RulezResult rulezResult = data.getResult();
                if (rulezResult.isOk) {
                    Toast.makeText(RulezActivity.this, R.string.vote_accepted, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<SimpleResult<RulezResult>> loader) {
        }
    };
}
