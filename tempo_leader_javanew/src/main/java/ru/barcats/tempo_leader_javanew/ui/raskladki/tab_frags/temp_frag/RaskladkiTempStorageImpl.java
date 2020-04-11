package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;


public class RaskladkiTempStorageImpl implements RaskladkiTempStorage {

    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;
    private ArrayList<String> data;

    public RaskladkiTempStorageImpl(Context context) {
        tempDBHelper = new TempDBHelper(context);
        database =  tempDBHelper.getWritableDatabase();
        data = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getRaskladkiList() {
        //количество файлов в TabFile
        int count = TabFile.getFilesCount(database);
        Log.d(TAG, "RaskladkiTempStorageImpl FilesCount = " + count);
        //если в таблице нет файлов - добавляем автосгенерированный файл
        if (count<=0){
            //получаем дату и время в нужном для базы данных формате
            String dateFormat = tempDBHelper.getDateString();
            String timeFormat = tempDBHelper.getTimeString();

            //создаём экземпляр класса DataFile в конструкторе
            DataFile file2 = new DataFile(P.FILENAME_OTSECHKI_TEMP,
                    dateFormat, timeFormat, "Полиатлон",
                    "Пистолетики", P.TYPE_TEMPOLEADER, 6);

            //добавляем запись в таблицу TabFile, используя данные DataFile и получаем id записи
            long file2_id = TabFile.addFile(database, file2);

            //создаём экземпляр класса DataSet в конструкторе
            DataSet set11 = new DataSet(2.2f, 3, 1);
            //добавляем запись в таблицу TabSet, используя данные DataSet
            TabSet.addSet(database, set11, file2_id);
            // повторяем для всех фрагментов подхода
            DataSet set22 = new DataSet(2.3f, 3, 2);
            TabSet.addSet(database, set22, file2_id);
            DataSet set33 = new DataSet(2.5f, 4, 3);
            TabSet.addSet(database, set33, file2_id);
            DataSet set44 = new DataSet(2.7f, 2, 4);
            TabSet.addSet(database, set44, file2_id);

            data.add(file2.getFileName());

        }else {
            data = TabFile.getArrayListFilesFromTempoleader(database);

            data.add("Четвёртый");
            data.add("Пятый");
            data.add("Шестой");
        }
        return data;
    }
}
