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

    private MutableLiveData<ArrayList<DataSet>>  data = new MutableLiveData<>();
    private TempoleaderStorage tempoleaderStorage;
    private MutableLiveData<Float> delay;
    private DelayStorage delayStorage;


    public TempoleaderViewModel(@NonNull Application application) {
        super(application);
        tempoleaderStorage = new TempoleaderStorageImpl(application);
        delayStorage = new DelayStorageImpl(application);
    }

    public LiveData<ArrayList<DataSet>> loadDataSet(String fileName) {
        getDataSet(fileName);
        return data;
    }

    private LiveData<ArrayList<DataSet>> getDataSet(String fileName) {
        data.setValue(tempoleaderStorage.getDataSet(fileName));
        return data;
    }

    public LiveData<Float> getDelay(float delayNumber) {
        if (delay == null) {
            delay = new MutableLiveData<>();
        }
            delay.setValue(delayStorage.getDelay(delayNumber));
            return delay;
        }
    }
