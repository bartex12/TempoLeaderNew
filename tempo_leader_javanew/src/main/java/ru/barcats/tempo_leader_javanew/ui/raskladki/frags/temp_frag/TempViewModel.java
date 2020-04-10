package ru.barcats.tempo_leader_javanew.ui.raskladki.frags.temp_frag;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.barcats.tempo_leader_javanew.ui.raskladki.frags.sec_frag.RaskladkiSecStorage;
import ru.barcats.tempo_leader_javanew.ui.raskladki.frags.sec_frag.RaskladkiSecStorageImpl;

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