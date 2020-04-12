package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class TempoleaderStorageImpl implements TempoleaderStorage {
    public static final String TAG ="33333";
    SQLiteDatabase database;

    public TempoleaderStorageImpl( Application application) {
        database = new TempDBHelper(application).getWritableDatabase();
    }

    @Override
    public ArrayList<DataSet> getDataSet(String fileName) {
        //TODO реально получать DataSet
        //получаем id записи с таким именем
        long fileId = TabFile.getIdFromFileName (database, fileName);
        Log.d(TAG,"TempoleaderStorageImpl  имя =" + fileName + "  Id = " + fileId );

        ArrayList<DataSet> arr = TabSet.getAllSetFragments(database,fileId);
        
        return arr;
    }
}
