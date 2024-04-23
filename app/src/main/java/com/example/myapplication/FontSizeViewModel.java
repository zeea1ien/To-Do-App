package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FontSizeViewModel extends ViewModel {
    private MutableLiveData<Integer> fontSize = new MutableLiveData<>();

    public LiveData<Integer> getFontSize() {
        return fontSize;
    }

    public void setFontSize(int size) {
        fontSize.setValue(size);
    }

}