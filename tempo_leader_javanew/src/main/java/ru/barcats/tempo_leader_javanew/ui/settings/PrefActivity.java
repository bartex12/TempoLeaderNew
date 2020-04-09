package ru.barcats.tempo_leader_javanew.ui.settings;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.barcats.tempo_leader_javanew.R;

public class PrefActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        //находим NavController для фрагмента
//        NavController navController = Navigation.findNavController(
//                PrefActivity.this, R.id.nav_host_fragment);
//
//        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
//        //конфигурация из графа обеспечивает правильную работу стрелок в тулбаре
//        AppBarConfiguration mAppBarConfiguration =
//                new AppBarConfiguration.Builder(navController.getGraph())
//                        .setDrawerLayout(drawerLayout)
//                        .build();
//        //инициализация  ToolBar();
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        //придётся добираться до ActionBar, чтобы сделать меню
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(R.string.Settings);
//        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);

        // меняем фрагмент в контейнере
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PrefFragment())
                .commit();
    }
}
