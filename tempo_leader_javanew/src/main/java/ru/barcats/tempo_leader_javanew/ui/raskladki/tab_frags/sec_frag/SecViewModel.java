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
    private MutableLiveData<String> dateTime = new MutableLiveData<>();
    private RaskladkiSecStorage storage;
    private DateTimeSecStorage storageDateTime;

    public SecViewModel(@NonNull Application application) {
        super(application);
        storage = new RaskladkiSecStorageImpl(application);
        storageDateTime = new DateTimeSecStorageImpl(application);
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

    public void  moveItemInTemp(String fileName){
        //
        data.setValue(storage.moveItemInTemp(fileName));
    }

    public void  moveItemInLike(String fileName){
        //
        data.setValue(storage.moveItemInLike(fileName));
    }

    public void  doChangeAction(String fileNameOld, String fileNameNew){
        data.setValue(storage.doChangeAction(fileNameOld, fileNameNew));
    }

    public String  getDateAndTime(String fileName){
        dateTime.setValue(storageDateTime.getDateAndTime(fileName));
        return dateTime.getValue();
    }

}