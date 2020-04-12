package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class TempoleaderViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<DataSet>> data;
    private TempoleaderStorage storage;

    public TempoleaderViewModel(@NonNull Application application) {
        super(application);
        storage = new TempoleaderStorageImpl(application);
    }

    public LiveData<ArrayList<DataSet>>  loadDataSet(String fileName){
        if (data == null){
            data = new MutableLiveData<>();
            getDataSet(fileName);
        }
        return data;
    }

    public LiveData<ArrayList<DataSet>> getDataSet(String fileName) {
        data.setValue(storage.getDataSet(fileName));
        return data;
    }
}