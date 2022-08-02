package com.carollim.myrecycleapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {
    private final MutableLiveData<String> selectedAddress = new MutableLiveData<String>();
    public void setData(String item){
        selectedAddress.setValue(item);
    }
    public LiveData<String> getSelectedAddress(){
        return selectedAddress;
    }
}
