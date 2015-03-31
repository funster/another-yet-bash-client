package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.res.TypedArray;

public class AttrUtils {

    public static int resolveResource(Context context, int attr) {
        int[] attribute = new int[]{attr};
        TypedArray array = context.obtainStyledAttributes(attribute);
        int reference = array.getResourceId(0, 0);
        array.recycle();
        return reference;
    }
}
