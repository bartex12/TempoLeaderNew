package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataSet;

public interface EditorStorage {

    ArrayList<DataSet> getDataSet(String fileName);

    ArrayList<DataSet>minus5Action(
            String fileName,float deltaTime, int countReps, boolean redactTime, boolean isChecked, int position);

   long getCopyFile(String fileName);

    ArrayList<DataSet> revertEdit (String fileName, long fileIdCopy);

    void clearCopyFile(long fileIdCopy, String fileName);

    ArrayList<DataSet> changeTemp(String finishFileName, int valueDelta, boolean up);

    DataSet getDataSetNew(String fileName);

    ArrayList<DataSet> addSet(DataSet dataSet, String finishFileName);

    float getSumOfTimes(String finishFileName);
    int getSumOfReps(String finishFileName);
    int getFragmentsCount(String finishFileName);

    long saveAsFile(String oldFileName, String newFileName, long fileOldIdCopy);

    DataSet getOneSetData(String finishFileName, int positionOfList);

    ArrayList<DataSet> updateSetFragment(DataSet dataSet, String finishFileName);

    ArrayList<DataSet>insertSetAfter(DataSet dataSet, String finishFileName, int position);

    ArrayList<DataSet>insertSetBefore(DataSet dataSet, String finishFileName, int position);

    ArrayList<DataSet> deleteOneSet(String fileName, int position);
}
