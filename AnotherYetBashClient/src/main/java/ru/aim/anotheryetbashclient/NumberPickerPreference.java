package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

/*
 *
 * @author Danesh
 * @author nebkat
 */
public class NumberPickerPreference extends DialogPreference {

    private int mMin, mMax, mDefault;

    private String mMaxExternalKey, mMinExternalKey;

    private NumberPicker mNumberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray numberPickerType = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference, 0, 0);

        mMaxExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_maxExternal);
        mMinExternalKey = numberPickerType.getString(R.styleable.NumberPickerPreference_minExternal);

        mMax = numberPickerType.getInt(R.styleable.NumberPickerPreference_max, 5);
        mMin = numberPickerType.getInt(R.styleable.NumberPickerPreference_min, 0);
        mDefault = numberPickerType.getInt(R.styleable.NumberPickerPreference_default_value, 0);

        numberPickerType.recycle();

        setNegativeButtonText(R.string.cancel);
        setPositiveButtonText(R.string.ok);
    }

    @Override
    protected View onCreateDialogView() {
        int max = mMax;
        int min = mMin;

        // External values
        if (mMaxExternalKey != null) {
            max = getSharedPreferences().getInt(mMaxExternalKey, mMax);
        }
        if (mMinExternalKey != null) {
            min = getSharedPreferences().getInt(mMinExternalKey, mMin);
        }

        LayoutInflater inflater =
                (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker_dialog, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);

        // Initialize state
        mNumberPicker.setMaxValue(max);
        mNumberPicker.setMinValue(min);
        mNumberPicker.setValue(getPersistedInt(mDefault));
        mNumberPicker.setWrapSelectorWheel(false);

        return view;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            persistInt(mNumberPicker.getValue());
            setSummary(getContext().getString(R.string.auto_update_depth_summary, mNumberPicker.getValue()));
        }
    }
}