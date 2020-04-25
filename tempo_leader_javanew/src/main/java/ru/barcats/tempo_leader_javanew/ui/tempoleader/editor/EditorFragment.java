package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.adapters.RecyclerViewEditorAdapter;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.TempoleaderFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditorFragment extends Fragment {

    private static final String TAG = "33333";

    private TextView changeTemp_textViewName;
    private TextView timeTotal;
    private TextView repsTotal;
    private TextView deltaValue;

    private Button changeTemp_buttonMinus5;
    private Button changeTemp_buttonMinus1;
    private ImageButton changeReps_imageButtonRevert;
    private Button changeTemp_buttonPlus1;
    private Button changeTemp_buttonPlus5;

    private CheckBox mCheckBoxAll;
    private RadioGroup mRadioGroupTimeCount;
    private RadioButton mRadioButtonTime;
    private RadioButton mRadioButtonCount;

    private float mTimeOfSet = 0;   //общее время выполнения подхода в секундах
    private float time = 0f; //размер изменений времени

    private int mTotalReps = 0;  //общее количество повторений в подходе
    private int positionOfList = 0;  //позиция списка фрагментов подхода
    private int count = 0; //размер изменений количества
    private int accurancy; //точность отсечек - количество знаков после запятой - от MainActivity

    private EditorViewModel editorViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewEditorAdapter adapter;
    private SharedPreferences prefSetting;// предпочтения из PrefActivity

    private SharedPreferences shp; //предпочтения для записи задержки общей для всех раскладок
    private SQLiteDatabase database; //база данных
    private SaverFragmentListener mSaverFragmentListener;

    private boolean sound = true; // включение / выключение звука
    private boolean saveVision = false; //показывать иконку Сохранить true - да false - нет
    private boolean isEditTime = true; //радиокнопка редактировать время нажата?
    private boolean isCheckedAll =  false;

    private long fileIdCopy;  // id  копии файла

    private String fileName;  //имя файла


    //интерфейс для передачи в Main fileIdCopy для работы из меню тулбара
    public interface SaverFragmentListener{
        void onFileCopyTransmit(long fileIdCopy);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         mSaverFragmentListener = (SaverFragmentListener)context;
        Log.d(TAG, "DialogSaveTempFragment: onAttach   mSaverFragmentListener = " +
                mSaverFragmentListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //получаем базу данных
        database = new TempDBHelper(getActivity()).getWritableDatabase();

        if (getArguments() != null) {
            fileName = getArguments().getString(P.NAME_OF_FILE, P.FILENAME_OTSECHKI_SEC);
            Log.d(TAG, "/+++/ EditorFragment onCreate fileName = " + fileName);

//            if (getArguments().getInt(P.FROM_ACTIVITY)>0) {
//                //считываем значение FROM_ACTIVITY из интента
//                //код -откуда пришли данные 111 --Main, 222-TimeMeterActivity, 333-ListOfFilesActivity
//                //444 -DetailActivity  555 - NewExerciseActivity 777 - DialogSetDelay
//                //888  - DialogChangeTempFragment
//               int fromActivity = getArguments().getInt(P.FROM_ACTIVITY,111);
//                //если  от DialogChangeTempFragment, то   isEditTime и
//                if (fromActivity == P.DIALOG_CHANGE_TEMP){
////                    isEditTime = true;
////                    isCheckedAll = true;
////                    setMarkerColor(); //цвет маркера от состояния чекбокса
////                    Log.d(TAG, "/+++/ EditorFragment onCreate mCheckBoxAll = " +
////                            mCheckBoxAll.isChecked());
//                }
//            }
        }else {
            //получаем имя последнего файла темполидера из преференсис (запись в onDestroy )
            shp = getActivity().getPreferences(MODE_PRIVATE);
            //грузим последний файл темполидера  или автосохранение секундомера
            fileName = shp.getString(P.KEY_FILENAME,P.FILENAME_OTSECHKI_SEC);
        }
        //для фрагментов требуется так разрешить появление  меню
         setHasOptionsMenu(true);
        //но лучше так - меню же в Main
        //вызываем onPrepareOptionsMenu в Main для показа меню в тулбаре
        //requireActivity().invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "/+++/  EditorFragment onCreateView" );
        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        recyclerView = view.findViewById(R.id.recycler_editor);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "/+++/  EditorFragment onViewCreated" );

        //разрешить только портретную ориентацию экрана
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getPrefSettings();  //получаем значения точности и звука из настроек
        initViews(view);  //находим вьюхи

        setChackBoxListener(); //изменение чекбокса
        setRadioButtonListener();  // надписи на кнопкажх в зависимости от радиокнопки

        changeTempMinus5(); //нажатие кнопки -5
        changeTempMinus1(); //нажатие кнопки -1
        changeTempPlus1(); //нажатие кнопки +1
        changeTempPlus5(); //нажатие кнопки +5
        revertTemp();   //нажатие кнопки Откатить изменения

        editorViewModel =
              new ViewModelProvider(requireActivity()).get(EditorViewModel.class);
        editorViewModel.loadDataSet(fileName)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<DataSet>>() {
                    @Override
                    public void onChanged(ArrayList<DataSet> dataSets) {
                        updateAdapter(dataSets);
                        setMarkerColor(); //цвет маркера от состояния чекбокса
                        Log.d(TAG, " /+++/  dataSets getReps =  " + dataSets.get(0).getReps());
                    }
                });
        //создаём копию файла и получаем его id
        fileIdCopy =  editorViewModel.getCopyFile(fileName);
        //передаём в MainAct id копии файла
        mSaverFragmentListener.onFileCopyTransmit(fileIdCopy);
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "EditorFragment  onResume ");

        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"EditorFragment accurancy = " + accurancy);

        //выводим список, суммарные время и количество, устанавливаем выделение цветом
        calculateAndShowTotalValues();

        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "/+++/  -=-=-=-=-EditorFragment - onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "/+++/  -=-=-=-=-EditorFragment - onDestroy");
        //записываем последнее имя файла на экране в преференсис активности
        shp = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(P.KEY_FILENAME, fileName);
        edit.apply();

        //удаляем копию файла, созданную при редактировании
        editorViewModel.clearCopyFile(fileIdCopy,fileName);
        database.close();
    }

    private void revertTemp() {
        changeReps_imageButtonRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //отменяем внесённые изменения
                editorViewModel.revertEdit(fileName, fileIdCopy);
                //копия файла была использована, поэтому для дальнейшего редактирования
                //создаём ещё одну копию файла -  и получаем его id
                fileIdCopy =  editorViewModel.getCopyFile(fileName);
                setMarkerColor();
                calculateAndShowTotalValues();
                saveVision = false;
                //делаем индикатор невидимым
                deltaValue.setVisibility(View.INVISIBLE);
                //обнуляем показатели разности значений
                time = 0f;
                count = 0;
            }
        });
    }

    //слушатель на изменение ChackBox
    private void setChackBoxListener() {
        mCheckBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //загружаем данные
                editorViewModel.loadDataSet(fileName);
                setMarkerColor();
                calculateAndShowTotalValues();
                //делаем индикатор невидимым
                deltaValue.setVisibility(View.INVISIBLE);
                //обнуляем показатели разности значений
                time = 0f;
                count = 0;
            }
        });
    }

    private void setMarkerColor() {
        if (mCheckBoxAll.isChecked()) {
            adapter.setForAll(true);
            adapter.setEditTime(isEditTime);
        }else {
            adapter.setForAll(false);
            adapter.setEditTime(isEditTime);
            adapter.setPosItem(positionOfList);
            recyclerView.scrollToPosition(positionOfList);
        }
        adapter.notifyDataSetChanged();
    }

    private void changeTempPlus5() {
        changeTemp_buttonPlus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //пересчитываем раскладку на +1 процентов времени или +1 раз
                editorViewModel.edidAction(fileName, 1.05f, 5,
                        isEditTime, mCheckBoxAll.isChecked(), positionOfList);

                getDeltaValue(1.05f, 5);
                setMarkerColor();
                calculateAndShowTotalValues();
                // changeTemp_listView.setSelectionFromTop(pos, offset);
                saveVision = true;
            }
        });
    }

    private void changeTempPlus1() {
        changeTemp_buttonPlus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //пересчитываем раскладку на +1 процентов времени или +1 раз
                editorViewModel.edidAction(fileName, 1.01f, 1,
                        isEditTime, mCheckBoxAll.isChecked(), positionOfList);

                getDeltaValue(1.01f, 1);
                setMarkerColor();
                calculateAndShowTotalValues();
                // changeTemp_listView.setSelectionFromTop(pos, offset);
                saveVision = true;
            }
        });
    }

    private void changeTempMinus1() {
        changeTemp_buttonMinus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //пересчитываем раскладку на -5 процентов времени или -5 раз
                editorViewModel.edidAction(fileName, 0.99f, -1,
                        isEditTime, mCheckBoxAll.isChecked(), positionOfList);

               getDeltaValue(0.99f, -1);
                setMarkerColor();
                calculateAndShowTotalValues();
                // changeTemp_listView.setSelectionFromTop(pos, offset);
                saveVision = true;
            }
        });
    }

    private void changeTempMinus5() {
        changeTemp_buttonMinus5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //пересчитываем раскладку на -5 процентов времени или -5 раз
                editorViewModel.edidAction(fileName, 0.95f, -5,
                        isEditTime, mCheckBoxAll.isChecked(),positionOfList);

               getDeltaValue(0.95f, -5);
                setMarkerColor();
                calculateAndShowTotalValues();
               // changeTemp_listView.setSelectionFromTop(pos, offset);
                saveVision = true;
            }
        });
    }

    private void getDeltaValue(float deltaTime, int countReps) {
        String s;
        if (isEditTime) {
            time += (deltaTime - 1f) * 100;
            s = String.format(Locale.getDefault(), "%+3.0f", time);
            deltaValue.setVisibility(View.VISIBLE);
            String ss = s + "%";
            deltaValue.setText(ss);
        } else {
            count += countReps;
            s = String.format(Locale.getDefault(), "%+3d", count);
            deltaValue.setVisibility(View.INVISIBLE);
        }
    }

    private void setRadioButtonListener() {
        mRadioGroupTimeCount.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonTime:
                        isEditTime = true;
                        deltaValue.setVisibility(View.VISIBLE);
                        changeTemp_buttonMinus5.setText("-5%");
                        changeTemp_buttonMinus1.setText("-1%");
                        changeTemp_buttonPlus1.setText("+1%");
                        changeTemp_buttonPlus5.setText("+5%");
                        break;
                    case R.id.radioButtonCount:
                        isEditTime = false;
                        deltaValue.setVisibility(View.INVISIBLE);
                        changeTemp_buttonMinus5.setText("-5");
                        changeTemp_buttonMinus1.setText("-1");
                        changeTemp_buttonPlus1.setText("+1");
                        changeTemp_buttonPlus5.setText("+5");
                        break;
                }
