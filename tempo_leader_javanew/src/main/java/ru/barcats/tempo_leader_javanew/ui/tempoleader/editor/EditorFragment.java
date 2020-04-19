package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.raskladki.adapters.RecyclerViewTabAdapter;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.RecyclerViewTempoleaderAdapter;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.TempoleaderFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditorFragment extends Fragment {

    private static final String TAG = "33333";

    private  TextView changeTemp_textViewName;

    private TextView timeTotal;
    private TextView repsTotal;
    private TextView deltaValue;

    private  Button changeTemp_buttonMinus5;
    private Button changeTemp_buttonMinus1;
    private ImageButton changeReps_imageButtonRevert;
    private  Button changeTemp_buttonPlus1;
    private Button changeTemp_buttonPlus5;

    private CheckBox mCheckBoxAll;
    private RadioGroup mRadioGroupTimeCount;
    private RadioButton mRadioButtonTime;
    private RadioButton mRadioButtonCount;
    private boolean redactTime = true;

    private float mTimeOfSet = 0;   //общее время выполнения подхода в секундах
    private int mTotalReps = 0;  //общее количество повторений в подходе

    private  int positionOfList = 0;
    private int pos;
    private int offset = 0;
    private float time = 0f; //размер изменений времени
    private int count = 0; //размер изменений количества
    private int countOfSet ;//количество фрагментов подхода
    private long fileId;


    private EditorViewModel editorViewModel;
    //private SQLiteDatabase database;
    private RecyclerView recyclerView;
    private RecyclerViewTempoleaderAdapter adapter;
    private String fileName;
    //private ViewPager viewPager;
   // private Dialog dialog;
    private SharedPreferences prefSetting;// предпочтения из PrefActivity
    private boolean sound = true; // включение / выключение звука
    private int accurancy; //точность отсечек - количество знаков после запятой - от MainActivity
    private SharedPreferences shp; //предпочтения для записи задержки общей для всех раскладок
    private SQLiteDatabase database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //получаем базу данных
        database = new TempDBHelper(getActivity()).getWritableDatabase();

        if (getArguments() != null) {
            fileName = getArguments().getString(P.NAME_OF_FILE, P.FILENAME_OTSECHKI_SEC);
            Log.d(TAG, "/+++/ EditorFragment onCreate fileName = " + fileName);
        }else {
            //получаем имя последнего файла темполидера из преференсис (запись в onDestroy )
            shp = getActivity().getPreferences(MODE_PRIVATE);
            //грузим последний файл темполидера  или автосохранение секундомера
            fileName = shp.getString(P.KEY_FILENAME,P.FILENAME_OTSECHKI_SEC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "/+++/  EditorFragment onCreateView" );

        View view = inflater.inflate(R.layout.fragment_editor, container, false);
        recyclerView = view.findViewById(R.id.recycler_editor);
        //находим ViewPager - он нужен для обновления вкладок после перемещения файлов
        //viewPager = container.findViewById(R.id.container_raskladki_activity);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "/+++/  EditorFragment onViewCreated" );

        //разрешить только портретную ориентацию экрана
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getPrefSettings();  //получаем значения точности и звека из настроек
        initViews(view);  //находим вьюхи

        editorViewModel =
              new ViewModelProvider(requireActivity()).get(EditorViewModel.class);
        editorViewModel.loadDataSet(fileName)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<DataSet>>() {
                    @Override
                    public void onChanged(ArrayList<DataSet> dataSets) {
                        showSetList(dataSets);
                    }
                });
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "TempoleaderFragment  onResume ");

        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"TimeMeterActivity accurancy = " + accurancy);

        //выводим список, суммарные время и количество, устанавливаем выделение цветом
        calculateAndShowTotalValues();

        //установка в нужную позицию списка
        //changeTemp_listView.setSelection(positionOfList);

        //объявляем о регистрации контекстного меню
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "EditorFragment - onDestroy");
        //записываем последнее имя файла на экране в преференсис активности
        shp = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(P.KEY_FILENAME, fileName);
        edit.apply();
        //database.close();
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
        if(redactTime){
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
        mTimeOfSet = TabSet.getSumOfTimeSet(database, fileId);
        Log.d(TAG, "Суммарное время подхода  = " + mTimeOfSet);

        //посчитаем общее количество повторений в подходе
        mTotalReps = TabSet.getSumOfRepsSet(database, fileId);
        Log.d(TAG, "Суммарное количество повторений  = " + mTotalReps);

        //покажем общее время подхода и общее число повторений в подходе

        timeTotal.setText(showTotalTime(mTimeOfSet, TempoleaderFragment.mKvant));
        repsTotal.setText(String.format(Locale.getDefault(), "Количество  %d", mTotalReps));
    }

    private  void showSetList(ArrayList<DataSet> dataSets){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerViewTempoleaderAdapter(dataSets, accurancy);
        recyclerView.setAdapter(adapter);
    }

    private void getPrefSettings() {
        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"/+++/ TempoleaderFragment getPrefSettings accurancy = " + accurancy);
        //получаем из файла настроек наличие звука
        sound = prefSetting.getBoolean("cbSound",true);
        Log.d(TAG,"/+++/ TempoleaderFragment getPrefSettings sound = " + sound);
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
            }else if (minut<60){
                time = String.format(Locale.getDefault(),"Время  %02d:%02d.%d",minut,second,decim);
            }
        }else {
            time = String.format(Locale.getDefault(),"Время  %d:%02d:%02d.%d",hour,minut,second,decim);
        }
        return time;
    }

}
