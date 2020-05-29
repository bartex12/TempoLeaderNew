package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;

import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_adapters.RecyclerViewTabAdapter;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag.TabBarLikeFragment;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag.TabBarSecFragment;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag.TabBarTempFragment;

public abstract class AbstrTabFragment extends Fragment {

    public static final String TAG = "33333";
    private View view;

    private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private RecyclerViewTabAdapter adapter;
    private String fileName;
    private ViewPager viewPager;
    private Dialog dialog;

    protected abstract void doDeleteAction(String fileName);
    protected abstract String getDateAndTime(String fileName);
    protected abstract void doChangeAction(String fileNameOld, String fileNameNew);
    protected abstract long getIdFromFileName(String fileName);
    protected RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    protected ViewPager getViewPager() {
        return viewPager;
    }

    public String getFileName() {
        return fileName;
    }

    protected RecyclerViewTabAdapter getAdapter(){
        return adapter;
    }

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

        int position;
       if(this instanceof TabBarSecFragment){
           position = 0;
       }else if(this instanceof TabBarTempFragment){
           position = 1;
       }else if(this instanceof TabBarLikeFragment){
           position = 2;
       }else {
           position = 0;
       }
        Log.d(TAG, "// AbstrTabFragment onCreateView position = " + position);
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
        deleteDialog.show();
    }

    //диалог изменения имени файла через AlertDialog.Builder - чтобы было плавающее окно
    //если делать через Navigation? окно будет просто фрагмент а не диалог
    //возможно нужно попробовать активность в диалоговом режиме
    protected void  showChangeDialog(){
        final AlertDialog.Builder changeDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View viewDialog = inflater.inflate(R.layout.fragment_dialog_chahge_name, null);

        final EditText name = viewDialog.findViewById(R.id.editTextNameOfFile);
        name.requestFocus();
        name.setInputType(InputType.TYPE_CLASS_TEXT);
        name.setText(fileName);  //пишем имя файла

        String min = getDateAndTime(fileName);  //абстр класс
        final EditText dateAndTime = viewDialog.findViewById(R.id.editTextDateAndTime);
        dateAndTime.setText(min);
        dateAndTime.setEnabled(false);

        final CheckBox date = viewDialog.findViewById(R.id.checkBoxDate);

        changeDialog.setView(viewDialog);
        changeDialog.setTitle("Изменить имя");
        changeDialog.setIcon(R.drawable.ic_wrap_text_black_24dp);

        Button saveButYes = viewDialog.findViewById(R.id.buttonSaveYesChangeName);
        saveButYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  newfileName = name.getText().toString();
                if(date.isChecked()){
                   newfileName = newfileName + "_" + P.setDateString();
                    Log.d(TAG, "**** DialogChangeFileName date.isChecked() Имя файла = " + newfileName);
                }
                //++++++++++++++++++   проверяем, есть ли такое имя   +++++++++++++//
                long fileIdNew =  getIdFromFileName(newfileName);  //абстрактный
                Log.d(TAG, "**** newfileName = " + newfileName + "  fileIdNew = " + fileIdNew);

                //если имя - пустая строка
                if (newfileName.trim().isEmpty()) {
                    Snackbar.make(viewDialog, "Введите непустое имя раскладки",
                            Snackbar.LENGTH_LONG).show();
                    //если такое имя уже есть в базе
                } else if (fileIdNew != -1) {
                    Snackbar.make(viewDialog, "Такое имя уже существует. Введите другое имя.",
                            Snackbar.LENGTH_LONG).show();
                    //если имя не повторяется, оно не пустое и не системный файл то
                } else {
                    //поручаем смену имени ViewModel
                    doChangeAction(fileName, newfileName); //абстрактный
                    dialog.dismiss();  //закрывает только диалог
                }
            }
        });
        Button saveButNo = viewDialog.findViewById(R.id.buttonSaveNoChangeName);
        saveButNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  //закрывает только диалог
            }
        });
        //если делать запрет на закрытие окна при щелчке за пределами окна,
        // то сначала билдер создаёт диалог
        dialog = changeDialog.create();
        //запрет на закрытие окна при щелчке за пределами окна
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
