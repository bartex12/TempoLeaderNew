package ru.barcats.tempo_leader_javanew.ui.help;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import ru.barcats.tempo_leader_javanew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private HelpViewModel helpViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_help, container, false);

        final TextView textView = root.findViewById(R.id.textViewHelpMain);
        ImageView imageViewLeft = root.findViewById(R.id.imageViewLeft);
        imageViewLeft.setImageDrawable(getActivity().getResources().
                getDrawable(R.drawable.help_magistr));
        ImageView imageViewRigth = root.findViewById(R.id.imageViewRigth);
        imageViewRigth.setImageDrawable(getActivity().getResources().
                getDrawable(R.drawable.help_magistr));

        helpViewModel = ViewModelProviders.of(this).get(HelpViewModel.class);
        helpViewModel.getHelp().observe(getViewLifecycleOwner(), new Observer<StringBuilder>() {
            @Override
            public void onChanged(StringBuilder stringBuilder) {
                textView.setText(stringBuilder);
            }
        });
        return root;
    }

}
