package ru.barcats.tempo_leader_javanew.ui.main;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
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
        setSupportActionBar(toolbar);

        //включение setDrawerLayout(drawerLayout) даёт появление гамбургера в панели
        appBarConfiguration =
                new AppBarConfiguration.Builder(
                        navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();

        //обработка событий нижней навигации с помощью NavigationUI
        NavigationUI.setupWithNavController(bottomNavigation, navController);
        //обработка событий тулбара - например смена заголовка - с помощью NavigationUI
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        //обработка событий меню шторки - если id меню совпадает с id в navigation
        NavigationUI.setupWithNavController(navigationView, navController);
        //обработка событий экшенбара
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        //добавляем слушатель изменений пункта назначения в NavController,
        //в методе обратного вызова проводим манипуляции с bottomNavigation и toolbar
        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(
                    @NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
               switch (destination.getId()){
                   case R.id.nav_rascladki:
                       toolbar.setVisibility(View.VISIBLE);
                       bottomNavigation.setVisibility(View.VISIBLE);
                       break;
                   case R.id.nav_help:
                   case R.id.nav_change_name:
                   case R.id.nav_home:
                   case R.id.nav_set:
                   case R.id.nav_secundomer:
                   case R.id.nav_tempoleader:
                       toolbar.setVisibility(View.VISIBLE);
                       bottomNavigation.setVisibility(View.GONE);
                       break;
               }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        //включаем звук
        AudioManager audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true;
    }

    //здесь будем менять опции меню для каждого CurrentDestination
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        int id = navController.getCurrentDestination().getId();
        switch (id){
            case R.id.nav_set:
                menu.findItem(R.id.nav_set).setVisible(false);
                menu.findItem(R.id.nav_help).setVisible(true);
                break;
            case R.id.nav_help:
                menu.findItem(R.id.nav_help).setVisible(false);
                menu.findItem(R.id.nav_set).setVisible(true);
                break;
                default:
                    menu.findItem(R.id.nav_help).setVisible(true);
                    menu.findItem(R.id.nav_set).setVisible(true);
                    break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //переопределение метода обработки пунктов верхнего меню - через NavController
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}
