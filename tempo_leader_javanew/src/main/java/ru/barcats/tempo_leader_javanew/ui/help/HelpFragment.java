package ru.barcats.tempo_leader_javanew.ui.help;


import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.barcats.tempo_leader_javanew.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFragment extends Fragment {

    private HelpViewModel helpViewModel;
    private static final String TAG ="33333";
    private NavController navController;
    private Toolbar toolbar;
    private AppBarConfiguration mAppBarConfiguration;

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

        //в альбомной ориентации отключаем картинки, чтобы было больше места
        if(getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE){
            imageViewLeft.setVisibility(View.GONE);
            imageViewRigth.setVisibility(View.GONE);
        }

        helpViewModel = ViewModelProviders.of(this).get(HelpViewModel.class);
        helpViewModel.getHelp().observe(getViewLifecycleOwner(), new Observer<StringBuilder>() {
            @Override
            public void onChanged(StringBuilder stringBuilder) {
                //textView.setText(stringBuilder);
                textView.setText(Html.fromHtml(stringBuilder.toString()));
            }
        });
        return root;
    }
}
