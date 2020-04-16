package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SecViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> data = new MutableLiveData<>();
    private RaskladkiSecStorage storage;

    public SecViewModel(@NonNull Application application) {
        super(application);
        storage = new RaskladkiSecStorageImpl(application);
    }

    public LiveData<ArrayList<String>> getRascladki() {
        loadData();
        return data;
    }
    private void loadData(){
        //
        data.setValue(storage.getRaskladkiList());
    }

    public void loadDataDeleteItem(String fileName){
        //
        data.setValue(storage.deleteItem(fileName));
    }
}