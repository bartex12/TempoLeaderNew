package ru.barcats.tempo_leader_javanew.ui.main;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
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
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.TempoleaderFragment;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.editor.EditorFragment;


import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements TempoleaderFragment.OnTransmitListener, EditorFragment.SaverFragmentListener {

    public static final String TAG ="33333";
    public static final int  VALUE = 10;  //  величина изменения темпа по умолчанию
    private boolean doubleBackToExitPressedOnce;
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private  BottomNavigationView bottomNavigation;
    private String data;
    private long fileIdCopy;
    boolean isChangeTemp;
    boolean isSaveVisible;

    @Override
    public void onTransmit(String data) {
        this.data = data;
        Log.d(TAG, "//**// MainActivity onTransmit data = " + data );
    }

    @Override
    public void onFileCopyTransmit(long fileIdCopy, boolean isChangeTemp, boolean isSaveVisible) {
        this.fileIdCopy = fileIdCopy;
        this.isChangeTemp = isChangeTemp;
        this.isSaveVisible = isSaveVisible;
        Log.d(TAG, "//**// MainActivity onFileCopyTransmit fileIdCopy = " + fileIdCopy );
    }

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
                menu.findItem(R.id.nav_rascladki).setVisible(false);
                menu.findItem(R.id.nav_editor).setVisible(false);
                menu.findItem(R.id.nav_grafic).setVisible(false);
                menu.findItem(R.id.menu_item_new_frag).setVisible(false);
                menu.findItem(R.id.change_temp_up_down).setVisible(false);
                menu.findItem(R.id.save_data_in_file).setVisible(false);
                break;
            case R.id.nav_help:
                menu.findItem(R.id.nav_set).setVisible(true);
                menu.findItem(R.id.nav_help).setVisible(false);
                menu.findItem(R.id.nav_rascladki).setVisible(false);
                menu.findItem(R.id.nav_editor).setVisible(false);
                menu.findItem(R.id.nav_grafic).setVisible(false);
                menu.findItem(R.id.menu_item_new_frag).setVisible(false);
                menu.findItem(R.id.change_temp_up_down).setVisible(false);
                menu.findItem(R.id.save_data_in_file).setVisible(false);
                break;
            case R.id.nav_tempoleader:
                menu.findItem(R.id.nav_help).setVisible(true);
                menu.findItem(R.id.nav_set).setVisible(true);
                menu.findItem(R.id.nav_rascladki).setVisible(true);
                menu.findItem(R.id.nav_editor).setVisible(true);
                menu.findItem(R.id.nav_grafic).setVisible(false);
                menu.findItem(R.id.menu_item_new_frag).setVisible(false);
                menu.findItem(R.id.change_temp_up_down).setVisible(false);
                menu.findItem(R.id.save_data_in_file).setVisible(false);
                break;
            case R.id.nav_rascladki:
            case R.id.nav_new_exercise:
            case R.id.nav_home:
            case R.id.nav_grafic:
                menu.findItem(R.id.nav_help).setVisible(true);
                menu.findItem(R.id.nav_set).setVisible(true);
                menu.findItem(R.id.nav_rascladki).setVisible(false);
                menu.findItem(R.id.nav_editor).setVisible(false);
                menu.findItem(R.id.nav_grafic).setVisible(false);
                menu.findItem(R.id.menu_item_new_frag).setVisible(false);
                menu.findItem(R.id.change_temp_up_down).setVisible(false);
                menu.findItem(R.id.save_data_in_file).setVisible(false);
                break;
            case R.id.nav_secundomer:
                menu.findItem(R.id.nav_help).setVisible(true);
                menu.findItem(R.id.nav_set).setVisible(true);
                menu.findItem(R.id.nav_rascladki).setVisible(false);
                menu.findItem(R.id.nav_editor).setVisible(false);
                menu.findItem(R.id.nav_grafic).setVisible(true);
                break;

            case R.id.nav_editor:
                menu.findItem(R.id.nav_help).setVisible(true);
                menu.findItem(R.id.nav_set).setVisible(true);
                menu.findItem(R.id.nav_rascladki).setVisible(false);
                menu.findItem(R.id.nav_editor).setVisible(false);
                menu.findItem(R.id.nav_grafic).setVisible(false);
                menu.findItem(R.id.menu_item_new_frag).setVisible(true);
                if (isChangeTemp){
                    menu.findItem(R.id.change_temp_up_down).setVisible(true);
                }else {
                    menu.findItem(R.id.change_temp_up_down).setVisible(false);
                }
                if (isSaveVisible){
                    menu.findItem(R.id.save_data_in_file).setVisible(true);
                }else {
                    menu.findItem(R.id.save_data_in_file).setVisible(false);
                }
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //переопределение метода обработки пунктов верхнего меню - через NavController
    //очень круто- если id пунктов графа и меню совпадают, переход происходит автоматически
    //А КАК БЫТЬ С ДАННЫМИ?
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Bundle bundle = null;
    switch (item.getItemId()){
        case R.id.nav_editor:
            bundle = new Bundle();
            bundle.putString(P.NAME_OF_FILE, data);
            navController.navigate(R.id.nav_editor, bundle);
            Log.d(TAG, "//****// MainActivity onOptionsItemSelected data = " + data );
            break;
        case R.id.save_data_in_file:
            bundle = new Bundle();
            bundle.putString(P.NAME_OF_FILE, data);
            bundle.putLong(P.FINISH_FILE_ID, fileIdCopy);
            navController.navigate(R.id.action_nav_editor_to_dialogSaveTempFragment, bundle);
            Log.d(TAG, "//****// MainActivity onOptionsItemSelected fileIdCopy = " + fileIdCopy );
            break;
        case R.id.change_temp_up_down:
            bundle = new Bundle();
            bundle.putInt(P.ARG_VALUE_CHANGE_TEMP, VALUE);
            bundle.putString(P.NAME_OF_FILE, data);
           // bundle.putInt(P.FROM_ACTIVITY, P.DIALOG_CHANGE_TEMP); // изменить темп
            navController.navigate(R.id.action_nav_editor_to_dialogChangeTemp, bundle);
            break;
        case R.id.menu_item_new_frag:
            bundle = new Bundle();
            bundle.putString(P.NAME_OF_FILE, data);
            bundle.putInt(P.FROM_ACTIVITY, P.TO_ADD_FRAG); // добавить фрагмент подхода
            navController.navigate(R.id.action_nav_editor_to_newSetDialog, bundle);
            break;
            default:
                return NavigationUI.onNavDestinationSelected(item, navController)
                        || super.onOptionsItemSelected(item);
    }
    return super.onOptionsItemSelected(item);
    }
}
