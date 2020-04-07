package ru.barcats.tempo_leader_javanew.ui.raskladki;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RaskladkiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RaskladkiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is раскладки  fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}