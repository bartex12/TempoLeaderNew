package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.sec_frag.DateTimeSecStorage;

public class TempViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> data =new MutableLiveData<>();;
    private RaskladkiTempStorage storage;
    private MutableLiveData<String> dateTime = new MutableLiveData<>();
    private DateTimeTempStorage storageDateTime;

    public TempViewModel(@NonNull Application application) {
        super(application);
        storage = new RaskladkiTempStorageImpl(application);
        storageDateTime = new DateTimeTempStorageImpl(application);
    }


    public LiveData<ArrayList<String>> getRascladki() {
        loadData();
        return data;
    }

    private void loadData(){
        data.setValue(storage.getRaskladkiList());
    }

    public void loadDataDeleteItem(String fileName){
        data.setValue(storage.deleteItem(fileName));
    }

    public void  moveItemInLike(String fileName){
        data.setValue(storage.moveItemInLike(fileName));
    }

    public void moveItemInSec(String fileName){
        data.setValue(storage.moveItemInSec(fileName));
    }

    public String  getDateAndTime(String fileName){
        dateTime.setValue(storageDateTime.getDateAndTime(fileName));
        return dateTime.getValue();
    }

    public void  doChangeAction(String fileNameOld, String fileNameNew){
        data.setValue(storage.doChangeAction(fileNameOld, fileNameNew));
    }

}