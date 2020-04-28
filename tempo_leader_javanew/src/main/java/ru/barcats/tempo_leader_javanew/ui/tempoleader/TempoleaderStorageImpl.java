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
    private static final String TAG ="33333";
    private SQLiteDatabase database;

    public TempoleaderStorageImpl( Application application) {
        database = new TempDBHelper(application).getWritableDatabase();
    }

    @Override
    public ArrayList<DataSet> getDataSet(String fileName) {
        //получаем id записи с таким именем
        long fileId = TabFile.getIdFromFileName (database, fileName);
        Log.d(TAG,"TempoleaderStorageImpl  имя =" + fileName + "  Id = " + fileId );
        final ArrayList<DataSet> arr = TabSet.getAllSetFragments(database, fileId);
        return arr;
    }

    @Override
    public float getSumOfTimes(String finishFileName) {
       long fileId = TabFile.getIdFromFileName(database, finishFileName);
       return  TabSet.getSumOfTimeSet(database, fileId);
    }

    @Override
    public int getSumOfReps(String finishFileName) {
        long fileId = TabFile.getIdFromFileName(database, finishFileName);
        return  TabSet.getSumOfRepsSet(database, fileId);
    }

    @Override
    public int getFragmentsCount(String finishFileName) {
        long fileId = TabFile.getIdFromFileName(database, finishFileName);
        return  TabSet.getSetFragmentsCount(database, fileId);
    }

    @Override
    public int getDelay(String finishFileName) {
        return  TabFile.getFileDelayFromTabFile(database,finishFileName);
    }

    @Override
    public void updateDelay(int timeOfDelay, String finishFileName) {
        long fileId = TabFile.getIdFromFileName(database, finishFileName);
        TabFile.updateDelay(database,timeOfDelay,fileId);
    }

    @Override
    public long getIdFromFileName(String finishFileName) {
        return TabFile.getIdFromFileName(database, finishFileName);
    }

    @Override
    public float getTimeOfRepInPosition(long fileId, int numberOfSet) {
       return TabSet.getTimeOfRepInPosition(database, fileId, numberOfSet);
    }

    @Override
    public int getRepsInPosition(long fileId, int numberOfSet) {
       return TabSet.getRepsInPosition(database, fileId, numberOfSet);
    }
}
