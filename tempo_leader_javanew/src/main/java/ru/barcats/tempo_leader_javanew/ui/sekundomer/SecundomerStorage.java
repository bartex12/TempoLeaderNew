package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public interface SecundomerStorage {
    ArrayList <DataSecundomer> loadData();
    long getIdFromFileName(String finishNameFile);

   String getDateString();
   String getTimeString();

   void deleteFileAndSets(long id);

    long addFile(DataFile dataFile);

    void addSet(DataSet set, long id);
}
