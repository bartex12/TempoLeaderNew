package ru.barcats.tempo_leader_javanew.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceDialogFragmentCompat;
import ru.barcats.tempo_leader_javanew.R;

public class DialogPrefFragCompat extends PreferenceDialogFragmentCompat
        implements SeekBar.OnSeekBarChangeListener{

    public static final String TAG ="33333";
    private SeekBar seekBar;
    private int progr;

    public static DialogPrefFragCompat newInstance(String key) {
        final DialogPrefFragCompat fragment = new DialogPrefFragCompat();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seekbar_preference,container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        seekBar = view.findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
           SharedPreferences pr =
                   PreferenceManager.getDefaultSharedPreferences(requireActivity());
           //int progress = seekBar.getProgress();
            Log.d(TAG, "//**// DialogPrefFragCompat onDialogClosed progress = " + progr );
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        progr = seekBar.getProgress();
        Log.d(TAG, "//**// DialogPrefFragCompat onProgressChanged progress = " + progr );
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        progr = seekBar.getProgress();
        Log.d(TAG, "//**// DialogPrefFragCompat onStartTrackingTouch progress = " + progr );
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        progr = seekBar.getProgress();
        Log.d(TAG, "//**// DialogPrefFragCompat onStopTrackingTouch progress = " + progr );
    }
}