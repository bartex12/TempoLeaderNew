package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.Timer;
import java.util.TimerTask;

import ru.barcats.tempo_leader_javanew.database.TempDBHelper;

public class DelayStorageImpl implements DelayStorage {
    private static final String TAG ="33333";
    private final static long mKvant = 100;//время в мс между срабатываниями TimerTask
    private SQLiteDatabase database;
    private Timer mTimer;
    //private MyTimerTask mTimerTask;

    private long countMillisDelay;
    private long mCurrentDelay = 0; //текущее время для задержки
    private Application application;

    public DelayStorageImpl(Application application) {
        this.application = application;
        //database = new TempDBHelper(application).getWritableDatabase();
        //mTimer =new Timer();

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

        //        mTimerTask = new MyTimerTask(delay);
//        //запускаем TimerTask на выполнение с периодом mKvant
//        mTimer.scheduleAtFixedRate(mTimerTask, mKvant,mKvant);
    }

//    public class MyTimerTask  extends TimerTask {
//
//         MyTimerTask(int delay) {
//            countMillisDelay =(long)( delay*1000);
//        }
//
//        @Override
//        public void run() {
//            mCurrentDelay += mKvant;
//
//        }
//    }
}
