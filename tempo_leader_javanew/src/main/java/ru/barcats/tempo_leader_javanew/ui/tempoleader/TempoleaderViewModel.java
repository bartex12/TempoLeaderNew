package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class TempoleaderViewModel extends AndroidViewModel {

    public static final String TAG ="33333";
    private MutableLiveData<ArrayList<DataSet>>  data = new MutableLiveData<>();
    private TempoleaderStorage tempoleaderStorage;

    private MutableLiveData<Integer>  delay = new MutableLiveData<>();
    private DelayStorage delayStorage;

    public TempoleaderViewModel(@NonNull Application application) {
        super(application);
        tempoleaderStorage = new TempoleaderStorageImpl(application);
        delayStorage = new DelayStorageImpl(application);
    }

    public LiveData<Integer> loadDelay(String fileName) {
        getDelaynew(fileName);
        return delay;
    }

    private  void getDelaynew(String fileName){
        delay.setValue(delayStorage.getDelayNew(fileName));
    }

    public  void updateDelayNew(int timeOfDelay, String fileName){
        int delayNew = delayStorage.updateDelayNew(timeOfDelay, fileName);
        delay.setValue(delayNew);
        Log.d(TAG, "// ### // TempoleaderViewModel delayNew = " + delay.getValue());
    }

    public LiveData<ArrayList<DataSet>> loadDataSet(String fileName) {
        getDataSet(fileName);
        return data;
    }

    private void getDataSet(String fileName) {
        data.setValue(tempoleaderStorage.getDataSet(fileName));
    }

    public float getSumOfTimes(String finishFileName){
        return tempoleaderStorage.getSumOfTimes(finishFileName);
    }

    public int getSumOfReps(String finishFileName){
        return tempoleaderStorage.getSumOfReps(finishFileName);
    }

    public int getFragmentsCount(String finishFileName){
        return tempoleaderStorage.getFragmentsCount(finishFileName);
    }

    public int getDelay(String finishFileName){
        return tempoleaderStorage.getDelay(finishFileName);
    }

    public void updateDelay(int timeOfDelay, String finishFileName){
        tempoleaderStorage.updateDelay(timeOfDelay, finishFileName);
    }

    public long getIdFromFileName(String finishFileName){
        return tempoleaderStorage.getIdFromFileName(finishFileName);
    }

    public float getTimeOfRepInPosition(long fileId, int numberOfSet){
        return tempoleaderStorage.getTimeOfRepInPosition(fileId, numberOfSet);
    }

    public int getRepsInPosition(long fileId, int numberOfSet){
        return tempoleaderStorage.getRepsInPosition(fileId, numberOfSet);
    }

}
