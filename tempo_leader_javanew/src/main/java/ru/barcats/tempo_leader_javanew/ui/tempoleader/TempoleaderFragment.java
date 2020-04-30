package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.SystemClock;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.adapters.RecyclerViewTempoleaderAdapter;


import static android.content.Context.MODE_PRIVATE;

public class TempoleaderFragment extends Fragment {

    public static final String TAG ="33333";
    private TempoleaderViewModel dataSetViewModel;
    private RecyclerViewTempoleaderAdapter adapter;

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

    private Timer mTimer;
    private TimerTask mTimerTask;
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

    private boolean delayOn = false; //признак начала задержки
    private boolean workOn = false; //признак начала работы
    private boolean restOn = false; //признак начала отдыха
    private boolean end = false; //признак окончания подхода
    private static boolean start = false;//признак нажатия на старт: public static для доступа из фрагмента

    //код -откуда пришли данные 111 --Main, 222-TimeMeterActivity, 333-ListOfFilesActivity
    //444 -DetailActivity  555 - NewExerciseActivity
    private int fromActivity;

    private int accurancy; //точность отсечек - количество знаков после запятой - от MainActivity
    boolean sound = true; // включение / выключение звука
    private SharedPreferences prefSetting;// предпочтения из PrefActivity
    private SharedPreferences shp; //предпочтения для записи задержки общей для всех раскладок
    private int  timeOfDelay = 6; //задержка в секундах
    //имя файла с раскладкой
    private String finishFileName;
    //id файла, загруженного в темполидер
    private long fileId;
    private RecyclerView recyclerView;
    private OnTransmitListener onTransmitListener;

    public interface OnTransmitListener{
        void onTransmit(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onTransmitListener = (OnTransmitListener)context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "// @@@ // TempoleaderFragment onCreate");
        //получаем  ViewModel для TempoleaderFragment
        dataSetViewModel =
                new ViewModelProvider(this).get("loadDataSet", TempoleaderViewModel.class);

        if (getArguments() != null) {
            //Log.d(TAG, " ### TempoleaderFragment onCreate getArguments = "  + getArguments());
            //************** Получение интента/ аргументов  с данными *************
            // когда грузим с главного экрана или со шторки, аргументы = null
            if ( getArguments().getString(P.NAME_OF_FILE) != null) {
                //получаем NAME_OF_FILE из аргументов
                finishFileName = getArguments().getString(P.NAME_OF_FILE,P.FILENAME_OTSECHKI_SEC);
                timeOfDelay = dataSetViewModel.getDelay(finishFileName);
            }
            if (getArguments().getInt(P.FROM_ACTIVITY)>0) {
                //считываем значение FROM_ACTIVITY из интента
                //код -откуда пришли данные 111 --Main, 222-TimeMeterActivity, 333-ListOfFilesActivity
                //444 -DetailActivity  555 - NewExerciseActivity 777 - DialogSetDelay
                fromActivity =getArguments().getInt(P.FROM_ACTIVITY,111);

                //если интент пришел от DialogSetDelay, он принёс с собой  задержку
                if (fromActivity == P.DIALOG_DELAY){
                    //если из диалога задержки, получаем задержку
                    timeOfDelay = getArguments().getInt(P.ARG_DELAY,6);
                    //записываем задержку в базу
                    dataSetViewModel.updateDelay(timeOfDelay,finishFileName);
                }
            }
        }else {
            //получаем имя последнего файла темполидера из преференсис (запись в onDestroy )
            shp = requireActivity().getPreferences(MODE_PRIVATE);
            //грузим последний файл темполидера  или автосохранение секундомера
            finishFileName = shp.getString(P.KEY_FILENAME,P.FILENAME_OTSECHKI_SEC);
            timeOfDelay = shp.getInt(P.KEY_DELAY, 6);
        }
        Log.d(TAG, "// @@@ // TempoleaderFragment fromActivity =  " +
                fromActivity +" mNameOfFile = " + finishFileName + " timeOfDelay = " + timeOfDelay);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tempoleader, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "// @@@ // TempoleaderFragment onViewCreated");

