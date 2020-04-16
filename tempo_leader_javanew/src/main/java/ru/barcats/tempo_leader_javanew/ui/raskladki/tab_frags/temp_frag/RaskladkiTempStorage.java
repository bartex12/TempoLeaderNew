package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag;

import java.util.ArrayList;

public interface RaskladkiTempStorage {
   ArrayList<String> getRaskladkiList();
   ArrayList<String> deleteItem(String fileName);
   ArrayList<String> moveItemInLike(String fileName);
   ArrayList<String> moveItemInSec(String fileName);
}
