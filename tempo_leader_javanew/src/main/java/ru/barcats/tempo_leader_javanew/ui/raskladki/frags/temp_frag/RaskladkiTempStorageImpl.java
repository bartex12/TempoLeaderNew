package ru.barcats.tempo_leader_javanew.ui.raskladki.frags.temp_frag;

import java.util.ArrayList;


public class RaskladkiTempStorageImpl implements RaskladkiTempStorage {
    @Override
    public ArrayList<String> getRaskladkiList() {
       ArrayList<String> temp = new ArrayList();
        temp.add("Четвёртый");
        temp.add("Пятый");
        temp.add("Шестой");
        return temp;
    }
}
