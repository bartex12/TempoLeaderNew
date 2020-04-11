package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag;

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


public class RaskladkiSecStorageImpl implements RaskladkiSecStorage {

    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;
    private ArrayList<String> data;

    public RaskladkiSecStorageImpl(Context context) {
        tempDBHelper = new TempDBHelper(context);
        database =  tempDBHelper.getWritableDatabase();
        data = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getRaskladkiList() {
        //количество файлов в TabFile
        int count = TabFile.getFilesCount(database);
        Log.d(TAG, "RaskladkiSecStorageImpl FilesCount = " + count);

        if (count<=0){
            //получаем дату и время в нужном для базы данных формате
            String dateFormat = tempDBHelper.getDateString();
            String timeFormat = tempDBHelper.getTimeString();

            //создаём экземпляр класса DataFile в конструкторе
            DataFile file1 = new DataFile(P.FILENAME_OTSECHKI_SEC,
                    dateFormat, timeFormat, "Подтягивание",
                    "Подтягивание на перекладине", P.TYPE_TIMEMETER, 6);
            //добавляем запись в таблицу TabFile, используя данные DataFile и получаем id записи
            long file1_id = TabFile.addFile(database, file1);

            //создаём экземпляр класса DataSet в конструкторе
            DataSet set1 = new DataSet(2f, 1, 1);
            //добавляем запись в таблицу TabSet, используя данные DataSet
            TabSet.addSet(database, set1, file1_id);
            // повторяем для всех фрагментов подхода
            DataSet set2 = new DataSet(2.5f, 1, 2);
            TabSet.addSet(database, set2, file1_id);
            DataSet set3 = new DataSet(3f, 1, 3);
            TabSet.addSet(database, set3, file1_id);
            DataSet set4 = new DataSet(3.5f, 1, 4);
            TabSet.addSet(database, set4, file1_id);

            data.add(file1.getFileName());

        }else {
            data = TabFile.getArrayListFilesFromTimemeter(database);

            data.add("Первый");
            data.add("Второй");
            data.add("Третий");
        }
        return data;
    }
}
