package ru.barcats.tempo_leader_javanew.ui.sekundomer.grafic;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.barcats.tempo_leader_javanew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraficFragment extends Fragment {


    public GraficFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grafic, container, false);
    }

}
