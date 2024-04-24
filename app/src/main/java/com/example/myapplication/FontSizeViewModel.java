package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FontSizeViewModel extends ViewModel {
    private MutableLiveData<Integer> fontSize = new MutableLiveData<>();
//MutableLiveData to hold current font size. view model to see changes across components
    public LiveData<Integer> getFontSize() {
        return fontSize;
    }
// method that allows changes from UI interactions and updates the font size stored in View model
    public void setFontSize(int size) {
        fontSize.setValue(size);
    }

}

// view model to manage the font size settings across all UI bits