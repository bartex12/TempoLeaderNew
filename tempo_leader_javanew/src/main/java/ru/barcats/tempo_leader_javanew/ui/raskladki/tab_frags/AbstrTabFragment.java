package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;

public abstract class AbstrTabFragment extends Fragment {

    public static final String TAG = "33333";
    public View view;

    private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private RecyclerViewTabAdapter adapter;
    private String fileName;

    public ViewPager getViewPager() {
        return viewPager;
    }

    private ViewPager viewPager;

    public String getFileName() {
        return fileName;
    }

    public RecyclerViewTabAdapter getAdapter(){
        return adapter;
    }
    protected abstract void doDeleteAction(String fileName);

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        database = new TempDBHelper(context).getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_raskladki, container, false);
        recyclerView = view.findViewById(R.id.recycler_rascladki);
        //находим ViewPager - он нужен для обновления вкладок после перемещения файлов
        viewPager = container.findViewById(R.id.container_raskladki_activity);
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

    public void initRecyclerAdapter(ArrayList<String> strings) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //получаем адаптер - абстрактный метод
        adapter =new RecyclerViewTabAdapter(strings);
        // получаем слушатель щелчков на списке смете работ  - абстрактный метод
        RecyclerViewTabAdapter.OnClickOnLineListener listener =
                getOnClickOnLineListener();
        RecyclerViewTabAdapter.OnLongClickLikeListener likeListener =
                getOnLongClickLikeListener();
        adapter.setOnClickOnLineListener(listener);
        adapter.setOnLongClickLikeListenerr(likeListener);
        recyclerView.setAdapter(adapter);
    }

    private RecyclerViewTabAdapter.OnClickOnLineListener getOnClickOnLineListener() {
        return new RecyclerViewTabAdapter.OnClickOnLineListener() {
            @Override
            public void onClickOnLineListener(String fileName) {
                //передаём имя файла в пункт назначения - во фрагмент темполидера
                Bundle bundle = new Bundle();
                bundle.putString(P.NAME_OF_FILE, fileName);
                bundle.putInt(P.FROM_ACTIVITY, P.TAB_BAR_ACTIVITY);  //333 - TabBarActivity
                Navigation.findNavController(view)
                        .navigate(R.id.action_nav_rascladki_to_nav_tempoleader, bundle);
            }
        };
    }

    //слушатель долгих нажатий на списке адаптера для получения имени файла
    private RecyclerViewTabAdapter.OnLongClickLikeListener getOnLongClickLikeListener() {
        //устанавливаем слушатель долгих нажатий для передачи имени файла
        return new RecyclerViewTabAdapter.OnLongClickLikeListener() {
            @Override
            public void onLongClickLike(String nameItem) {
                fileName = nameItem;
                Log.d(TAG, "// onLongClickLike nameItem = " + nameItem );
            }
        };
    }

    protected void showDeleteDialog(){

        AlertDialog.Builder deleteDialog = new AlertDialog.Builder(getActivity());
        deleteDialog.setTitle("Удалить: Вы уверены?");
        deleteDialog.setPositiveButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //ничего не делаем
            }
        });
        deleteDialog.setNegativeButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //поручаем удаление файла ViewModel
                doDeleteAction(fileName);
            }
        });
        if (fileName.equals(P.FILENAME_OTSECHKI_SEC)){
            Toast.makeText(getActivity(), "Системный файл. Удаление запрещено.",
                    Toast.LENGTH_SHORT).show();
        }else {
            deleteDialog.show();
        }

    }
}
