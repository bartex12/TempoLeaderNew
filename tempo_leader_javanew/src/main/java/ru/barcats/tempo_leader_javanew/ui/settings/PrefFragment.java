package ru.barcats.tempo_leader_javanew.ui.settings;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import ru.barcats.tempo_leader_javanew.R;
import androidx.preference.PreferenceFragmentCompat;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrefFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // создаём настройки из xml файла
        addPreferencesFromResource(R.xml.preferences);
    }

}
