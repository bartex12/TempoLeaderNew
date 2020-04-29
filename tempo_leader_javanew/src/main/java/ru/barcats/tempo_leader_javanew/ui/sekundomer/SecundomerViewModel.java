package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.barcats.tempo_leader_javanew.model.DataFile;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class SecundomerViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<DataSecundomer>> data = new MutableLiveData<>();
    private SecundomerStorage storage;

    public SecundomerViewModel(@NonNull Application application) {
        super(application);
        storage = new SecundomerStorageImpl(application);
    }

    public LiveData<ArrayList<DataSecundomer>> getDataSec() {
        loadDataSec();
        return data;
    }

    public void loadDataSec(){
        data.setValue(storage.loadData());
    }

    public long getIdFromFileName(String finishNameFile){
        return storage.getIdFromFileName(finishNameFile);
    }


    public String getDateString(){
        return storage.getDateString();
    }

    public String getTimeString(){
        return storage.getTimeString();
    }

    public void deleteFileAndSets(long id){
        storage.deleteFileAndSets(id);
    }

    public long addFile(DataFile dataFile){
        return  storage.addFile(dataFile);
    }

    public void addSet(DataSet set, long id){
        storage.addSet(set, id);
    }
}