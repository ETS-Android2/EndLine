package com.example.endline_v1.ui.beauty;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BeautyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public BeautyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is beauty fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}