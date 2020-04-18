package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;

import java.util.ArrayList;

public interface RaskladkiStorage {

    ArrayList<String> getRaskladkiList(String tabType);
    ArrayList<String> deleteItem(String fileName, String tabType);
    ArrayList<String> moveItemInLike(String fileName);
    ArrayList<String> moveItemInTemp(String fileName);
    ArrayList<String> moveFromTo(String fileName,String from,String to);
    ArrayList<String> doChangeAction(String fileNameOld, String fileNameNew, String tabType);
}