//                adapter.setEditTime(isEditTime);
//                adapter.notifyDataSetChanged();
                setMarkerColor();
            }
        });
    }

    private void initViews(@NonNull View view) {
        deltaValue = view.findViewById(R.id.deltaValue);
        deltaValue.setVisibility(View.INVISIBLE);
        //deltaValue.setText("-00%");
        //deltaValue.setBackground(R.drawable.ramka);

        mRadioButtonTime = view.findViewById(R.id.radioButtonTime);
        mRadioButtonCount = view.findViewById(R.id.radioButtonCount);
        mRadioGroupTimeCount = view.findViewById(R.id.radioGroupTimeCount);

        mCheckBoxAll = view.findViewById(R.id.checkBox);
        mCheckBoxAll.setChecked(true);

        changeTemp_textViewName =view.findViewById(R.id.changeTemp_textViewName);
        changeTemp_textViewName.setText(fileName);

        timeTotal = view.findViewById(R.id.timeTotal);
        repsTotal =view.findViewById(R.id.repsTotal);

        changeTemp_buttonMinus5 = view.findViewById(R.id.changeTemp_buttonMinus5);
        changeTemp_buttonMinus1 = view.findViewById(R.id.changeTemp_buttonMinus1);
        changeReps_imageButtonRevert = view.findViewById(R.id.changeTemp_imageButtonRevert);
        changeTemp_buttonPlus1 = view.findViewById(R.id.changeTemp_buttonPlus1);
        changeTemp_buttonPlus5 = view.findViewById(R.id.changeTemp_buttonPlus5);

        //Выставляем надписи на кнопках перед началом редактирования
        if(isEditTime){
            changeTemp_buttonMinus5.setText("-5%");
            changeTemp_buttonMinus1.setText("-1%");
            changeTemp_buttonPlus1.setText("+1%");
            changeTemp_buttonPlus5.setText("+5%");
        }else {
            changeTemp_buttonMinus5.setText("-5");
            changeTemp_buttonMinus1.setText("-1");
            changeTemp_buttonPlus1.setText("+1");
            changeTemp_buttonPlus5.setText("+5");
        }
    }

    private void calculateAndShowTotalValues(){
        //посчитаем общее врямя выполнения подхода в секундах
        mTimeOfSet =editorViewModel.getSumOfTimes(fileName);
        Log.d(TAG, "Суммарное время подхода  = " + mTimeOfSet);

        //посчитаем общее количество повторений в подходе
        mTotalReps = editorViewModel.getSumOfReps(fileName);
        Log.d(TAG, "Суммарное количество повторений  = " + mTotalReps);

        //покажем общее время подхода и общее число повторений в подходе
        timeTotal.setText(showTotalTime(mTimeOfSet, TempoleaderFragment.mKvant));
        repsTotal.setText(String.format(Locale.getDefault(), "Количество  %d", mTotalReps));
    }

    private  void updateAdapter(ArrayList<DataSet> dataSets){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewEditorAdapter(dataSets, accurancy);
        //получаем слушатель щелчков на элементах списка
        RecyclerViewEditorAdapter.OnSetListClickListener listListener =
                getOnSetListClickListener();
        //устанавливаем слушатель в адаптер
        adapter.setOnSetListClickListener(listListener);
        recyclerView.setAdapter(adapter);
    }

    //метод для получения слушателя щелчков на элементах списка
    private RecyclerViewEditorAdapter.OnSetListClickListener getOnSetListClickListener() {
        return new RecyclerViewEditorAdapter.OnSetListClickListener() {
            @Override
            public void onSetListClick(int position) {
                positionOfList = position;
                adapter.setPosItem(position);
                adapter.notifyDataSetChanged();
                //TODO
                Log.d(TAG,"/+++/ EditorFragment Adapter position = " + position);
            }
        };
    }

    private void getPrefSettings() {
        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"/+++/EditorFragment getPrefSettings accurancy = " + accurancy);
        //получаем из файла настроек наличие звука
        sound = prefSetting.getBoolean("cbSound",true);
        Log.d(TAG,"/+++/ EditorFragment getPrefSettings sound = " + sound);
    }

    //покажем общее время подхода
    private String showTotalTime(float timeOfSet, long kvant){

        float millisTime = timeOfSet*1000;
        //покажем суммарное время подхода
        int minut = ((int)millisTime/60000)%60;
        int second = ((int)millisTime/1000)%60;
        int decim = (int)((millisTime%1000)/kvant);
        int hour = (int)((millisTime/3600000)%24);

        // общее время подхода
        String time = "";
        if (hour<1){
            if(minut<10) {
                time = String.format(Locale.getDefault(),"Время  %d:%02d.%d",minut, second, decim);
            }else {
                time = String.format(Locale.getDefault(),"Время  %02d:%02d.%d",minut,second,decim);
            }
        }else {
            time = String.format(Locale.getDefault(),"Время  %d:%02d:%02d.%d",hour,minut,second,decim);
        }
        return time;
    }

}
