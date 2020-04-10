package ru.barcats.tempo_leader_javanew.ui.raskladki.frags.sec_frag;

import java.util.ArrayList;


public class RaskladkiSecStorageImpl implements RaskladkiSecStorage {
    @Override
    public ArrayList<String> getRaskladkiList() {
       ArrayList<String> temp = new ArrayList();
        temp.add("Первый");
        temp.add("Второй");
        temp.add("Третий");
        return temp;
    }
}
