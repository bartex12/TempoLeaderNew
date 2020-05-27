package ru.barcats.tempo_leader_javanew.ui.home;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.database.TempDBHelper;
import ru.barcats.tempo_leader_javanew.model.DataHome;

public class HomeStorageImpl implements HomeStorage {

   private Application application;
    public static final String TAG = "33333";
    private SQLiteDatabase database;
    private TempDBHelper tempDBHelper;

    public HomeStorageImpl(Application application){
        this.application = application;
        tempDBHelper = new TempDBHelper(application);
        database =  tempDBHelper.getWritableDatabase();
    }

    @Override
    public ArrayList<DataHome> getListMain() {

        String[] stringListMain = application.getResources().getStringArray(R.array.MenuMain);
        String[] stringListSubMain = application.getResources().getStringArray(R.array.MenuSubMain);

        ArrayList<DataHome> list = new ArrayList<>();
        list.add(new DataHome(application.getDrawable(R.drawable.sec1),
                stringListMain[0], stringListSubMain[0]));

        list.add(new DataHome(application.getDrawable(R.drawable.metronome70),
                stringListMain[1], stringListSubMain[1]));

        list.add(new DataHome(application.getDrawable(R.drawable.list80x100),
                stringListMain[2], stringListSubMain[2]));

        return list;
    }

    @Override
    public void createDefoultFile() {
        //если в базе нет данных - пишем файл автосохранение_секундомера
        tempDBHelper.createDefaultSetIfNeed(database);
    }
}