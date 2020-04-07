package ru.barcats.tempo_leader_javanew.ui.sekundomer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SecundomerViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public SecundomerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is секундомер fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}