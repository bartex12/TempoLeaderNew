package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.google.android.material.snackbar.Snackbar;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.SectionsPagerAdapter;


public  class AbstrTabFragment extends Fragment implements SectionsPagerAdapter.onGetPositionListener {

    public static final String TAG = "33333";
    private View view;
    private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private RecyclerViewTabAdapter adapter;
    private String fileName;
    private ViewPager viewPager;
    private Dialog dialog;
    private TabViewModel viewModel;
    private String tabType;

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onGetPosition(int position) {
        Log.d(TAG, "//***// AbstrTabFragment position =" +position);
    }

//    @Override
//    public void onGetPosition(int position) {
//        switch (position){
////                    case 0:
////                    tabType = P.TYPE_TIMEMETER;
////                    break;
////                    case 1:
////                    tabType = P.TYPE_TEMPOLEADER;
////                    break;
////                    case 2:
////                        tabType = P.TYPE_LIKE;
////                        break;
//                }
//        Log.d(TAG, "//***// AbstrTabFragment tabType =" +tabType);
//    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "// AbstrTabFragment onAttach // " );
        database = new TempDBHelper(context).getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "// AbstrTabFragment onCreateView // " );
        view = inflater.inflate(R.layout.fragment_raskladki, container, false);
        recyclerView = view.findViewById(R.id.recycler_rascladki);
        //находим ViewPager - он нужен для обновления вкладок после перемещения файлов
        viewPager = container.findViewById(R.id.container_raskladki_activity);
        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// AbstrTabFragment onViewCreated // " );

        SectionsPagerAdapter pagerAdapter = (SectionsPagerAdapter)viewPager.getAdapter();
        int positionTab =  pagerAdapter.getPosition();
        Log.d(TAG, "// AbstrTabFragment onViewCreated positionTab = " + positionTab);
        int position = viewPager.getCurrentItem();
        Log.d(TAG, "//// AbstrTabFragment onViewCreated position = " + position);
        switch (position){
            case 0:
                tabType = P.TYPE_TIMEMETER;
                break;
            case 1:
                tabType = P.TYPE_TEMPOLEADER;
                break;
            case 2:
                tabType = P.TYPE_LIKE;
                break;
        }
        viewModel =
                ViewModelProviders.of(this).get(TabViewModel.class);
        viewModel.getRascladki(tabType)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
                    @Override
                    public void onChanged(ArrayList<String> strings) {
                        initRecyclerAdapter(strings);
                    }
                });
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

    //создаём контекстное меню для списка
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, P.CANCEL_ACTION, 50, "Отмена");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        handleMenuItemClick(item);
        return super.onContextItemSelected(item);
    }

    private void handleMenuItemClick(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case P.DELETE_ACTION_TEMP: {
                Log.d(TAG, "// AbstrTabFragment DELETE_ACTION_TEMP getFileName() = " + fileName);
                showDeleteDialog();
                adapter.notifyDataSetChanged();
                break;
            }
            case P.CHANGE_ACTION_TEMP: {
                Log.d(TAG, "// AbstrTabFragment CHANGE_ACTION_SEC fileName = " +
                        fileName + "viewPager.getCurrentItem() =" + viewPager.getCurrentItem());
                showChangeDialog();
                adapter.notifyDataSetChanged();
                break;
            }
            case P.MOVE_SEC_ACTION_TEMP: {
                Log.d(TAG, "// AbstrTabFragment MOVE_SEC_ACTION_TEMP getFileName() = " + fileName);
                //поручаем перемещение файла ViewModel
                viewModel.moveFromTo(fileName, tabType, P.TYPE_TIMEMETER);
                //обновляем список вкладки после перемещения файла
                adapter.notifyDataSetChanged();
                // обновляем вкладки после перемещения файла
                viewPager.getAdapter().notifyDataSetChanged(); //работает !
                break;
            }
            case P.MOVE_LIKE_ACTION_TEMP: {
                Log.d(TAG, "// AbstrTabFragment MOVE_LIKE_ACTION_TEMP gfileName = " + fileName);
                //поручаем перемещение файла ViewModel
                viewModel.moveFromTo(fileName,tabType, P.TYPE_LIKE);
                //обновляем список вкладки после перемещения файла
                adapter.notifyDataSetChanged();
                // обновляем вкладки после перемещения файла
                viewPager.getAdapter().notifyDataSetChanged(); //работает !
            }
            case P.CANCEL_ACTION: {
                break;
            }
        }
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
                //doDeleteAction(fileName);
                viewModel.loadDataDeleteItem(fileName, tabType);
            }
        });
        if (fileName.equals(P.FILENAME_OTSECHKI_SEC)){
            Snackbar.make(recyclerView, getResources().getString(R.string.system_file),
                    Snackbar.LENGTH_SHORT).setAnchorView(R.id.recycler_rascladki).show();
        }else {
            deleteDialog.show();
        }
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

        //String min = getDateAndTime(fileName);  //абстр класс
        String min = viewModel.getDateAndTime(fileName);
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
                long fileIdNew = TabFile.getIdFromFileName(database, newfileName);
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
                    //doChangeAction(fileName, newfileName); // абстр класс
                    //поручаем удаление файла ViewModel
                   viewModel.doChangeAction(fileName, newfileName, tabType);
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

        if (fileName.equals(P.FILENAME_OTSECHKI_SEC)){
            Snackbar.make(recyclerView, getResources().getString(R.string.system_file_change),
                    Snackbar.LENGTH_SHORT).setAnchorView(R.id.recycler_rascladki).show();
        }else {
            dialog.show();
        }
    }



}
