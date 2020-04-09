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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //находим NavController
        navController = Navigation.
                findNavController(getActivity(), R.id.nav_host_fragment);

        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        //конфигурация из графа обеспечивает правильную работу стрелок в тулбаре
        mAppBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();

        //инициализация  ToolBar();
        toolbar = view.findViewById(R.id.toolbar);
        //придётся добираться до ActionBar, чтобы сделать меню
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setTitle(R.string.main_menu);
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
        // разрешаем меню ActionBar во фрагменте
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.help_menu, menu);
    }

    //переопределяем метод для работы с navController -для этого в navigation и меню должны
    //быть одинаковые id пункта назначения и id пункта меню
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        if (item.getItemId() == R.id.nav_set) {
//            Log.d(TAG, "OptionsItem = action_settings");
//            navController.navigate(R.id.action_nav_help_to_nav_set);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
