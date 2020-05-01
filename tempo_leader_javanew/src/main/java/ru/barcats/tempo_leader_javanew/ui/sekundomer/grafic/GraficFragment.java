package ru.barcats.tempo_leader_javanew.ui.sekundomer.grafic;



import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;
import ru.barcats.tempo_leader_javanew.model.P;


import static android.content.Context.MODE_PRIVATE;


public class GraficFragment extends Fragment {

    private static final String TAG ="33333";

    private int accurancy; //точность отсечек - количество знаков после запятой - от MainActivity
    private RecyclerView recyclerView;
    private GraficViewModel graficViewModel;
    private String finishFileName;
    private SharedPreferences prefLastFile;
    private OnTransmitListener onTransmitListener;

    public interface OnTransmitListener{
        void onTransmit(String data);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onTransmitListener = (OnTransmitListener)context;
    }

    public GraficFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"GraficFragment onCreate");

        graficViewModel = new ViewModelProvider(this).get(GraficViewModel.class);
        //получаем имя последнего файла темполидера из преференсис (запись в onDestroy )
        // последний сохранённый файл
        prefLastFile = requireActivity().getPreferences(MODE_PRIVATE);
        String s =  prefLastFile.getString(P.LAST_FILE,P.FILENAME_OTSECHKI_SEC);
        Log.d(TAG,"GraficFragment prefLastFile = " + s);

        //если ничего нет в аргументах, смотрим в  SharedPreferences,
        //если файл с таким именем был удалён, то грузим автосохранение секундомера
        if (getArguments() != null){
            finishFileName = getArguments().getString(P.NAME_OF_FILE);
        }else {
            finishFileName = prefLastFile.getString(P.LAST_FILE, P.FILENAME_OTSECHKI_SEC);
        }

        Log.d(TAG,"GraficFragment onCreate имя = " + finishFileName);
      if (graficViewModel.getFragmentsCount(finishFileName)<=0){
          finishFileName = P.FILENAME_OTSECHKI_SEC;
      }
        //НЕ стирать = без этой строки меню тулбара пропадант,
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"GraficFragment onCreateView");
        return inflater.inflate(R.layout.fragment_grafic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"GraficFragment onViewCreated");

        TextView nameOfFile = view.findViewById(R.id.textViewName_TimeGraf);
        nameOfFile.setText(finishFileName);

        recyclerView = view.findViewById(R.id.recycler_grafic);
        GraphView graphPace = view.findViewById(R.id.graphPace);

        //получаем настройки из активности настроек
        SharedPreferences prefSetting = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        //получаем из файла настроек количество знаков после запятой
        accurancy = Integer.parseInt(prefSetting.getString("accurancy", "1"));
        Log.d(TAG, "GraficFragment accurancy = " + accurancy);

        //получаем курсор с данными подхода для finishFileName
        Cursor cursor = graficViewModel.getAllSetFragmentsRaw(finishFileName);
        // Узнаем индекс  столбца
        int idColumnIndex =  graficViewModel.getColumnIndex(cursor);

        //размер массива данных для линии графика = размер курсора
        DataPoint[] pointPace1 = new DataPoint[cursor.getCount()];
        //суммарное время подхода сначала = 0
        long time_total = 0;  //суммарное время подхода
        long time_now;  //время на отрезке

        //проходим по курсору и берём данные
        if (cursor.moveToFirst()) {
            do {
                // Используем индекс для получения строки или числа и переводим в милисекунды
                //чтобы использовать ранее написанные функции getTimeString1
                time_now = (long) (cursor.getFloat(idColumnIndex) * 1000);
                time_total += time_now;
                Log.d(TAG, "GraficFragment cursor.getPosition()+1 = " +
                        (cursor.getPosition() + 1) + "  time_now = " + time_now +
                        "  time_total = " + time_total);

                //Переводим время в секунды
                double time_tran = (double) time_now / 1000;
                //добавляем точку для графика, используя double time_tran
                pointPace1[cursor.getPosition()] = new DataPoint(
                        (cursor.getPosition() + 1), time_tran);

            } while (cursor.moveToNext());
        }
        //заполняем линию графика точками
        LineGraphSeries<DataPoint> seriesPace = new LineGraphSeries<>(pointPace1);
        //устснавливаем параметры кривой графика
        setParamSeries(seriesPace);
        //добавляем кривую на график
        graphPace.addSeries(seriesPace);
        //устанавливаем параметры графика
        setParamGraph(graphPace, cursor.getCount());
        //Название графика
        graphPace.setTitle("Длительность круга, с");

        // грузим данные и следим за их изменением, кроме того передаём имя файла в Main
        graficViewModel.loadDataSet(finishFileName, accurancy)
                .observe(getViewLifecycleOwner(), new Observer<ArrayList<DataSecundomer>>() {
                    @Override
                    public void onChanged(ArrayList<DataSecundomer> dataSecundomers) {
                        //показываем список на экране
                        updateAdapter(view, dataSecundomers);
                        onTransmitListener.onTransmit(finishFileName);
                    }
                });
    }


    private void setParamSeries(LineGraphSeries<DataPoint> series){
        series.setColor(Color.BLUE);
        //ручная установка параметров
        series.setDrawDataPoints(true);
        //радиус точки
        series.setDataPointsRadius(8);
        // толщина линии
        series.setThickness(4);
    }

    private void setParamGraph(GraphView graph, int size){
        graph.getGridLabelRenderer().setTextSize(50);
        //  ручная установка горизонт пределов
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(size);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Номер цикла");
        // разрешение горизонт прокрутки
        //graph.getViewport().setScrollable(true);
    }

    private  void updateAdapter(View view, ArrayList<DataSecundomer> dataSecundomers){
        Log.d(TAG, " /*/GraficFragment  updateAdaptersize =  " + dataSecundomers.size());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        RecyclerViewGraficAdapter adapter = new RecyclerViewGraficAdapter(dataSecundomers);
        recyclerView.setAdapter(adapter);
    }
}
