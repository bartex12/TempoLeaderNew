package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataSet;

public interface TempoleaderStorage {
    ArrayList<DataSet> getDataSet(String fileName);
}
