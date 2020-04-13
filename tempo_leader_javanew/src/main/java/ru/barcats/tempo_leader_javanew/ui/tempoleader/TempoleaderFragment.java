package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

import static android.content.Context.MODE_PRIVATE;

public class TempoleaderFragment extends Fragment {

    public static final String TAG ="33333";
    private TempoleaderViewModel slideshowViewModel;
    private SQLiteDatabase database;

    private Button mStartButton;
    private Button mStopButton;
    private Button mResetButton;
    private Button mDelayButton;

    private ProgressBar mProgressBarTime;
    private TextView mCurrentTime;
    private TextView mCurrentReps;
    private TextView mTextViewDelay;
    private TextView mTextViewRest;
    private TextView mTimeLabel;
    private TextView mRepsLabel;
    private TextView mtextViewCountDown;
    private TextView mNameOfFile;

   // private Timer mTimer;
    private Timer mTimerRest;
    //private TimerTask mTimerTask;
    private ToneGenerator mToneGenerator;

    private float countMilliSecond =1000; //колич миллисекунд, получаемое из mEditTextTime
    private float countMillisDelay =5000; //колич миллисекунд, получаемое из mEditTextDelay
    private float mTimeOfSet = 0;   //общее время выполнения подхода в секундах
    private int countReps = 2; //количество повторений,получаемое из текста mEditTextReps

    public final static long mKvant = 100;//время в мс между срабатываниями TimerTask
    private long mTotalKvant = 0;//текущее суммарное время для фрагмента подхода
    private long mTotalTime = 0; //текущее суммарное время для подхода
    private long mCurrentDelay = 0; //текущее время для задержки

    private long mTimeRestStart = 0; //начальное время отдыха
    private long mTimeRestCurrent = 0; //текущее время отдыха

    private int mCurrentRep = 0; //счётчик повторений
    private int mTotalReps = 0;  //общее количество повторений в подходе
    private int mCurrentTotalReps = 0; //суммарное текущее количество повторений
    private int mCountFragment = 0;  //номер фрагмента подхода
    private int mTotalCountFragment = 0;  //количество фрагментов в подходе

    private boolean workOn = false; //признак начала работы
    private boolean restOn = false; //признак начала отдыха
    private boolean end = false; //признак окончания подхода
    public static boolean start = false;//признак нажатия на старт: public static для доступа из фрагмента

    int mCountFragmentLast = 0;
    //код -откуда пришли данные 111 --Main, 222-TimeMeterActivity, 333-ListOfFilesActivity
    //444 -DetailActivity  555 - NewExerciseActivity
    int fromActivity;

