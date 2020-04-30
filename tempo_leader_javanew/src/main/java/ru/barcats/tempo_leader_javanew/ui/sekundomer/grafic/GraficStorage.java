package ru.barcats.tempo_leader_javanew.ui.sekundomer.grafic;

import android.database.Cursor;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataSecundomer;

public interface GraficStorage {

    ArrayList<DataSecundomer> getDataSet(String fileName, int accurancy);
    int getFragmentsCount(String finishFileName);

    int getColumnIndex(Cursor cursor);

    Cursor getAllSetFragmentsRaw(String finishFileName);
}
