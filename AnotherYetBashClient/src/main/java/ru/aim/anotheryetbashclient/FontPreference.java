package ru.aim.anotheryetbashclient;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

@SuppressWarnings("unused")
public class FontPreference extends Preference implements SeekBar.OnSeekBarChangeListener {

    TextView exampleTextView;
    SeekBar seekBar;

    int mMax = 28;
    int mProgress = 0;
    boolean mTrackingTouch;
    int mMinValue;
    int mMaxValue;
    int mMidValue;

    public FontPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutResource(R.layout.preference_seekbar);
        TypedArray a = context.getTheme().obtainStyledAttributes(android.R.style.TextAppearance_Small, new int[]{android.R.attr.textSize});
        mMinValue = toSp(a.getString(0));
        a.recycle();
        a = context.getTheme().obtainStyledAttributes(android.R.style.TextAppearance_Large, new int[]{android.R.attr.textSize});
        mMaxValue = toSp(a.getString(0));
        a.recycle();
        a = context.getTheme().obtainStyledAttributes(android.R.style.TextAppearance_Medium, new int[]{android.R.attr.textSize});
        mMidValue = toSp(a.getString(0));
        a.recycle();
    }

    static int toSp(String spText) {
        return Integer.parseInt(spText.replace(".0sp", ""));
    }

    public FontPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        seekBar = (SeekBar) view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        mProgress = getPersistedInt(0);
        if (mProgress == 0) {
            mProgress = mMidValue - mMinValue;
        } else {
            mProgress = mProgress - mMinValue;
        }
        seekBar.setProgress(mProgress);
        seekBar.setMax(mMaxValue - mMinValue);
        seekBar.setEnabled(isEnabled());
        exampleTextView = (TextView) view.findViewById(R.id.summary);
        applyFontSize(mProgress);
    }

    @Override
    public CharSequence getSummary() {
        return null;
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
//        setProgress(restoreValue ? getPersistedInt(mProgress)
//                : (Integer) defaultValue);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    public void setMax(int max) {
        if (max != mMax) {
            mMax = max;
            notifyChanged();
        }
    }

    public void setProgress(int progress) {
        setProgress(progress, true);
    }

    private void setProgress(int progress, boolean notifyChanged) {
        if (progress > mMax) {
            progress = mMax;
        }
        if (progress < 0) {
            progress = 0;
        }
        if (progress != mProgress) {
            mProgress = progress;
            persistInt(progress + mMinValue);
            if (notifyChanged) {
                notifyChanged();
            }
        }
    }

    public int getProgress() {
        return mProgress;
    }

    /**
     * Persist the seekBar's progress value if callChangeListener
     * returns true, otherwise set the seekBar's progress to the stored value
     */
    void syncProgress(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        if (progress != mProgress) {
            if (callChangeListener(progress)) {
                setProgress(progress, true);
            } else {
                seekBar.setProgress(mProgress);
            }
        }
        applyFontSize(progress);
    }

    void applyFontSize(int progress) {
        final int inSp = progress + mMinValue;
        Handler handler = new Handler(Looper.getMainLooper());
        exampleTextView.setTextSize(inSp);
        exampleTextView.setText(getContext().getString(R.string.font_size_example, inSp));
        exampleTextView.getParent().requestLayout();
    }

    @Override
    public void onProgressChanged(
            SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser && !mTrackingTouch) {
            syncProgress(seekBar);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        mTrackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mTrackingTouch = false;
        if (seekBar.getProgress() != mProgress) {
            syncProgress(seekBar);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        /*
         * Suppose a client uses this preference type without persisting. We
         * must save the instance state so it is able to, for example, survive
         * orientation changes.
         */

        final Parcelable superState = super.onSaveInstanceState();
        if (isPersistent()) {
            // No need to save instance state since it's persistent
            return superState;
        }

        // Save the instance state
        final SavedState myState = new SavedState(superState);
        myState.progress = mProgress;
        myState.max = mMax;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!state.getClass().equals(SavedState.class)) {
            // Didn't save state for us in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }

        // Restore the instance state
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        mProgress = myState.progress;
        mMax = myState.max;
        notifyChanged();
    }

    /**
     * SavedState, a subclass of {@link BaseSavedState}, will store the state
     * of MyPreference, a subclass of Preference.
     * <p/>
     * It is important to always call through to super methods.
     */
    private static class SavedState extends BaseSavedState {
        int progress;
        int max;

        public SavedState(Parcel source) {
            super(source);

            // Restore the click counter
            progress = source.readInt();
            max = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);

            // Save the click counter
            dest.writeInt(progress);
            dest.writeInt(max);
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
