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
    private EditorStorage storageEditor;

    public EditorViewModel(@NonNull Application application) {
        super(application);
        storageEditor = new EditorStorageImpl(application);
    }

    public LiveData<ArrayList<DataSet>> loadDataSet(String fileName) {
        loadData(fileName);
        return data;
    }

    private void loadData(String fileName){
        //
        data.setValue(storageEditor.getDataSet(fileName));
    }

    public void minus5Action(String fileName, float deltaTime, int countReps , boolean redactTime,
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

}
