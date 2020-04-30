package ru.barcats.tempo_leader_javanew.ui.sekundomer.grafic;

import android.app.Application;
import android.database.Cursor;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.barcats.tempo_leader_javanew.model.DataSecundomer;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class GraficViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<DataSecundomer>> data = new MutableLiveData<>();
    private GraficStorage graficStorage;

    public GraficViewModel(@NonNull Application application) {
        super(application);
        graficStorage = new GraficStorageImpl(application);
    }

    public LiveData<ArrayList<DataSecundomer>> loadDataSet(String fileName, int accurancy) {
        getDataSet(fileName,accurancy);
        return data;
    }

    private LiveData<ArrayList<DataSecundomer>> getDataSet(String fileName, int accurancy) {
        data.setValue(graficStorage.getDataSet(fileName, accurancy));
        return data;
    }

    public int getFragmentsCount(String finishFileName){
        return graficStorage.getFragmentsCount(finishFileName);
    }

    public int getColumnIndex(Cursor cursor ){
        return graficStorage.getColumnIndex(cursor);
    }

    public Cursor getAllSetFragmentsRaw(String finishFileName){
        return graficStorage.getAllSetFragmentsRaw(finishFileName);
    }

}
