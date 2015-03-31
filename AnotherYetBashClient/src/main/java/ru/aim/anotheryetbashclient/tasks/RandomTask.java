package ru.aim.anotheryetbashclient.tasks;

import android.text.TextUtils;

import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRoot;
import static ru.aim.anotheryetbashclient.loaders.Package.wrapWithRootWithoutSlash;

public class RandomTask extends TemplateTask {

    static final String URL = wrapWithRoot("random");
    static final String MORE_URL = wrapWithRootWithoutSlash("%s");

    String more;

    @Override
    protected String getUrl() {
        if (!TextUtils.isEmpty(more)) {
            return String.format(MORE_URL, more);
        } else {
            return URL;
        }
    }
}
