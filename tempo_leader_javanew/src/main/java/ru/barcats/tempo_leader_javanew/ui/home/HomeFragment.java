package ru.barcats.tempo_leader_javanew.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.ui.main.RecyclerViewMainAdapter;
import ru.barcats.tempo_leader_javanew.model.DataHome;

public class HomeFragment extends Fragment {

    private static final String TAG ="33333";
    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private NavController navController;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewHome);

        homeViewModel.getListMain().observe(getViewLifecycleOwner(),
                new Observer<ArrayList<DataHome>>() {
            @Override
            public void onChanged(ArrayList<DataHome> dataHomes) {
                showMainList(dataHomes);
            }
        });

        //находим NavController для фрагмента
        navController = Navigation.findNavController(view);

        DrawerLayout drawerLayout = view.findViewById(R.id.drawer_layout);
        //конфигурация из графа обеспечивает правильную работу стрелок в тулбаре
        AppBarConfiguration mAppBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .setDrawerLayout(drawerLayout)
                        .build();
        //инициализация  ToolBar();
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        //придётся добираться до ActionBar, чтобы сделать меню
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setTitle(R.string.main_menu);
        NavigationUI.setupWithNavController(toolbar, navController, mAppBarConfiguration);
        // разрешаем меню ActionBar во фрагменте
        setHasOptionsMenu(true);
    }

    private void showMainList(ArrayList<DataHome> dataHomes) {
        //используем встроенный GridLayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        //передаём список в адаптер
        //адаптер для RecyclerView
        RecyclerViewMainAdapter adapter = new RecyclerViewMainAdapter(dataHomes);
        //получаем слушатель щелчков на элементах списка
        RecyclerViewMainAdapter.OnMainListClickListener listListener = getOnMainListClickListListener();
        //устанавливаем слушатель в адаптер
        adapter.setOnMainListClickListener(listListener);
        //устанавливаем GridLayoutManager для RecyclerView
        recyclerView.setLayoutManager(layoutManager);
        //устанавливаем адаптер для RecyclerView
        recyclerView.setAdapter(adapter);
    }

    //метод для получения слушателя щелчков на элементах списка
    private RecyclerViewMainAdapter.OnMainListClickListener getOnMainListClickListListener() {
        return new RecyclerViewMainAdapter.OnMainListClickListener() {
            @Override
            public void onMainListClick(int position) {
                NavController navController =
                        Navigation.findNavController(Objects.requireNonNull(getActivity()),
                                R.id.nav_host_fragment);
                switch (position){
                    case 0:
                        navController.navigate(R.id.action_nav_home_to_nav_secundomer);
                        break;
                    case 1:
                        navController.navigate(R.id.action_nav_home_to_nav_tempoleader);
                        break;
                    case 2:
                        navController.navigate(R.id.action_nav_home_to_nav_rascladki);
                        break;
                }
            }
        };
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home_menu, menu);
    }

    //переопределяем метод для работы с navController -для этого в navigation и меню должны
    //быть одинаковые id пункта назначения и id пункта меню
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

}
