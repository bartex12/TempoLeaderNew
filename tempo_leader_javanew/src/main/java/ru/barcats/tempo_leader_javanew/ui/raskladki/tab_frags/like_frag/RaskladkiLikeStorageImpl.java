package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.P;


public class RaskladkiLikeStorageImpl implements RaskladkiLikeStorage {

    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;
    private ArrayList<String> data;

    public RaskladkiLikeStorageImpl(Context context) {
        tempDBHelper = new TempDBHelper(context);
        database =  tempDBHelper.getWritableDatabase();
        data = new ArrayList<>();
    }

    @Override
    public ArrayList<String> getRaskladkiList() {

        //получаем все файлы для вкладки темполидер
        data = TabFile.getArrayListFilesWhithType(database,P.TYPE_LIKE);

        for (int i = 0; i <20 ; i++) {
            data.add("файл " + i*100);
        }

        return data;
    }
}
