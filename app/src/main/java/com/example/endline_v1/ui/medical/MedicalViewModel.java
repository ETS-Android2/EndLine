package com.example.endline_v1.ui.medical;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MedicalViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MedicalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is medical fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}