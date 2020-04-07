package ru.barcats.tempo_leader_javanew.ui.home;

import android.app.Application;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.R;
import ru.barcats.tempo_leader_javanew.model.DataHome;

public class HomeStorageImpl implements HomeStorage {

   private Application application;

    public HomeStorageImpl(Application application){
        this.application = application;
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
}