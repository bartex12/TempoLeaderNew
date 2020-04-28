package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataSet;

public interface TempoleaderStorage {
    ArrayList<DataSet> getDataSet(String fileName);
    float getSumOfTimes(String finishFileName);
    int getSumOfReps(String finishFileName);
    int getFragmentsCount(String finishFileName);
    int getDelay(String finishFileName);
    void  updateDelay(int timeOfDelay, String finishFileName);
    long getIdFromFileName(String finishFileName);
    float getTimeOfRepInPosition(long fileId, int numberOfSet);
    int getRepsInPosition(long fileId, int numberOfSet);


}
