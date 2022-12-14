package com.example.musicalevents.mvp.login

import com.example.musicalevents.base.BasePresenterKt
import com.example.musicalevents.base.OnRepositoryCallback
import com.example.musicalevents.data.model.Userkt

/**
 * Contrato del Login
 * @author Rafa
 */

interface LoginContractKt {
    interface View : OnRepositoryCallback {
        fun setEmailEmptyError()
        fun setPasswordEmptyError()
        fun setPasswordError()
    }

    interface Presenter : BasePresenterKt {
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