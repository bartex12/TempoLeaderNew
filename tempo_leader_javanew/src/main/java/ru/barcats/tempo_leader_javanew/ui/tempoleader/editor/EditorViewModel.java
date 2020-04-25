package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class EditorViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<DataSet>> data = new MutableLiveData<>();;
    private MutableLiveData<String> dataFile = new MutableLiveData<>();;
    private EditorStorage storageEditor;

    public EditorViewModel(@NonNull Application application) {
        super(application);
        storageEditor = new EditorStorageImpl(application);
    }

    //получение раскладки - публичный метод
    public LiveData<ArrayList<DataSet>> loadDataSet(String fileName) {
        loadData(fileName);
        return data;
    }

    //получение раскладки - приватный метод
    private void loadData(String fileName){
        //загрузка данных
        data.setValue(storageEditor.getDataSet(fileName));
    }

    //получение имени файла из LiveData
    public LiveData<String> getFileName(){
        return dataFile;
    }

    //получение и запись в LiveData  имени файла
    public String loadFileName(long fileIdNew){
        return storageEditor.loadFileName(fileIdNew);
    }

    public void edidAction(String fileName, float deltaTime, int countReps , boolean redactTime,
                           boolean isChecked, int position){
        data.setValue(storageEditor.minus5Action(fileName,deltaTime, countReps,
                            redactTime, isChecked, position));
    }

    public long getCopyFile(String fileName){
       return storageEditor.getCopyFile(fileName);
    }

    public void revertEdit(String fileName, long fileIdCopy){
        data.setValue(storageEditor.revertEdit(fileName, fileIdCopy));
    }

    public void clearCopyFile(long fileIdCopy, String fileName){
        storageEditor.clearCopyFile(fileIdCopy,fileName);
    }

    public void changeTemp(String finishFileName, int valueDelta, boolean up){
        data.setValue(storageEditor.changeTemp(finishFileName, valueDelta, up));
    }

    public DataSet getDataSetNew(String fileName){
        return storageEditor.getDataSetNew(fileName);
    }

    public void  addSet(DataSet dataSet,String finishFileName){
        data.setValue(storageEditor.addSet(dataSet, finishFileName));
    }

    public float getSumOfTimes(String finishFileName){
        return storageEditor.getSumOfTimes(finishFileName);
    }

    public int getSumOfReps(String finishFileName){
        return storageEditor.getSumOfReps(finishFileName);
    }

    public int getFragmentsCount(String finishFileName){
        return storageEditor.getFragmentsCount(finishFileName);
    }

    public long saveAsFile(String oldFileName, String newFileName, long fileOldIdCopy){
        return  storageEditor.saveAsFile(oldFileName, newFileName, fileOldIdCopy);
    }


}
