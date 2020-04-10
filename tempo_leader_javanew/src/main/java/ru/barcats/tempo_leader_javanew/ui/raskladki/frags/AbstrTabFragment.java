package ru.barcats.tempo_leader_javanew.ui.raskladki.frags;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.ui.raskladki.frags.sec_frag.SecViewModel;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;

public abstract class AbstrTabFragment extends Fragment {

    public static final String TAG = "33333";
    private int positionItem;
    private SQLiteDatabase database;

    public RecyclerView recyclerView;
    public RecyclerViewTabAdapter adapter;

    public AbstrTabFragment() {
        // Required empty public constructor
    }

    //public abstract RecyclerViewTabAdapter getRecyclerViewTabAdapter(ArrayList<String> strings);
    public abstract RecyclerViewTabAdapter.OnClickOnLineListener getOnClickOnLineListener();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        database = new TempDBHelper(context).getWritableDatabase();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "// AbstrTabFragment onCreate // " );
        //получаем id файла из аргументов
        //positionItem = getArguments().getInt(P.ARG_NUMBER_ITEM, 0);
        //Log.d(TAG, "AbstrTabFragment onCreate  positionItem = " + positionItem );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_raskladki, container, false);
        recyclerView = view.findViewById(R.id.recycler_rascladki);
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

    public void initRecycler(View view, ArrayList<String> strings) {

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



}
