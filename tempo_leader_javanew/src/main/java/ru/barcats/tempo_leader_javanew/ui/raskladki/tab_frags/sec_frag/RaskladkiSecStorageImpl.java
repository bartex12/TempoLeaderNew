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

        //если в базе нет данных - пишем файл автосохранение_секундомера
        tempDBHelper.createDefaultSetIfNeed(database);

        //получаем все файлы для вкладки темполидер
        data = TabFile.getArrayListFilesWhithType(database, P.TYPE_TIMEMETER);

        //TODO  удалить
        for (int i = 0; i <20 ; i++) {
            data.add("файл " + i);
        }

        return data;
    }
}
