package ru.aim.anotheryetbashclient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;
import ru.aim.anotheryetbashclient.R;

import java.util.Calendar;

/**
 *
 */
public abstract class DateDialog extends DialogFragment implements DialogInterface.OnClickListener, NumberPicker.OnValueChangeListener {

    Calendar now = Calendar.getInstance();
    NumberPicker year;
    NumberPicker month;
    NumberPicker day;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.custom_date_picker, null);

        year = (NumberPicker) view.findViewById(R.id.year);
        year.setMaxValue(now.get(Calendar.YEAR));
        year.setMinValue(2004);
        year.setValue(year.getMaxValue());
        year.setOnValueChangedListener(this);
        month = (NumberPicker) view.findViewById(R.id.month);
        month.setMaxValue(12);
        month.setMinValue(1);
        month.setDisplayedValues(getActivity().getResources().getStringArray(R.array.months));
        checkMonthInCurrentYear();
        month.setValue(month.getMaxValue());

        builder.setView(view);
        builder.setPositiveButton(R.string.ok, this);
        builder.setNeutralButton(R.string.today, this);
        builder.setTitle(R.string.choose_period);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        DateResult dateResult = new DateResult();
        if (which == AlertDialog.BUTTON_NEUTRAL) {
            // today
            dateResult.year = -1;
            dateResult.month = -1;
        } else if (which == AlertDialog.BUTTON_POSITIVE) {
            // custom date
            dateResult.year = year.getValue();
            dateResult.month = month.getValue();
        }
        BaseFragment.sendMessage(getActivity(), sendTo(), dateResult);
    }

    abstract Class<? extends BaseFragment> sendTo();

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        checkMonthInCurrentYear();
    }

    void checkMonthInCurrentYear() {
        if (year.getValue() == now.get(Calendar.YEAR)) {
            int value = month.getValue();
            month.setMaxValue(now.get(Calendar.MONTH) + 1);
            if (value > month.getMaxValue()) {
                month.setValue(month.getMaxValue());
            }
        } else {
            month.setMaxValue(12);
        }
    }
}