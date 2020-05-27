package ru.barcats.tempo_leader_javanew.ui.home;

import java.util.ArrayList;

import ru.barcats.tempo_leader_javanew.model.DataHome;

public interface HomeStorage {
    ArrayList<DataHome> getListMain();
    void createDefoultFile();
}