        //разрешить только портретную ориентацию экрана
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //получаем значения точности и звека из настроек
        getPrefSettings();
        //находим вьюхи
        initViews(view);
        //обработка нажатия на кнопку Задержка
        pressDelayButton(view);
        //обработка нажатия на кнопку Старт
        pressStartButton(view);
        //обработка нажатия на кнопку Стоп
        pressStopButton();
        //обработка нажатия на кнопку Сброс
        pressResetButton();
        //выставляем доступность кнопок
        buttonsEnable (true,false,false);

        //если файл с таким именем был удалён, то грузим автосохранение секундомера
        if (dataSetViewModel.getFragmentsCount(finishFileName)<=0){
            finishFileName = P.FILENAME_OTSECHKI_SEC;
        }
        // грузим данные и следим за их изменением, кроме того передаём имя файла в Main
        dataSetViewModel.loadDataSet(finishFileName)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<DataSet>>() {
            @Override
            public void onChanged(ArrayList<DataSet> dataSets) {
                Log.d(TAG, " // *** //TempoleaderFragment dataSets size =  " + dataSets.size());
                //показываем список на экране
                updateAdapter(view, dataSets);
                //передаём в MainActivity чтобы засунуть в Bundle
                onTransmitListener.onTransmit(finishFileName);
            }
        });

        dataSetViewModel.loadDelay(finishFileName).observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(TAG, " // *** //TempoleaderFragment delay =  " + integer);
            }
        });
       }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "// @@@ // TempoleaderFragment  onResume ");
        //включаем/выключаем звук в зависимости от состояния чекбокса в PrefActivity
        AudioManager audioManager;
        if(sound){
            audioManager =(AudioManager)requireActivity().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }else{
            audioManager =    (AudioManager)requireActivity().getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }

        //выводим имя файла на экран
        mNameOfFile.setText(finishFileName);

        //получаем id  файла с раскладкой по его имени finishFileName из интента
        fileId =  dataSetViewModel.getIdFromFileName(finishFileName);
        //fileId = TabFile.getIdFromFileName(database, finishFileName);
        Log.d(TAG, "fileId  = " + fileId);
        //получаем количество фрагментов в выполняемом подходе если было удаление или добавление
        //фрагмента подхода, нужно пересчитывать каждый раз - это по кнопке Старт
        mTotalCountFragment =dataSetViewModel.getFragmentsCount(finishFileName);

        //посчитаем общее врямя выполнения подхода в секундах
        mTimeOfSet =  dataSetViewModel.getSumOfTimes(finishFileName);
        Log.d(TAG, "Суммарное время подхода  = " + mTimeOfSet);

        //посчитаем общее количество повторений в подходе
        mTotalReps =  dataSetViewModel.getSumOfReps(finishFileName);
        Log.d(TAG, "Суммарное количество повторений  = " + mTotalReps);

        //покажем общее время подхода и общее число повторений в подходе
        showTotalValues(mTimeOfSet,mTotalReps, mKvant);

        setHasOptionsMenu(true);
        requireActivity().invalidateOptionsMenu();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "// @@@ // TempoleaderFragment - onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "// @@@ // TempoleaderFragment - onDestroy");
        //записываем последнее имя файла на экране в преференсис активности
        shp = requireActivity().getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(P.KEY_FILENAME, finishFileName);
        edit.putInt(P.KEY_DELAY, timeOfDelay);
        edit.apply();

        //отключаем таймер
        if (mTimer!=null)mTimer.cancel();
    }

    private void getPrefSettings() {
        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"TempoleaderFragment getPrefSettings accurancy = " + accurancy);
        //получаем из файла настроек наличие звука
        sound = prefSetting.getBoolean("cbSound",true);
        Log.d(TAG,"TempoleaderFragment getPrefSettings sound = " + sound);
    }

    private void initViews(@NonNull View view) {
        recyclerView =  view.findViewById(R.id.recycler_tempoleader);
        //текстовая метка  для названия файла
        mNameOfFile = view.findViewById(R.id.textViewName);
        //текстовая метка Задержка, сек
        mTextViewDelay = view.findViewById(R.id.textViewDelay);
        //Текстовая метка До старта, сек и Время отдыха, сек в зависимости от состояния
        mTextViewRest = view.findViewById(R.id.textViewRest);
        mTextViewRest.setText(R.string.textViewTimeRemain);

        mDelayButton = view.findViewById(R.id.buttonDelay);
        mDelayButton.setText(String.valueOf(timeOfDelay)); //через viewModel никак -ещё null

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

    }

    private void pressDelayButton(View view) {
        mDelayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //передаём задержку в диалог, имя файла гоняем туда и обратно
                NavController navController = Navigation.findNavController(view);
                Bundle bundle = new Bundle();
                bundle.putString(P.NAME_OF_FILE, finishFileName );
                bundle.putString(P.ARG_DELAY, mDelayButton.getText().toString() );
                navController.navigate(R.id.action_nav_tempoleader_to_dialogSetDelay, bundle);

            }
        });
    }

    private void pressStartButton(@NonNull View view) {

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, " mStartButton.onClick");
                if (mTimer!=null)mTimer.cancel();
                mTimer =new Timer();
                mTimerTask = new MyTimerTask();
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
                countMilliSecond =  dataSetViewModel
                        .getTimeOfRepInPosition(fileId, mCountFragment)*1000;
                Log.d(TAG, "Время между повторениями = " + countMilliSecond);

                //получаем количество повторений для mCountFragment = 0 фрагмента подхода
                countReps =  dataSetViewModel.getRepsInPosition(fileId, mCountFragment);
                Log.d(TAG, "Количество повторений во фрагменте подхода = " + countReps);

                //получаем количество фрагментов в выполняемом подходе. Если было удаление или добавление
                //фрагмента подхода, нужно пересчитывать каждый раз
                mTotalCountFragment =dataSetViewModel.getFragmentsCount(finishFileName);

                //Покажем таймер задержки
                mtextViewCountDown.setText(String.valueOf(timeOfDelay));

                delayOn = true;  //Выставляем флаг "задержка"
                workOn = false; //Выставляем флаг "работа"
                restOn = false; //признак начала отдыха
                end = false;  //Выставляем флаг конец работы - нет

                mTimeRestCurrent = 0; //текущее время отдыха
                //играем мелодию начала подхода  с задержкой
                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 200);

                //запускаем TimerTask на выполнение с периодом mKvant
                mTimer.scheduleAtFixedRate(mTimerTask, mKvant,mKvant);

                //выставляем доступность кнопок
                buttonsEnable (false,true,false);
                Log.d(TAG, "countMilliSecond = " + countMilliSecond +"  countReps = " + countReps);
                //выставляем флаг нажатия на Старт = да
                start = true;

                //делаем изменение задержки недоступным
                mDelayButton.setEnabled(false);

                mTextViewDelay.setText(R.string.textViewDelay); //Задержка, сек
                mTextViewRest.setText(R.string.textViewTimeRemain); //До старта, сек

            }
        });
    }

    private void pressStopButton() {
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pressed StopButton");
                if (mTimer!=null)mTimer.cancel();
                //выставляем доступность кнопок
                buttonsEnable (true,false,true);
                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 500);

                mTimeRestCurrent = 0; //обнуляем текущее время отдыха
                //фиксируем момент начала отдыха
                mTimeRestStart = System.currentTimeMillis();
                //делаем изменение задержки доступным
                mDelayButton.setEnabled(true);

                if (delayOn &&(!workOn)){
                    mTextViewRest.setText(R.string.textViewTimeRemain); //До старта, сек
                }else if(workOn&&(!delayOn)) {
                    Log.d(TAG, "Pressed StopButton workOn = " + workOn);
                    mTextViewRest.setText(""); //До старта, сек
                    mtextViewCountDown.setTextColor(Color.BLUE);
                    mtextViewCountDown.setText(getResources().getString(R.string.stop));
                }else {
                    mTextViewRest.setText(R.string.textViewRestTime);  //Время отдыха, сек
                }

                Log.d(TAG, "Pressed StopButton workOn = " + workOn);

                delayOn = false; //Выставляем флаг "задержка"
                start = false;  //выставляем флаг нажатия на Старт = нет
                workOn = false; //Выставляем флаг "работа"
                restOn = true; //признак начала отдыха
            }
        });
    }

    private void  pressResetButton() {
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Pressed ResetButton");

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
                //выставляем доступность кнопок
                buttonsEnable (true,false,false);
                mtextViewCountDown.setText("");

                delayOn = false; //Выставляем флаг "задержка"
                workOn = false;  //признак начала работы
                restOn = false; //признак начала отдыха
                end = false;  //признак окончания подхода в Нет
                start = false;

                mTimeRestCurrent = 0; //текущее время отдыха
                mTextViewDelay.setText(R.string.textViewDelay); //задержка,сек
                mTextViewRest.setText(R.string.textViewTimeRemain); //До старта, сек
                mtextViewCountDown.setTextColor(Color.RED);
                mtextViewCountDown.setText(String.valueOf(timeOfDelay));// величина задержки из поля timeOfDelay

                //устанавливаем цвет маркера фрагмента подхода в исходный цвет, обновляя адаптер
                adapter.setItem(mCountFragment);
                recyclerView.scrollToPosition(mCountFragment);
                adapter.notifyDataSetChanged();
                if (mTimer!=null)mTimer.cancel();
            }
        });
    }

    private  void updateAdapter(View view, ArrayList<DataSet> dataSets){
            Log.d(TAG, " /*/TempoleaderFragment  showSetList dataSets size =  " + dataSets.size());
           RecyclerView recyclerView = view.findViewById(R.id.recycler_tempoleader);
           LinearLayoutManager manager = new LinearLayoutManager(getActivity());
           recyclerView.setLayoutManager(manager);
            adapter = new RecyclerViewTempoleaderAdapter(dataSets, accurancy);
           //получаем слушатель щелчков на элементах списка
           RecyclerViewTempoleaderAdapter.OnSetListClickListener listListener =
                getOnSetListClickListener();
           //устанавливаем слушатель в адаптер
           //adapter.setOnSetListClickListener(listListener);
           recyclerView.setAdapter(adapter);
       }

    //метод для получения слушателя щелчков на элементах списка
    private RecyclerViewTempoleaderAdapter.OnSetListClickListener getOnSetListClickListener() {
        return new RecyclerViewTempoleaderAdapter.OnSetListClickListener() {
            @Override
            public void onSetListClick(int position) {
                //adapter.setPosItem(position);
               // adapter.notifyDataSetChanged();
                //TODO
                Log.d(TAG,"/+++/ TempoleaderFragment Adapter position = " + position);
            }
        };
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

    //======================class MyTimerTask=================================//

    public class MyTimerTask extends TimerTask{
        @Override
        public void run() {  //запускаем MyTimerTask в методе run()

            //фиксируем изменения на экране (в пользовательском потоке)
            doChangeOnViThread();

            mCurrentDelay += mKvant;

            //если не отдых
            if (!restOn){
                if ((mCurrentDelay<=countMillisDelay-500)&&(mCurrentDelay>countMillisDelay-600)){
                    //играем мелодию начала подхода
                    mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 150);
                    SystemClock.sleep(250);
                    mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 300);
                }
                if (mCurrentDelay>=countMillisDelay){
                    workOn = true;
                    delayOn = false;
                }
                // если отдых
            }else {
                mTimeRestCurrent = System.currentTimeMillis() - mTimeRestStart;
                //фиксируем изменения на экране (в пользовательском потоке)
                doChangeOnViThread();
            }

            if (workOn&&!restOn) {
                mTotalKvant += mKvant;  // добавляем 100мс пока не будет больше времени между повторами
                mTotalTime += mKvant;  //добавляем 100мс к текущему времени подхода
                if (mTotalKvant >= countMilliSecond) {
                    mCurrentRep++; // если стало больше, переходим к следующему повтору
                    Log.d(TAG, "mCurrentRep = " + mCurrentRep);
                    mCurrentTotalReps++; //считаем количество выполненных повторений
                    mTotalKvant = 0;  //при этом обнуляя текущее время между повторами
                    mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 200);
                }//переходим к следующему фрагменту
                if ((mCurrentRep >= countReps) && (mCountFragment < mTotalCountFragment - 1)) {
                    mCountFragment++;
                    countMilliSecond =  dataSetViewModel
                            .getTimeOfRepInPosition(fileId, mCountFragment)*1000;
                    countReps = dataSetViewModel.getRepsInPosition(fileId, mCountFragment);
                    //countReps = TabSet.getRepsInPosition(database, fileId, mCountFragment);
                    Log.d(TAG, "countMilliSecond = " + countMilliSecond + "  countReps = " + countReps);
                    mTotalKvant = 0;
                    mCurrentRep = 0;
                }
                //если в последнем фрагменте - прекращаем выполнение
                if ((mCurrentRep >= countReps) && (mCountFragment >= mTotalCountFragment - 1)) {
                    //if (mTimer != null) mTimer.cancel();
                    Log.d(TAG, "mTimer.cancel");
                    mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 500);
                    //выставляем начальные значения - для повторного старта
                    mProgressBarTime.setProgress(0);
                    //mProgressBarTotal.setProgress(0);
                    mCurrentRep = 0;
                    mTotalKvant = 0;
                    mTotalReps = 0;
                    mCountFragment ++; // чтобы в одаптере маркер фрагментов подхода правильно показывал
                    end = true;
                    start = false; //это для разблокировки кнопки BACK
                    workOn = false;  //признак начала работы
                    restOn = true; //признак начала отдыха
                    //фиксируем момент начала отдыха
                    mTimeRestStart = System.currentTimeMillis();

                    //изменяем свойство кнопок и сбрасываем цвет, восстанавливаем меню
                    // в пользовательском потоке
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //чтобы не оставался последний фрагмент подхода со старым цветом
                            adapter.setItem(mCountFragment); // для маркера фрагментов подхода
                            recyclerView.scrollToPosition(mCountFragment); //переход в позицию списка
                            adapter.notifyDataSetChanged(); //оповещаем об изменениях

                            buttonsEnable(true, false, true);
                            mDelayButton.setEnabled(true);
                        }
                    });
                }
            }
        }
    }

    // показать изменения в пользовательском потоке
    private void doChangeOnViThread(){

    try{
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String time = showFormatString(mTotalTime, mKvant);
                //показ текущ времени
                mCurrentTime.setText(time);
                //показ текущ количества повторений
                mCurrentReps.setText(Integer.toString(mCurrentTotalReps));
                //показываем текущую задержку
                float f =(countMillisDelay - mCurrentDelay)/1000;
                if (f>=0) {
                    if (!restOn){
                        mTextViewRest.setText(R.string.textViewTimeRemain); //До старта, сек
                        mtextViewCountDown.setTextColor(Color.RED);
                        mtextViewCountDown.setText(Float.toString((f)));
                    }else {
                        mTextViewRest.setText(R.string.textViewRestTime);  //Время отдыха, сек
                        String timeRest = showFormatString(mTimeRestCurrent, mKvant);
                        mtextViewCountDown.setTextColor(Color.BLUE);
                        mtextViewCountDown.setText(timeRest);
                    }
                }else{
                    //если работа а не отдых
                    if (workOn&&!restOn){
                        mTextViewRest.setText(""); //До старта, сек
                        mtextViewCountDown.setTextColor(Color.RED);
                        mtextViewCountDown.setText(getResources().getString(R.string.workOn));
                    }
                    //если отдых
                    if (restOn) {
                        mTextViewRest.setText(R.string.textViewRestTime);  //Время отдыха, сек
                        String timeRest = showFormatString(mTimeRestCurrent, mKvant);
                        mtextViewCountDown.setTextColor(Color.BLUE);
                        mtextViewCountDown.setText(timeRest);
                    }
                }
                //при переходе к следующему фрагменту подхода меняем цвет маркера, для чего
                //передаём в адаптер  mCountFragment и обновляем адаптер
                adapter.setItem(mCountFragment);
                recyclerView.scrollToPosition(mCountFragment);
                adapter.notifyDataSetChanged();

               //Показываем прогресс текущего времени фрагмента подхода
                if (countMilliSecond == 0){
                    mProgressBarTime.setProgress(100);
                }else {
                    String s = Float.toString(((float) mTotalKvant) * 100 / countMilliSecond);
                    float i = Float.parseFloat(s);
                    //прогресс текущ времени
                    mProgressBarTime.setProgress(100 - (int) i);
                }
            }
        });
        }catch (NullPointerException e){
        e.getStackTrace();
        }
    }

    private String showFormatString (long total, long kvant) {
        //формируем формат строки показа времени
        int minut = ((int)total/60000)%60;
        int second = ((int)total/1000)%60;
        int decim = (int)((total%1000)/kvant);
        int hour = (int)((total/3600000)%24);

        // общее время подхода
        String time = "";
        if (hour<1){
            if(minut<10) {
                time = String.format(Locale.ENGLISH,"%d:%02d.%d",minut, second, decim);
            }else {
                time = String.format(Locale.ENGLISH,"%02d:%02d.%d",minut,second,decim);
            }
        }else {
            time = String.format(Locale.ENGLISH,"%d:%02d:%02d.%d",hour,minut,second,decim);
        }
        return time;
    }


}