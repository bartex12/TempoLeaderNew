package ru.barcats.tempo_leader_javanew.ui.raskladki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ru.barcats.tempo_leader_javanew.R;

public class RaskladkiFragment extends Fragment {

    private RaskladkiViewModel raskladkiViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        raskladkiViewModel =
                ViewModelProviders.of(this).get(RaskladkiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_raskladki, container, false);
        final TextView textView = root.findViewById(R.id.text_rascladki);
        raskladkiViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}