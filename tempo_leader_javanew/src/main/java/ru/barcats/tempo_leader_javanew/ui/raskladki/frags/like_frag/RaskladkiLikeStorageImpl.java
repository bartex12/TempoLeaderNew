package ru.barcats.tempo_leader_javanew.ui.raskladki.frags.like_frag;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.ui.raskladki.frags.sec_frag.RaskladkiSecStorage;


public class RaskladkiLikeStorageImpl implements RaskladkiLikeStorage {
    @Override
    public ArrayList<String> getRaskladkiList() {
       ArrayList<String> temp = new ArrayList();
        temp.add("Седьмой");
        temp.add("Восьмой");
        temp.add("Девятый");
        return temp;
    }
}
