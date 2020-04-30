package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag;

import java.util.ArrayList;

public interface RaskladkiSecStorage {
   ArrayList<String> getRaskladkiList();
   ArrayList<String> deleteItem(String fileName);
   ArrayList<String> moveItemInTemp(String fileName);
   ArrayList<String> moveItemInLike(String fileName);
   ArrayList<String> doChangeAction(String fileNameOld, String fileNameNew);
   long getIdFromFileName(String fileName);



}
