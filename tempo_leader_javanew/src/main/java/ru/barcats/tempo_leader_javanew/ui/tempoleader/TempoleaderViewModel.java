package ru.barcats.tempo_leader_javanew.ui.tempoleader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TempoleaderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TempoleaderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is темполидер fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}