package ru.barcats.tempo_leader_javanew.ui.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.preference.PreferenceManager;
import android.util.Log;
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

import android.view.Menu;

public class MainActivity extends AppCompatActivity {

    public static final String TAG ="33333";
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
        Log.d(TAG, "MainActivity onBackPressed");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //openQuitDialog();
            super.onBackPressed();
        }
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(this);
        quitDialog.setTitle(R.string.ExitYesNo);

        quitDialog.setPositiveButton(R.string.DeleteNo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        quitDialog.setNegativeButton(R.string.DeleteYes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        quitDialog.show();
    }

}
