package ru.barcats.tempo_leader_javanew.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.barcats.tempo_leader_javanew.R;
import androidx.preference.PreferenceFragmentCompat;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrefFragment extends PreferenceFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // создаём настройки из xml файла
        addPreferencesFromResource(R.xml.preferences);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

//    @Override
//    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
//        // создаём настройки из xml файла
//      addPreferencesFromResource(R.xml.preferences);
//    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        //находим NavController для фрагмента
//        NavController navController = Navigation.findNavController(view);
//
//        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
//        //конфигурация из графа обеспечивает правильную работу стрелок в тулбаре
//        AppBarConfiguration mAppBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph())
//                        .setDrawerLayout(drawerLayout)
//                        .build();
//        //инициализация  ToolBar();
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        toolbar.setTitle(R.string.Settings);
//        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
//    }
//
}