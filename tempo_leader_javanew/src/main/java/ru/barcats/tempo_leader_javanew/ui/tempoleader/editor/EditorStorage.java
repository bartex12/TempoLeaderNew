package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataSet;

public interface EditorStorage {
    ArrayList<DataSet> getDataSet(String fileName);
    ArrayList<DataSet>minus5Action(
            String fileName,float deltaTime, int countReps, boolean redactTime, boolean isChecked, int position);

   long getCopyFile(String fileName);

    ArrayList<DataSet>revertEdit (String fileName, long fileIdCopy);
}
