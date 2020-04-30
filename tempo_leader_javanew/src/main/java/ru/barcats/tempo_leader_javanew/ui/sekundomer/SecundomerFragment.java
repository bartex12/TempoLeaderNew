package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

import static android.content.Context.MODE_PRIVATE;

public class SecundomerFragment extends Fragment implements DialogSaveSec.SaveSecListener {

    public static final String TAG ="33333";
    private Button mButtonStart;
    private Button mButtonStop;
    private Button mButtonReset;
    private Button mButtonNext;
    private TextView mCurrentTime;
    private TextView mCurrentTimePause;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private ToneGenerator mToneGenerator;
    private final long mKvant = 100;//время в мс между срабатываниями TimerTask
    private long mTotalTime = 0;//текущее суммарное время для фрагмента подхода
    private long mTimeLast = 0;   // время на предыдущей отсечке
    private long mTimeStart = 0;  //время в момент нажатия на Старт
    private long mTimeExersize = 0;  //время от старта
    private long mTimeNext = 0;   //время на последующей отсечке
    private long mDelta = 0;   //время между соседними отсечками
    private long mTimeStop = 0; //время в момент нажатия на стоп
    private long mTimeRestart = 0; //время в момент нажатия на Продолжить (Рестарт)
    private long mDeltaDelaySummary = 0; //Суммарное время приостановки секундомера
    private long mDeltaDelayCurrent = 0; //Текущее время приостановки секундомера
    private long mPauseTime = 0;  //Текущее время в паузе секундомера
    private boolean mRestart = false; //признак повторного старта (true)
    private boolean mIsStop  = false;  //признак нажатия на Стоп (Пауза)
    private boolean mIsStopped = false; //признак нахождения в режиме паузы
    private boolean start = false;//признак нажатия на старт
    private int ii = 0; //порядковый номер отсечки

    //Временный список отсечек по кругам для записи в базу, если будет нужно (в диалоге сохранения)
    private ArrayList<String> repTimeList = new ArrayList<>();

    private ArrayList<DataSecundomer> dataSec = new ArrayList<>();

    private int accurancy; //точность отсечек - количество знаков после запятой - от MainActivity
    private int pause;  //режим паузы 1-остановка времени 2- остановка индикации
    private boolean sound = true; // включение / выключение звука
    private SharedPreferences prefSetting;// предпочтения из PrefActivity
    private SharedPreferences prefNameOfLastFile;// предпочтения - имя последнего сохранённого файла

