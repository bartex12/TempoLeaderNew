package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataSet;

public interface EditorStorage {
    ArrayList<DataSet> getDataSet(String fileName);
}
