package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.model.P;

public class EditorStorageImpl implements EditorStorage {

    public static final String TAG = "33333";
    private SQLiteDatabase database;

    public EditorStorageImpl(Application application) {
        TempDBHelper tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
    }

    @Override
    public ArrayList<DataSet> getDataSet(String fileName) {
        //получаем id записи с таким именем
        long fileId = TabFile.getIdFromFileName (database, fileName);
        Log.d(TAG,"EditorStorageImpl  имя =" + fileName + "  Id = " + fileId );
        return TabSet.getAllSetFragments(database, fileId);
    }
}
