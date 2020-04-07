package ru.barcats.tempo_leader_javanew.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Objects;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.ui.main.RecyclerViewMainAdapter;
import ru.barcats.tempo_leader_javanew.model.DataHome;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;

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

        homeViewModel.getListMain().observe(getViewLifecycleOwner(), new Observer<ArrayList<DataHome>>() {
            @Override
            public void onChanged(ArrayList<DataHome> dataHomes) {
                showMainList(dataHomes);
            }
        });
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
}
