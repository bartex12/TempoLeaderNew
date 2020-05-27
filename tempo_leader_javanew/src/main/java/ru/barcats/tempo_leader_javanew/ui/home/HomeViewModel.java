package ru.barcats.tempo_leader_javanew.ui.home;

import android.app.Application;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.barcats.tempo_leader_javanew.model.DataHome;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<DataHome>> data;
    private HomeStorage storage;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        storage = new HomeStorageImpl(application);
    }

    public LiveData<ArrayList<DataHome>> getListMain() {
        if (data == null) {
            //создаём экземпляр класса
            data = new MutableLiveData<>();
            //загружаем данные в LiveData
            getData();
        }
        return data;
    }

    private void getData() {
        ArrayList<DataHome> list = storage.getListMain();
        data.setValue(list);
    }

    public void createDefaultFile() {
        storage.createDefoultFile();
    }
}