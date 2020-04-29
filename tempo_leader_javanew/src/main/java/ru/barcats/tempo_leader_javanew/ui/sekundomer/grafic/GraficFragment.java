package ru.barcats.tempo_leader_javanew.ui.sekundomer.grafic;


import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Map;

import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;

/**
 * A simple {@link Fragment} subclass.
 */
public class GraficFragment extends Fragment {

    public static final String TAG ="33333";

    public static final String REP_TIME_LIST = "ru.bartex.p010_train.repTimeList";

    ListView mListViewTiming;
    TextView nameOfFile;

    private GraphView graphPace;
    LineGraphSeries<DataPoint> seriesPace;
    DataPoint[] pointPace1;

    //список данных для показа на экране
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    SimpleAdapter sara;
    int accurancy; //точность отсечек - количество знаков после запятой - от MainActivity
    private SharedPreferences prefSetting;// предпочтения из PrefActivity
    private String finishFileName = P.FINISH_FILE_NAME;

    //TempDBHelper mTempDBHelper = new TempDBHelper(this);
    private SQLiteDatabase database;

    public GraficFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new TempDBHelper(requireActivity()).getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_grafic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
    }
}
