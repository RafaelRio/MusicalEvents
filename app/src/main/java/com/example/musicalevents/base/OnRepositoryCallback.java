package com.example.musicalevents.base;

import com.example.musicalevents.data.model.Userkt;

public interface OnRepositoryCallback {

    void onSuccess(Userkt u);
    void onFailure(String message);
}
