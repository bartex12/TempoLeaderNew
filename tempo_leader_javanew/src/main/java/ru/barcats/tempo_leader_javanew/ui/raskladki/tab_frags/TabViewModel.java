package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class TabViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> dataTab =new MutableLiveData<>();;
    private RaskladkiStorage storageTab;
    private MutableLiveData<String> dateTimeTab = new MutableLiveData<>();
    private DateTimeStorage storageDateTimeTab;

    public TabViewModel(@NonNull Application application) {
        super(application);
        storageTab = new RaskladkiStorageImpl(application);
        storageDateTimeTab = new DateTimeStorageImpl(application);
    }

    public LiveData<ArrayList<String>> getRascladki(String tabType) {
        loadData(tabType);
        return dataTab;
    }

    private void loadData(String tabType){
        //
        dataTab.setValue(storageTab.getRaskladkiList(tabType));
    }

    public void loadDataDeleteItem(String fileName, String tabType){
        //
        dataTab.setValue(storageTab.deleteItem(fileName, tabType));
    }

    public void  moveItemInLike(String fileName){
        //
        dataTab.setValue(storageTab.moveItemInLike(fileName));
    }

    public void  moveItemInTemp(String fileName){
        //
        dataTab.setValue(storageTab.moveItemInTemp(fileName));
    }

    public void moveFromTo(String fileName,String from,String to){
        //
        dataTab.setValue(storageTab.moveFromTo(fileName, from, to));
    }

    public void  doChangeAction(String fileNameOld, String fileNameNew, String tabType){
        //
        dataTab.setValue(storageTab.doChangeAction(fileNameOld, fileNameNew, tabType));
    }

    public String  getDateAndTime(String fileName){
        dateTimeTab.setValue(storageDateTimeTab.getDateAndTime(fileName));
        return dateTimeTab.getValue();
    }

}
