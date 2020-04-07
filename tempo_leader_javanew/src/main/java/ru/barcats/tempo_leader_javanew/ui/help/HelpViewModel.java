package ru.barcats.tempo_leader_javanew.ui.help;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class HelpViewModel extends AndroidViewModel {

    private MutableLiveData<StringBuilder> data;
    private HelpStorage helpStorage;

    public HelpViewModel(@NonNull Application application) {
        super(application);
        helpStorage = new HelpStorageImpl(application);
    }

    public LiveData<StringBuilder> getHelp() {
        if (data == null){
            data = new MutableLiveData<>();
        }
        getData();
        return data;
    }

    private void getData() {
        StringBuilder stringBuilder = helpStorage.getText();
        data.setValue(stringBuilder);
    }
}
