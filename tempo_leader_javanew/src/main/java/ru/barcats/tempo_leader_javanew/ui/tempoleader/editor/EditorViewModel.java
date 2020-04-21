package ru.barcats.tempo_leader_javanew.ui.tempoleader.editor;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ru.barcats.tempo_leader_javanew.model.DataSet;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag.DateTimeTempStorage;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag.DateTimeTempStorageImpl;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag.RaskladkiTempStorage;
import ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.temp_frag.RaskladkiTempStorageImpl;
import ru.barcats.tempo_leader_javanew.ui.tempoleader.TempoleaderStorage;

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

}
