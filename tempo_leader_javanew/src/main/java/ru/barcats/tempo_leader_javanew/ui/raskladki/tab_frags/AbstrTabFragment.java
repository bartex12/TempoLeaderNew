package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;

public abstract class AbstrTabFragment extends Fragment {

    public static final String TAG = "33333";
    private int positionItem;
    private SQLiteDatabase database;

    protected RecyclerView recyclerView;
    protected RecyclerViewTabAdapter adapter;
    public View view;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        database = new TempDBHelper(context).getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_raskladki, container, false);
        recyclerView = view.findViewById(R.id.recycler_rascladki);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "// AbstrTabFragment onStop // " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.close();
        Log.d(TAG, "// AbstrTabFragment onDestroy // " );
    }

    public void initRecycler(ArrayList<String> strings) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //получаем адаптер - абстрактный метод
        adapter =new RecyclerViewTabAdapter(strings);
        // получаем слушатель щелчков на списке смете работ  - абстрактный метод
        RecyclerViewTabAdapter.OnClickOnLineListener listener =
                getOnClickOnLineListener();
        adapter.setOnClickOnLineListener(listener);
        recyclerView.setAdapter(adapter);
    }

    public RecyclerViewTabAdapter getAdapter(){
        return adapter;
    }

    private RecyclerViewTabAdapter.OnClickOnLineListener getOnClickOnLineListener() {
        return new RecyclerViewTabAdapter.OnClickOnLineListener() {
            @Override
            public void onClickOnLineListener(String fileName) {
                //TODO у каждого фрагмента свой вариант действий если через диалог
                //передаём имя файла в пункт назначения - во фрагмент темполидера
                Bundle bundle = new Bundle();
                bundle.putString(P.NAME_OF_FILE, fileName);
                bundle.putInt(P.FROM_ACTIVITY, P.TAB_BAR_ACTIVITY);  //333 - TabBarActivity
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_rascladki_to_nav_tempoleader, bundle);
            }
        };
    }

}
