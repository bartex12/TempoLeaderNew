package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TempViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> data;
    private RaskladkiTempStorage storage;

    public TempViewModel() {

        storage = new RaskladkiTempStorageImpl();
    }

    public LiveData<ArrayList<String>> getRascladki() {
        if (data == null) {
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }

    private void loadData(){
        data.setValue(storage.getRaskladkiList());
    }
}