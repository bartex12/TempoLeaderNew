package ru.barcats.tempo_leader_javanew.ui.tempoleader.dialogsave;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class SaveViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<DataSet>> data = new    MutableLiveData<ArrayList<DataSet>>();
    SaveStorage storage;

    public SaveViewModel(@NonNull Application application) {
        super(application);
        storage = new SaveStorageImpl(application);
    }

  public long saveAsFile(String oldFileName, String newFileName, long fileOldIdCopy){
     return storage.saveAsFile(oldFileName, newFileName, fileOldIdCopy);
    }
}