    private String finishNameFile;//имя файла - или из метода интерфейса или по умолчанию
    private RecyclerSecundomerAdapter adapter;
    private DataSecundomer dataSecundomer;
    private SecundomerViewModel secViewModel;
    private View root;
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
    public void onNameAndGrafTransmit(String nameFile, boolean showGraf, boolean cancel) {
        Log.d(TAG,"//##// SecundomerFragment onNameAndGrafTransmit");
        //showGraf -флаг : показывать график отсечек или нет
        //cancel - флаг : диалог отменён пользователем? true - да
        if (cancel){
            //если диалог был отменён, просто стираем список с отсечками
            repTimeList.clear();
            //в противном случае
        }else{
            //получаем дату и время в нужном для базы данных формате
            String dateFormat  = secViewModel.getDateString();
            String timeFormat  = secViewModel.getTimeString();
            //если строка имени пустая
            if (nameFile.isEmpty()) {
                //имя будет "Автосохранение секундомера"
                finishNameFile = P.FILENAME_OTSECHKI_SEC;
                //проверяем, есть ли в базе запись с таким именем FILENAME_OTSECHKI_SEC
                long repeatId =  secViewModel.getIdFromFileName(finishNameFile);
                Log.d(TAG,"onNameAndGrafTransmit repeatId = " + repeatId);
                //если есть (repeatId не равно -1), стираем её и потом пишем новые данные под таким именем
                if (repeatId != -1){
                    secViewModel.deleteFileAndSets(repeatId);
                }
            }else {
                //проверяем, есть ли в базе запись с именем nameFile< чтобы избежать дублирования
                long checkRepeatId = secViewModel.getIdFromFileName (nameFile);
                Log.d(TAG,"onNameAndGrafTransmit checkRepeatId = " + checkRepeatId);
                //если есть (repeatId не равно -1), добавляем к имени +
                if (checkRepeatId != -1){
                    finishNameFile = nameFile + R.string.LowMinus + secViewModel.getTimeString() ;
                }else {
                    finishNameFile = nameFile;
                }
            }
            Log.d(TAG, " После finishNameFile = " + finishNameFile);
            //======Начало добавления записей в таблицы DataFile и DataSet=========//
            //если имя файла не пустое или "Автосохранение секундомера"
            //создаём экземпляр класса DataFile в конструкторе
            DataFile file1 = new DataFile(finishNameFile, dateFormat, timeFormat,
                    null,null,P.TYPE_TIMEMETER, 6);
            //добавляем запись в таблицу TabFile, используя данные DataFile
            long file1_id = secViewModel.addFile(file1);

            //готовим данные фрагментов подхода
            // если индекс =0, то первое значение
            for (int j = 0; j<repTimeList.size(); j++ ) {
                float time_now;
                //получаем время в секундах между измерениями
                //если первое значение, то так
                if (j == 0){
                    time_now = (float) (Long.parseLong(repTimeList.get(j)) )/1000;
                    //если не первое значение, то как разницу
                }else{
                    time_now = (float) (Long.parseLong(repTimeList.get(j)) -
                            Long.parseLong(repTimeList.get(j - 1)))/1000;
                }
                //создаём экземпляр класса DataSet в конструкторе
                DataSet set = new DataSet(time_now,1,j+1);
                //добавляем запись в таблицу TabSet, используя данные DataSet
                secViewModel.addSet(set, file1_id);
                //TabSet.addSet(database, set, file1_id);
                //======Окончание добавления записей в таблицы DataFile и DataSet=========//
            }
            // Cохраняем имя файла в предпочтениях (ИСПОЛЬЗУЕМ  при переходе в график с тулбара)
            // получаем Editor
            SharedPreferences.Editor ed = prefNameOfLastFile.edit();
            //пишем имя последнего сохранённого файла в предпочтения
            ed.putString(P.LAST_FILE, finishNameFile);
            ed.apply();

            //если нужно записать и показать
            //вызываем интент, в котором передаём имя последнего файла в TimeGrafActivity
            if (showGraf){
                NavController controller =Navigation.findNavController(root);
                Bundle bundle = new Bundle();
                bundle.putString(P.NAME_OF_FILE, finishNameFile);
                controller.navigate(R.id.action_nav_secundomer_to_nav_grafic, bundle);
            }
            //стираем список с отсечками
            repTimeList.clear();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        secViewModel = new ViewModelProvider(requireActivity()).get(SecundomerViewModel.class);
        //получаем файл предпочтений
        prefNameOfLastFile= requireActivity().getPreferences(MODE_PRIVATE);
        //если ничего нет в аргументах, смотрим в  SharedPreferences,
        //если файл с таким именем был удалён, то грузим автосохранение секундомера
        if (getArguments() != null){
            finishNameFile = getArguments().getString(P.FINISH_FILE_NAME);
        }else {
            finishNameFile = prefNameOfLastFile.getString(P.LAST_FILE,P.FILENAME_OTSECHKI_SEC);
        }
        Log.d(TAG,"//##// SecundomerFragment onCreate имя = " + finishNameFile);

        //передаём имя файла в Main
        onTransmitListener.onTransmit(finishNameFile);
        //НЕ стирать = без этой строки меню тулбара пропадант,
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_secundomer, container, false);
        recyclerView = root.findViewById(R.id.recycler_secundomer);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //установить портретную ориентацию экрана
        requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initViews(view);
        setStartListener();
        setNextListener();
        setPauseListener();
        setStopListener();

        //состояние кнопок при запуске программы
        mButtonStart.setEnabled(true);
        mButtonStart.setVisibility(View.VISIBLE);
        mButtonStop.setEnabled(false);
        mButtonStop.setVisibility(View.GONE);
        mButtonReset.setEnabled(false);
        mButtonReset.setVisibility(View.GONE);
        mButtonNext.setEnabled(false);
        mButtonNext.setVisibility(View.GONE);

        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"//##// SecundomerFragment accurancy = " + accurancy);
    }

    private  void updateAdapter(ArrayList<DataSecundomer> dataSec){
        Log.d(TAG, "//##// SecundomerFragment updateAdapter dataSec.size =  " + dataSec.size());

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        adapter = new RecyclerSecundomerAdapter(dataSec);
        recyclerView.setAdapter(adapter);
    }

    private void setStopListener() {
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //если число отсечек не меньше 1, вызываем диалог записи в файл
                if (ii>0) {
                    //открываем диалог сохранения - проще так чем гонть Bundle туда -сюда
                    openSaveInFileDialogFragment();
                }else {
                    // стираем repTimeList
                    repTimeList.clear();
                }
                Log.d(TAG,"//##// mButtonReset count = " + ii);

                //Останавливаем часы и обнуляем их, подаём звуковой сигнал
                if (mTimer!=null)mTimer.cancel();
                mTotalTime = 0;
                mCurrentTime.setText(R.string.CountZeroTime);
                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 300);

                ii = 0;
                mTimeLast = 0;   // время на предыдущей отсечке
                mTimeStart = 0;  //время в момент нажатия на Старт
                mTimeNext = 0;   //время на последующей отсечке
                mDelta = 0;   //время между соседними отсечками
                mDeltaDelaySummary = 0; //суммарное время приостановки секундомера
                mDeltaDelayCurrent = 0; //Текущее время приостановки секундомера
                mRestart = false; //признак повторного, а не первого старта
                mIsStop = false; //признак нажатия кнопки стоп для отсчёта приостановки времени


                dataSec.clear();
                if (adapter != null){
                    adapter.notifyDataSetChanged();
                }
                mButtonReset.setEnabled(false);
                mButtonReset.setVisibility(View.GONE);
                mButtonStart.setEnabled(true);
                mButtonStart.setText(R.string.start_button );
                mButtonStart.setVisibility(View.VISIBLE);
                mButtonStop.setEnabled(false);
                mButtonStop.setVisibility(View.GONE);
                mButtonNext.setEnabled(false);
                mButtonNext.setVisibility(View.GONE);

                //выставляем флаг нажатия на Старт = нет
                start = false;
                //вызываем onPrepareOptionsMenu для создания недоступномсти значков ToolBar
                //invalidateOptionsMenu();

            }
        });
    }

    private void setPauseListener() {
        mButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //останавливаем часы и подаём сигнал
                if (mTimer!=null)mTimer.cancel();
                //засекаем время в момент приостановки секундомера
                mTimeStop = System.currentTimeMillis();
                //подаём звуковой сигнал
                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 100);

                mButtonStart.setEnabled(true);
                mButtonStart.setText(R.string.continue_task);
                mButtonStart.setVisibility(View.VISIBLE);
                mButtonReset.setEnabled(true);
                mButtonReset.setVisibility(View.VISIBLE);
                mButtonStop.setEnabled(false);
                mButtonStop.setVisibility(View.GONE);
                mButtonNext.setEnabled(false);
                mButtonNext.setVisibility(View.GONE);

                //признак нажатия стоп
                mIsStop = true;
                //признак нахождения в режиме паузы
                mIsStopped = true;
            }
        });
    }

    private void setNextListener() {
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 50);

                mTimeNext = System.currentTimeMillis();
                switch (pause){
                    case 1:
                        mTimeExersize = mTimeNext-mTimeStart - mDeltaDelaySummary;
                        mDelta = mTimeNext - mTimeLast - mDeltaDelayCurrent;
                        break;
                    case 2:
                        mTimeExersize = mTimeNext-mTimeStart;
                        mDelta = mTimeNext - mTimeLast;
                        break;
                }

                String s_item = Integer.toString(ii+1);
                String s_time;
                String s_delta;

                switch (accurancy){
                    case 1:
                        s_time = P.getTimeString1(mTimeExersize);
                        s_delta = P.getTimeString1 (mDelta);
                        break;
                    case 2:
                        s_time = P.getTimeString2(mTimeExersize);
                        s_delta = P.getTimeString2 (mDelta);
                        break;
                    case 3:
                        s_time = P.getTimeString3(mTimeExersize);
                        s_delta = P.getTimeString3 (mDelta);
                        break;
                    default:
                        s_time = P.getTimeString1(mTimeExersize);
                        s_delta = P.getTimeString1 (mDelta);
                }

                dataSecundomer = new DataSecundomer(s_item, s_time, s_delta);
                dataSec.add(0,dataSecundomer);

                updateAdapter(dataSec);

                //добавляем время отсечки в список (в конец) для записи в базу в диалоге сохранения
                repTimeList.add(Long.toString(mTimeExersize));
                Log.d(TAG, "//##//  repTimeList.size = " + repTimeList.size());

                //увеличиваем порядковый номер отсечки
                ii++;
                //обновляем время на предыдущей отсечке
                mTimeLast = mTimeNext;
                //признак нажатия стоп
                mIsStop = false;
                //обнуление текущей приостановки секундомера (между отсечками ее не было)
                mDeltaDelayCurrent = 0;
            }
        });
    }

    private void setStartListener() {
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //признак нахождения в режиме паузы
                mIsStopped = false;

                if (!mRestart){
                    mTimeStart = System.currentTimeMillis();
                    mTimeLast = mTimeStart;
                }else {
                    mTimeRestart = System.currentTimeMillis();
                    mDeltaDelaySummary += mTimeRestart - mTimeStop;
                    if (mIsStop){
                        mDeltaDelayCurrent += mTimeRestart - mTimeStop;
                    }
                    Log.d(TAG, "//##//  mDeltaDelaySummary = " + mDeltaDelaySummary +
                            "  mDeltaDelayCurrent = " + mDeltaDelayCurrent);
                }

                if (mTimer!=null)mTimer.cancel();
                mTimer =new Timer();
                mTimerTask = new MyTimerTask();
                mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
                //запускаем TimerTask на выполнение с периодом mKvant
                mTimer.scheduleAtFixedRate(mTimerTask,mKvant,mKvant);
                //играем мелодию начала подхода  с задержкой
                mToneGenerator.startTone(ToneGenerator.TONE_DTMF_0, 50);

                mButtonStop.setEnabled(true);
                mButtonStop.setVisibility(View.VISIBLE);
                mButtonReset.setEnabled(false);
                mButtonReset.setVisibility(View.GONE);
                mButtonStart.setEnabled(false);
                mButtonStart.setVisibility(View.GONE);
                mButtonNext.setEnabled(true);
                mButtonNext.setVisibility(View.VISIBLE);
                //выставляем признак рестарта
                mRestart = true;
                //выставляем флаг нажатия на Старт = да
                start = true;
                //вызываем onPrepareOptionsMenu для создания недоступномсти значков ToolBar
                //invalidateOptionsMenu();

            }
        });
    }


    //инициализация вьюх
    private void initViews(@NonNull View view) {
        //для textViewTime в макете стоит запрет на выключение экрана android:keepScreenOn="true"
        mCurrentTime = view.findViewById(R.id.tvTime);
        mCurrentTimePause = view.findViewById(R.id.tvTimePause);
        mButtonStart = view.findViewById(R.id.buttonStartSec);
        //кнопка "Круг"
        mButtonNext = view.findViewById(R.id.buttonNextSec);
        //Кнопка "Пауза"
        mButtonStop = view.findViewById(R.id.buttonStopSec);
        mButtonReset = view.findViewById(R.id.buttonResetSec);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"//##//  SecundomerFragment onResume");
        //получаем настройки из активности настроек
        prefSetting = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG,"//##//  SecundomerFragment onResume accurancy = " + accurancy);
        //получаем режим работы секундомера в паузе
        pause = Integer.parseInt(prefSetting.getString("pause_type", "1"));
        Log.d(TAG,"//##//  SecundomerFragment onResume pause = " + pause);
        //получаем из файла настроек наличие звука
        sound = prefSetting.getBoolean("cbSound",true);
        //включаем/выключаем звук в зависимости от состояния чекбокса в PrefActivity
        AudioManager audioManager;
        audioManager =(AudioManager)requireActivity().getSystemService(Context.AUDIO_SERVICE);
        if(sound){
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }else{
            audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"//##//  SecundomerFragment onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"//##//  SecundomerFragment onDestroy");
        //стираем список
        repTimeList.clear();
        //выключаем таймер
        if (mTimer!=null)mTimer.cancel();
    }



