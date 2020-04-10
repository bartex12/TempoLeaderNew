package ru.barcats.tempo_leader_javanew.ui.main;


import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import ru.barcats.tempo_leader_javanew.R;


import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String TAG ="33333";
    private boolean doubleBackToExitPressedOnce;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private  BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Вызов этого метода при выполнении onCreate() гарантирует, что  приложение
        // правильно инициализируется и получит настройки по умолчанию
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigation = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        toolbar = findViewById(R.id.toolbar);

        //включение setDrawerLayout(drawerLayout) даёт появление гамбургера в панели
        appBarConfiguration =
                new AppBarConfiguration.Builder(
                        navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();
        //обработка событий нижней навигации с помощью NavigationUI
        NavigationUI.setupWithNavController(bottomNavigation, navController);
        //обработка событий тулбара с помощью NavigationUI
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        //обработка событий меню шторки - если id меню совпадает с id в navigation
        NavigationUI.setupWithNavController(navigationView, navController);
        //добавляем слушатель изменений пункта назначения в NavController,
        //в методе обратного вызова проводим манипуляции с bottomNavigation и toolbar
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
               switch (destination.getId()){
                   case R.id.nav_home:
                       toolbar.setVisibility(View.VISIBLE);
                       bottomNavigation.setVisibility(View.GONE);
                       break;
                       case R.id.nav_help:
                           toolbar.setVisibility(View.GONE);
                           bottomNavigation.setVisibility(View.GONE);
                       break;
                   case R.id.nav_set:
                       toolbar.setVisibility(View.VISIBLE);
                       bottomNavigation.setVisibility(View.GONE);
                       break;
               }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //если  фрагмент HomeFragment закрываем программу по двойному щелчку Назад
        if (navController.getCurrentDestination().getId() == R.id.nav_home){
            //если флаг = true - а это при двойном щелчке - закрываем программу
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            //выставляем флаг = true
            this.doubleBackToExitPressedOnce = true;
            //закрываем шторку, если была открыта
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            //показываем Snackbar Для выхода нажмите  НАЗАД  ещё раз
            Snackbar.make(findViewById(android.R.id.content),
                    Objects.requireNonNull(this).getString(R.string.forExit),
                    Snackbar.LENGTH_SHORT).show();
            //запускаем поток, в котором через 2 секунды меняем флаг
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }else {
            super.onBackPressed();
        }
    }

}
