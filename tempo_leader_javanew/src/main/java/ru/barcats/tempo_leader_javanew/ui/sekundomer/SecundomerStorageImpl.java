package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TabSet;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class SecundomerStorageImpl implements SecundomerStorage{

    private static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;

    public SecundomerStorageImpl(Application application) {
        tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
    }

    @Override
    public ArrayList<DataSecundomer> loadData() {
        ArrayList<DataSecundomer> data = new ArrayList<DataSecundomer>();
        data.add(new DataSecundomer("1","2", "3"));
        return data;
    }

    @Override
    public long getIdFromFileName(String finishNameFile) {
        return TabFile.getIdFromFileName(database, finishNameFile);
    }

    @Override
    public String getDateString() {
        return tempDBHelper.getDateString();
    }

    @Override
    public String getTimeString() {
        return tempDBHelper.getTimeString();
    }

    @Override
    public void deleteFileAndSets(long id) {
        tempDBHelper.deleteFileAndSets(database, id);
    }

    @Override
    public long addFile(DataFile dataFile) {
        return TabFile.addFile(database, dataFile);
    }

    @Override
    public void addSet(DataSet set, long id) {
        TabSet.addSet(database, set, id);
    }
}
