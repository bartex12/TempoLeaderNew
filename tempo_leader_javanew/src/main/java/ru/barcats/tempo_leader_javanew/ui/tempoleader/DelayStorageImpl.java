package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ru.barcats.tempo_leader_javanew.database.TabFile;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;


public class DelayStorageImpl  implements DelayStorage {

    private static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;

    public DelayStorageImpl(Application application) {
        tempDBHelper = new TempDBHelper(application);
        database = tempDBHelper.getWritableDatabase();
    }


    @Override
    public int getDelayNew(String fileName) {
        return  TabFile.getFileDelayFromTabFile(database,fileName);
    }

    @Override
    public int updateDelayNew(int timeOfDelay, String finishFileName) {
        long fileId = TabFile.getIdFromFileName(database, finishFileName);
        TabFile.updateDelay(database,timeOfDelay,fileId);
        int delay =  TabFile.getFileDelayFromTabFile(database,finishFileName);
        Log.d(TAG," //~~~// DelayStorageImpl delay = " + delay);
        return  delay;
    }
}
