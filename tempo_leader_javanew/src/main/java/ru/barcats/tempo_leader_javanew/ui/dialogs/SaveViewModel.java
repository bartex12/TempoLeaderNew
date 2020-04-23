package ru.barcats.tempo_leader_javanew.ui.dialogs;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.barcats.tempo_leader_javanew.model.DataSet;

public class SaveViewModel extends AndroidViewModel {
    MutableLiveData<ArrayList<DataSet>> data = new    MutableLiveData<ArrayList<DataSet>>();
    SaveAsStorage storage;

    public SaveViewModel(@NonNull Application application) {
        super(application);
        storage = new SaveAsStorageImpl(application);
    }

  long saveAsFile(String oldFileName, String newFileName, long fileOldIdCopy){
     return storage.saveAsFile(oldFileName, newFileName, fileOldIdCopy);
    }
}
