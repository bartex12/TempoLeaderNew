package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import ru.barcats.tempo_leader_javanew.R;

public class SecundomerFragment extends Fragment {

    SecundomerViewModel secundomerViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        secundomerViewModel =
                new ViewModelProvider(requireActivity()).get(SecundomerViewModel.class);

        View root = inflater.inflate(R.layout.fragment_secundomer, container, false);

        secundomerViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
               // textView.setText(s);
            }
        });
        return root;
    }
}