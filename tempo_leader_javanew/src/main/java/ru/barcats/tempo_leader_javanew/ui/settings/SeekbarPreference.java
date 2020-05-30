package ru.barcats.tempo_leader_javanew.ui.settings;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.DialogPreference;
import ru.barcats.tempo_leader_javanew.R;

public class SeekbarPreference extends DialogPreference {

    public SeekbarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.seekbar_preference);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }


}