    int accurancy; //точность отсечек - количество знаков после запятой - от MainActivity
    boolean sound = true; // включение / выключение звука
    private SharedPreferences prefSetting;// предпочтения из PrefActivity
    private SharedPreferences shp; //предпочтения для записи задержки общей для всех раскладок
    private int  timeOfDelay = 0; //задержка в секундах
    //имя файла с раскладкой
    private String finishFileName;
    //id файла, загруженного в темполидер
    long fileId;
    //количество фрагментов подхода
    int countOfSet ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tempoleader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "TempoleaderFragment onViewCreated.onClick");

        database = new TempDBHelper(getActivity()).getWritableDatabase();
        //разрешить только портретную ориентацию экрана
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //находим вьюхи
        initViews(view);

        //обработка нажатия на кнопку Задержка
        pressDelayButton();
        //обработка нажатия на кнопку Старт
        pressStartButton(view);
        //обработка нажатия на кнопку Стоп
        onStopButton();
        //обработка нажатия на кнопку Сброс
        onResetButton();
        //выставляем доступность кнопок
        buttonsEnable (true,false,false);

        //************** Получение интента/ аргументов  с данными *************
        // когда грузим с главного экрана или со шторки, аргументы = null
       //TODO заменить на finishFileName
        if (getArguments() != null) {
            Log.d(TAG, "** TempoleaderFragment onViewCreated " +
                    " NAME_OF_FILE = " + getArguments().getString(P.NAME_OF_FILE) +
                    " FROM_ACTIVITY = " +  getArguments().getInt(P.FROM_ACTIVITY));
            //получаем NAME_OF_FILE из аргументов
            finishFileName = getArguments().getString(P.NAME_OF_FILE,P.FILENAME_OTSECHKI_SEC);
            //считываем значение FROM_ACTIVITY из интента
            //код -откуда пришли данные 111 --Main, 222-TimeMeterActivity, 333-ListOfFilesActivity
            //444 -DetailActivity  555 - NewExerciseActivity
            fromActivity =getArguments().getInt(P.FROM_ACTIVITY,111);
        }else {
            //получаем имя последнего файла темполидера из преференсис (запись в onDestroy )
            shp = getActivity().getPreferences(MODE_PRIVATE);
            //грузим последний файл темполидера  или автосохранение секундомера
            finishFileName = shp.getString(P.KEY_FILENAME,P.FILENAME_OTSECHKI_SEC);
        }
        Log.d(TAG, " @@@TempoleaderFragment fromActivity =  " +
                fromActivity +" mNameOfFile = " + finishFileName);
        //если интент пришел от TimeGrafActivity, он принёс с собой  отсечеки в файле
       if (fromActivity == 222){
            //TODO раскоментировать
//            Log.d(TAG, " SingleFragmentActivity fromActivity =  " + fromActivity);
//            //получаем имя файла из интента
//            finishFileName = intent.getStringExtra(P.FINISH_FILE_NAME);
//            //finishFileName=null, если не было записи в преференсис- тогда присваиваем имя
//            //единственной записи в базе, сделанной в onCreate MainActivity
//            if (finishFileName==null){
//                finishFileName = P.FILENAME_OTSECHKI_SEC;
//            }
            //если интент пришёл от NewExerciseActivity
        } else if(fromActivity == 555) {
            //TODO раскоментировать
//            //получаем имя файла из интента
//            finishFileName = intent.getStringExtra(P.FINISH_FILE_NAME);
//            timeOfDelay = TabFile.getFileDelayFromTabFile(database, finishFileName);
//            mDelayButton.setText(String.valueOf(timeOfDelay));
        }else Log.d(TAG, " intentTransfer = null ");

        //получаем id файла
        fileId = TabFile.getIdFromFileName(database, finishFileName);
        //количество фрагментов подхода
        countOfSet =TabSet.getSetFragmentsCount(database, fileId);
        Log.d(TAG, " getSetFragmentsCount =  " + countOfSet);

        slideshowViewModel =
                ViewModelProviders.of(this).get(TempoleaderViewModel.class);

        slideshowViewModel.loadDataSet(finishFileName)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<DataSet>>() {
            @Override
            public void onChanged(ArrayList<DataSet> dataSets) {
                //TODO
                Log.d(TAG, " /*/ dataSets size =  " + dataSets.size());
                //показываем список на экране
                showSetList(view, dataSets);
            }
        });
       }

    private void initViews(@NonNull View view) {
        //текстовая метка  для названия файла
        mNameOfFile = view.findViewById(R.id.textViewName);
        //текстовая метка Задержка, сек
        mTextViewDelay = view.findViewById(R.id.textViewDelay);
        //Текстовая метка До старта, сек и Время отдыха, сек в зависимости от состояния
        mTextViewRest = view.findViewById(R.id.textViewRest);
        mTextViewRest.setText(R.string.textViewTimeRemain);

        mDelayButton = view.findViewById(R.id.buttonDelay);
        shp = getActivity().getPreferences(MODE_PRIVATE);
        timeOfDelay = shp.getInt(P.KEY_DELAY, 6);

        mProgressBarTime = view.findViewById(R.id.progressBarTime);
        //mProgressBarTime.setBackgroundColor();

        mCurrentTime = view.findViewById(R.id.current_time);
        //для mCurrentReps в макете стоит запрет на выключение экрана android:keepScreenOn="true"
        mCurrentReps = view.findViewById(R.id.current_reps);
        mTimeLabel = view.findViewById(R.id.time_item1_label);
        mRepsLabel = view.findViewById(R.id.reps_item1_label);

        //счётчик времени задержки и времени отдыха
        mtextViewCountDown = view.findViewById(R.id.textViewCountDown);
        mtextViewCountDown.setText(String.valueOf(timeOfDelay));

        mStartButton = view.findViewById(R.id.start_button);
        mStopButton = view.findViewById(R.id.stop_button);
        mResetButton = view.findViewById(R.id.reset_button);
        mDelayButton.setText(String.valueOf(timeOfDelay));
    }

    private void pressDelayButton() {
        mDelayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
//                String delay = mDelayButton.getText().toString();
//                DialogSetDelay dialogFragment = DialogSetDelay.newInstance(delay);
//                dialogFragment.show(getSupportFragmentManager(),"delayDialog");
            }
        });
    }

    private void onResetButton() {
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pressed ResetButton");
                //TODO if (mTimer!=null)mTimer.cancel()
                //if (mTimer!=null)mTimer.cancel();
                mCurrentRep = 0;
                mCurrentTotalReps = 0;
                mTotalKvant = 0;
                mCurrentDelay = 0;  //устанавливаем текущее время задержки в 0
                mTotalTime = 0;
                mTotalReps = 0;
                mCountFragment = 0;
                mCurrentReps.setText("");
                mCurrentTime.setText("");
                mProgressBarTime.setProgress(100);
                //mProgressBarTotal.setProgress(100);
                //выставляем доступность кнопок
                buttonsEnable (true,false,false);
                mtextViewCountDown.setText("");
                //признак начала работы
                workOn = false;
                //признак окончания подхода в Нет
                end = false;
                restOn = false; //признак начала отдыха
                mTimeRestCurrent = 0; //текущее время отдыха
                mTextViewDelay.setText(R.string.textViewDelay); //задержка,сек
                mTextViewRest.setText(R.string.textViewTimeRemain); //До старта, сек
                mtextViewCountDown.setTextColor(Color.RED);
                mtextViewCountDown.setText(String.valueOf(timeOfDelay));// величина задержки из поля timeOfDelay

                //устанавливаем цвет маркера фрагмента подхода в исходный цвет, обновляя адаптер
               // changeMarkColor(R.id.fragment_container, mCountFragment, end);
                start = false;
                //вызываем onPrepareOptionsMenu чтобы открыть элементы тулбара
                //getActivity().invalidateOptionsMenu();
            }
        });
    }

    private void onStopButton() {
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pressed StopButton");
                //TODO if (mTimer!=null)mTimer.cancel()
                //if (mTimer!=null)mTimer.cancel();
                //выставляем доступность кнопок
                buttonsEnable (true,false,true);
                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 500);
                //выставляем флаг нажатия на Старт = нет
                start = false;
                restOn = true; //признак начала отдыха
                mTimeRestCurrent = 0; //обнуляем текущее время отдыха
                //фиксируем момент начала отдыха
                mTimeRestStart = System.currentTimeMillis();
                //делаем имя файла доступным для щелчка
                // mNameLayout.setEnabled(true);
                //делаем изменение задержки доступным
                mDelayButton.setEnabled(true);
                mTextViewRest.setText(R.string.textViewRestTime);  //Время отдыха, сек

                //вызываем onPrepareOptionsMenu чтобы открыть элементы тулбара если стоп
                //invalidateOptionsMenu();
            }
        });
    }

    private void pressStartButton(@NonNull View view) {

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Log.d(TAG, " mStartButton.onClick");
               // if (mTimer!=null)mTimer.cancel();
               // mTimer =new Timer();
                //mTimerTask = new MyTimerTask();
                mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC,100);

                //Выполняем начальные установки параметров, которые могли измениться
                mCurrentRep = 0;  //устанавливаем счётчик повторений во фрагменте в 0
                mCurrentTotalReps = 0; //устанавливаем счётчик выполненных в подходе повторений в 0
                mCurrentDelay = 0;  //устанавливаем текущее время задержки в 0
                mTotalKvant = 0;  //устанавливаем текущее время между повторениями в 0
                mTotalTime = 0; //обнуляем счётчик суммарного времени
                mCountFragment = 0; //обнуляем счётчик фрагментов подхода
                mProgressBarTime.setProgress(100);

                //Получаем время задержки в мс
                countMillisDelay = timeOfDelay*1000;

                // рассчитываем время между повторениями и количество повторений
                //   для первого фрагмента подхода до начала работы таймера

                //получаем время между повторениями mCountFragment = 0 фрагмента подхода
                countMilliSecond = TabSet.
                        getTimeOfRepInPosition(database, fileId, mCountFragment)*1000;
                Log.d(TAG, "Время между повторениями = " + countMilliSecond);

                //получаем количество повторений для mCountFragment = 0 фрагмента подхода
                countReps = TabSet.getRepsInPosition(database, fileId, mCountFragment);
                Log.d(TAG, "Количество повторений во фрагменте подхода = " + countReps);

                //получаем количество фрагментов в выполняемом подходе. Если было удаление или добавление
                //фрагмента подхода, нужно пересчитывать каждый раз
                mTotalCountFragment = TabSet.getSetFragmentsCount(database, fileId);

                //Покажем таймер задержки
                mtextViewCountDown.setText(String.valueOf(timeOfDelay));

                //Выставляем флаг "работа"
                workOn = false;

                //Выставляем флаг конец работы - нет
                end = false;

                restOn = false; //признак начала отдыха
                mTimeRestCurrent = 0; //текущее время отдыха

                //играем мелодию начала подхода  с задержкой
                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 200);

                //TODO запускаем TimerTask на выполнение с периодом mKvant
                //запускаем TimerTask на выполнение с периодом mKvant
                //mTimer.scheduleAtFixedRate(mTimerTask, mKvant,mKvant);

                //выставляем доступность кнопок
                buttonsEnable (false,true,false);
                Log.d(TAG, "countMilliSecond = " + countMilliSecond +"  countReps = " + countReps);
                //выставляем флаг нажатия на Старт = да
                start = true;

                //делаем изменение задержки недоступным
                mDelayButton.setEnabled(false);

                mTextViewDelay.setText(R.string.textViewDelay); //Задержка, сек
                mTextViewRest.setText(R.string.textViewTimeRemain); //До старта, сек

                //вызываем onPrepareOptionsMenu чтобы скрыть элементы тулбара пока старт
                getActivity().invalidateOptionsMenu();
                //invalidateOptionsMenu();
            }
        });
    }

    private  void showSetList(View view, ArrayList<DataSet> dataSets){
           RecyclerView recyclerView = view.findViewById(R.id.recycler_tempoleader);
           LinearLayoutManager manager = new LinearLayoutManager(getActivity());
           recyclerView.setLayoutManager(manager);
           RecyclerViewTempoleaderAdapter adapter = new RecyclerViewTempoleaderAdapter( dataSets);
           recyclerView.setAdapter(adapter);
       }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "TempoleaderFragment  onResume ");

        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"TempoleaderFragment onResume accurancy = " + accurancy);
        //получаем из файла настроек наличие звука
        sound = prefSetting.getBoolean("cbSound",true);
        Log.d(TAG,"TempoleaderFragment onResume sound = " + sound);
        //включаем/выключаем звук в зависимости от состояния чекбокса в PrefActivity
        AudioManager audioManager;
        if(sound){
            audioManager =(AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }else{
            audioManager =    (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }

        //выводим имя файла на экран
        mNameOfFile.setText(finishFileName);

        //получаем id  файла с раскладкой по его имени finishFileName из интента
        fileId = TabFile.getIdFromFileName(database, finishFileName);

        Log.d(TAG, "fileId  = " + fileId);
        //получаем количество фрагментов в выполняемом подходе если было удаление или добавление
        //фрагмента подхода, нужно пересчитывать каждый раз - это по кнопке Старт
        mTotalCountFragment = TabSet.getSetFragmentsCount(database, fileId);

        //посчитаем общее врямя выполнения подхода в секундах
        mTimeOfSet = TabSet.getSumOfTimeSet(database, fileId);
        Log.d(TAG, "Суммарное время подхода  = " + mTimeOfSet);

        //посчитаем общее количество повторений в подходе
        mTotalReps = TabSet.getSumOfRepsSet(database, fileId);
        Log.d(TAG, "Суммарное количество повторений  = " + mTotalReps + " fileId = " + fileId);

        //покажем общее время подхода и общее число повторений в подходе
        showTotalValues(mTimeOfSet,mTotalReps, mKvant);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "TempoleaderFragment - onDestroy");
        //записываем последнее имя файла на экране в преференсис активности
        shp = getActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(P.KEY_FILENAME, finishFileName);
        edit.apply();

        database.close();
    }

    //покажем общее время подхода и общее число повторений в подходе
    private void showTotalValues(float timeOfSet,int totalReps, long kvant){

        float millisTime = timeOfSet*1000;
        //покажем суммарное время подхода
        int minut = ((int)millisTime/60000)%60;
        int second = ((int)millisTime/1000)%60;
        int decim = (int)((millisTime%1000)/kvant);

        // общее время подхода
        String timeTotal = String.format(
                Locale.getDefault(), "Время  %d:%02d.%d",minut,second,decim);
        // общее количество повторений в подходе
        String repsTotal = String.format(
                Locale.getDefault(), "Количество  %02d", totalReps);

        mTimeLabel.setText(timeTotal);
        mRepsLabel.setText(repsTotal);

        Log.d(TAG, "showTotalValues timeTotal = " + timeTotal +
                "   repsTotal = " + repsTotal);
    }

    //состояние кнопок управления темполидером
    private void buttonsEnable(boolean start,boolean stop,boolean reset){
        mStartButton.setEnabled(start);
        mStopButton.setEnabled(stop);
        mResetButton.setEnabled(reset);
    }

    }