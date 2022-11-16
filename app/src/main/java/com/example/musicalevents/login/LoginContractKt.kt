package com.example.musicalevents.login

import com.example.musicalevents.base.BasePresenter
import com.example.musicalevents.base.IProgressView
import com.example.musicalevents.base.OnRepositoryCallback
import com.example.musicalevents.data.model.Userkt

interface LoginContractKt {
    interface View : OnRepositoryCallback, IProgressView {
        fun setEmailEmptyError()
        fun setPasswordEmptyError()
        fun setPasswordError()
    }

    interface Presenter : BasePresenter {
        fun validateCredentials(user: Userkt?)
    }

    interface Repository {
        fun login(user: Userkt?)
    }

    interface OnInteractorListener : OnRepositoryCallback {
        fun onEmailEmptyError()
        fun onPasswordEmptyError()
        fun onPasswordError()
    }
}