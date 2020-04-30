package ru.barcats.tempo_leader_javanew.ui.sekundomer.grafic;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

public class GraficStorageImpl implements GraficStorage {

    private static final String TAG ="33333";
    private SQLiteDatabase database;

    public GraficStorageImpl(Application application) {
        database = new TempDBHelper(application).getWritableDatabase();
    }

    @Override
    public ArrayList<DataSecundomer> getDataSet(String fileName, int accurancy) {
        //получаем id записи с таким именем
        long finishFileId = TabFile.getIdFromFileName (database, fileName);
        Log.d(TAG,"GraficStorageImpl  имя =" + fileName + "  Id = " + finishFileId );
        //получаем курсор с данными подхода с id = finishFileId
        Cursor cursor = TabSet.getAllSetFragmentsRaw(database, finishFileId);
       //создаём ArrayList для DataSecundomer из курсора
        ArrayList<DataSecundomer> arr = new ArrayList<>(cursor.getCount());
        // Узнаем индекс столбца
        int idColumnIndex = cursor.getColumnIndex(TabSet.COLUMN_SET_TIME);
        //суммарное время подхода сначала = 0
        long time_total = 0;  //суммарное время подхода
        long time_now;  //время на отрезке
        //проходим по курсору и берём данные
        if (cursor.moveToFirst()) {
            do {
                // Используем индекс для получения строки или числа и переводим в милисекунды
                //чтобы использовать ранее написанные функции getTimeString1
                time_now = (long) (cursor.getFloat(idColumnIndex)*1000);
                time_total += time_now;
                Log.d(TAG,"GraficStorageImpl Position = " + (cursor.getPosition()+1) +
                        "  time_now = " + time_now + "  time_total = " + time_total);

                //Делаем данные для адаптера
                String s_item = Integer.toString(cursor.getPosition()+1);
                String s_time;
                String s_delta;

                switch (accurancy){
                    case 1:
                        s_time = P.getTimeString1(time_total);
                        s_delta = P.getTimeString1 (time_now);
                        break;
                    case 2:
                        s_time = P.getTimeString2(time_total);
                        s_delta = P.getTimeString2 (time_now);
                        break;
                    case 3:
                        s_time = P.getTimeString3(time_total);
                        s_delta = P.getTimeString3 (time_now);
                        break;
                    default:
                        s_time = P.getTimeString1(time_total);
                        s_delta = P.getTimeString1 (time_now);
                }
               DataSecundomer dataSecundomer = new DataSecundomer(s_item,s_time, s_delta);
                arr.add(0,dataSecundomer);
            } while (cursor.moveToNext());
        }
        return arr;
    }

    @Override
    public int getFragmentsCount(String finishFileName) {
        long fileId = TabFile.getIdFromFileName(database, finishFileName);
        return  TabSet.getSetFragmentsCount(database, fileId);
    }

    @Override
    public long getColumnIndex(Cursor cursor) {
        return cursor.getColumnIndex(TabSet.COLUMN_SET_TIME);
    }

    @Override
    public Cursor getAllSetFragmentsRaw(String finishFileName) {
       long finishFileId =  TabFile.getIdFromFileName(database, finishFileName);
       return TabSet.getAllSetFragmentsRaw(database, finishFileId);
    }
}
