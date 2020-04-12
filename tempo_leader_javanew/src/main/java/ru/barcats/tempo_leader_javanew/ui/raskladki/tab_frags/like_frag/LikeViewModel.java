package ru.barcats.tempo_leader_javanew.ui.raskladki.tab_frags.like_frag;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LikeViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<String>> data;
    private RaskladkiLikeStorage storage;

    public LikeViewModel(@NonNull Application application) {
        super(application);
        storage = new RaskladkiLikeStorageImpl(application);
    }


    public LiveData<ArrayList<String>> getRascladki() {
        if (data == null) {
            data = new MutableLiveData<>();
            loadData();
        }
        return data;
    }

    private void loadData(){
        data.setValue(storage.getRaskladkiList());
    }
}