//
//    //отслеживаем нажатие аппаратной кнопки Back и запрещаем, если секундомер работает
//    @Override
//    public void onBackPressed() {
//        if (start){
//            Log.d(TAG,"TimeMeterActivity onBackPressed if (start)");
//            Toast.makeText(getApplicationContext(),
//                    R.string.PressStopBefore, Toast.LENGTH_SHORT).show();
//        }else{
//            super.onBackPressed();
//            Log.d(TAG,"TimeMeterActivity onBackPressed if (!start)");
//        }
//    }

    //======================class MyTimerTask=================================//
    public class MyTimerTask extends TimerTask{
        @Override
        public void run() {  //запускаем MyTimerTask в методе run()
            //Рассчитываем общее время в зависимости от режима паузы секундомера
            switch (pause){
                case 1:
                    //Остановка времени
                    //Общее время
                    mTotalTime = System.currentTimeMillis() - mTimeStart - mDeltaDelaySummary;
                    // если в режиме паузы, показать текущее время паузы
                    if (mIsStopped){
                        mPauseTime =  System.currentTimeMillis() - mTimeStop;
                    }
                    break;
                case 2:
                    //Остановка индикации времени
                    mTotalTime = System.currentTimeMillis() - mTimeStart;
                    break;
            }
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String time = P.getTimeString1(mTotalTime);
                    String timePause = P.getTimeString1(mPauseTime);
                    //показ текущ времени
                    mCurrentTime.setText(time);
                    if (mIsStopped){
                        mCurrentTimePause.setText(timePause);
                    }
                }
            });
        }
    }
    //====================  end class MyTimerTask  =================================//

    //диалог сохранения, оформленный как класс с указанием имени файла
    private void openSaveInFileDialogFragment() {
        DialogSaveSec dialogFragment = new DialogSaveSec();
        dialogFragment.setSaveSectListener(this);
        dialogFragment.show(requireActivity().getSupportFragmentManager(),"SavePickerSecundomer");
    }
}