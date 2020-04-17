package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.Timer;
import java.util.TimerTask;

import ru.barcats.tempo_leader_javanew.database.TempDBHelper;

public class DelayStorageImpl implements DelayStorage {
    private static final String TAG ="33333";

    private Application application;

    public DelayStorageImpl(Application application) {
        this.application = application;
    }

    @Override
    public Float getDelay(float delay) {

        long endTime = System.currentTimeMillis() +(long) delay*1000;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return (float) (endTime -System.currentTimeMillis()) ;

    }

}
