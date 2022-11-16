package com.example.musicalevents.base

import com.example.musicalevents.data.model.Userkt

interface OnRepositoryCallback {
    fun onSuccess(u: Userkt)
    fun onFailure(message: Int)
}